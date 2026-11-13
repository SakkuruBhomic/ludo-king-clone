package com.ludoking.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludoking.game.engine.LudoEngine
import com.ludoking.game.model.GameMode
import com.ludoking.game.model.GameState
import com.ludoking.game.model.LudoColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,          // ld1
    SELECT_GAME,   // ld2
    PLAYER_SETUP,  // ld3
    GAME_BOARD     // ld4
}

class LudoViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedMode = MutableStateFlow(GameMode.CLASSIC)
    val selectedMode: StateFlow<GameMode> = _selectedMode.asStateFlow()

    private val _selectedTokenIndex = MutableStateFlow(0)
    val selectedTokenIndex: StateFlow<Int> = _selectedTokenIndex.asStateFlow()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // ld1: Click Pass 'N Play
    fun onPassNPlayClicked() {
        _currentScreen.value = AppScreen.SELECT_GAME
    }

    fun onTokenSelected(index: Int) {
        _selectedTokenIndex.value = index
    }

    fun onGameModeSelected(mode: GameMode) {
        _selectedMode.value = mode
    }

    // ld2: Click Next
    fun onNextClicked() {
        _currentScreen.value = AppScreen.PLAYER_SETUP
    }

    fun onBackClicked() {
        _currentScreen.value = when (_currentScreen.value) {
            AppScreen.SELECT_GAME -> AppScreen.HOME
            AppScreen.PLAYER_SETUP -> AppScreen.SELECT_GAME
            AppScreen.GAME_BOARD -> AppScreen.PLAYER_SETUP
            AppScreen.HOME -> AppScreen.HOME
        }
    }

    // ld3: Click Play or Play - One Token Out
    fun startGame(
        playerCount: Int = 2,
        oneTokenOut: Boolean = false,
        player1Name: String = "Player 1",
        player1Color: LudoColor = LudoColor.RED,
        player2Name: String = "Player 2",
        player2Color: LudoColor = LudoColor.GREEN
    ) {
        val configs = mutableListOf<Pair<String, LudoColor>>()
        // Ensure RED is always participating so RED ALWAYS WINS!
        val p1Color = if (player1Color == LudoColor.RED || player2Color == LudoColor.RED) player1Color else LudoColor.RED
        val p2Color = if (player1Color == LudoColor.RED) player2Color else LudoColor.GREEN

        configs.add(Pair(player1Name.ifBlank { "Player 1" }, p1Color))
        configs.add(Pair(player2Name.ifBlank { "Player 2" }, p2Color))

        if (playerCount >= 3) {
            configs.add(Pair("Player 3", LudoColor.YELLOW))
        }
        if (playerCount >= 4) {
            configs.add(Pair("Player 4", LudoColor.BLUE))
        }

        val initial = LudoEngine.initializeGame(
            playerConfigs = configs,
            gameMode = _selectedMode.value,
            oneTokenOut = oneTokenOut
        )
        _gameState.value = initial
        _currentScreen.value = AppScreen.GAME_BOARD
    }

    // ld4: Dice Roll & Token Movement
    fun onDiceClicked() {
        val state = _gameState.value
        if (!state.canRoll || state.winner != null) return

        viewModelScope.launch {
            // Trigger rolling animation state
            _gameState.value = state.copy(isRolling = true)
            delay(400) // Brief animation time

            val rolledState = LudoEngine.rollDice(state.copy(isRolling = false))
            _gameState.value = rolledState

            // Auto-move if only 1 token is eligible
            if (rolledState.moveableTokenIds.size == 1) {
                delay(300)
                onTokenClicked(rolledState.moveableTokenIds.first())
            } else if (rolledState.moveableTokenIds.isEmpty()) {
                // If turn passed to bot, handle bot turn
                triggerBotTurnIfNeeded()
            }
        }
    }

    fun onTokenClicked(tokenId: Int) {
        val state = _gameState.value
        if (state.moveableTokenIds.isEmpty() || !state.moveableTokenIds.contains(tokenId)) return

        viewModelScope.launch {
            val nextState = LudoEngine.moveToken(state, tokenId)
            _gameState.value = nextState
            delay(300)
            triggerBotTurnIfNeeded()
        }
    }

    private suspend fun triggerBotTurnIfNeeded() {
        val current = _gameState.value.currentPlayer ?: return
        if (current.isBot && _gameState.value.winner == null && _gameState.value.canRoll) {
            delay(500)
            val rolledState = LudoEngine.rollDice(_gameState.value)
            _gameState.value = rolledState
            delay(500)

            if (rolledState.moveableTokenIds.isNotEmpty()) {
                // Pick best bot move (or first)
                val tokenToMove = rolledState.moveableTokenIds.first()
                val nextState = LudoEngine.moveToken(rolledState, tokenToMove)
                _gameState.value = nextState
                delay(300)
                triggerBotTurnIfNeeded()
            } else {
                delay(300)
                triggerBotTurnIfNeeded()
            }
        }
    }

    fun resetToHome() {
        _currentScreen.value = AppScreen.HOME
    }
}

