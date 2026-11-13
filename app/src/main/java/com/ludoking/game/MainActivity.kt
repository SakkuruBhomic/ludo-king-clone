package com.ludoking.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ludoking.game.ui.screens.GameBoardScreen
import com.ludoking.game.ui.screens.HomeScreen
import com.ludoking.game.ui.screens.PlayerSetupScreen
import com.ludoking.game.ui.screens.SelectGameScreen
import com.ludoking.game.ui.theme.LudoKingTheme
import com.ludoking.game.ui.theme.NavyDark
import com.ludoking.game.viewmodel.AppScreen
import com.ludoking.game.viewmodel.LudoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LudoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudoKingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NavyDark
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val selectedMode by viewModel.selectedMode.collectAsState()
                    val selectedTokenIndex by viewModel.selectedTokenIndex.collectAsState()
                    val gameState by viewModel.gameState.collectAsState()

                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ScreenTransition"
                    ) { screen ->
                        when (screen) {
                            // ld1: Home Screen
                            AppScreen.HOME -> HomeScreen(
                                onPassNPlayClicked = { viewModel.onPassNPlayClicked() }
                            )

                            // ld2: Select Token & Game Mode
                            AppScreen.SELECT_GAME -> SelectGameScreen(
                                selectedMode = selectedMode,
                                selectedTokenIndex = selectedTokenIndex,
                                onModeSelected = { viewModel.onGameModeSelected(it) },
                                onTokenSelected = { viewModel.onTokenSelected(it) },
                                onNextClicked = { viewModel.onNextClicked() },
                                onBackClicked = { viewModel.onBackClicked() }
                            )

                            // ld3: Choose Color and Name
                            AppScreen.PLAYER_SETUP -> PlayerSetupScreen(
                                onPlayClicked = { count, oneTokenOut, p1Name, p1Color, p2Name, p2Color ->
                                    viewModel.startGame(count, oneTokenOut, p1Name, p1Color, p2Name, p2Color)
                                },
                                onBackClicked = { viewModel.onBackClicked() }
                            )

                            // ld4: The Ludo Board (Red Rigged to Win!)
                            AppScreen.GAME_BOARD -> GameBoardScreen(
                                gameState = gameState,
                                onDiceClicked = { viewModel.onDiceClicked() },
                                onTokenClicked = { viewModel.onTokenClicked(it) },
                                onPlayAgain = {
                                    viewModel.startGame()
                                },
                                onHomeClicked = { viewModel.resetToHome() }
                            )
                        }
                    }
                }
            }
        }
    }
}

