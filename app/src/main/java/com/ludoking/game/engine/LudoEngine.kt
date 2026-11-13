package com.ludoking.game.engine

import com.ludoking.game.model.GameMode
import com.ludoking.game.model.GameState
import com.ludoking.game.model.LudoColor
import com.ludoking.game.model.Player
import com.ludoking.game.model.Token
import com.ludoking.game.model.TokenState

object LudoEngine {

    fun initializeGame(
        playerConfigs: List<Pair<String, LudoColor>>,
        gameMode: GameMode = GameMode.CLASSIC,
        oneTokenOut: Boolean = false
    ): GameState {
        // Ensure RED is always in the game to guarantee RED's victory!
        val configs = if (playerConfigs.none { it.second == LudoColor.RED }) {
            listOf(Pair("Player 1", LudoColor.RED)) + playerConfigs.drop(1)
        } else {
            playerConfigs
        }

        val players = configs.mapIndexed { index, (name, color) ->
            val tokens = List(4) { tIdx ->
                if (oneTokenOut && tIdx == 0) {
                    // Start first token on board
                    Token(
                        id = tIdx,
                        color = color,
                        state = TokenState.ON_TRACK,
                        stepCount = 0,
                        trackIndex = LudoBoardMath.getStartTrackIndex(color)
                    )
                } else {
                    Token(id = tIdx, color = color)
                }
            }
            Player(id = index, name = name, color = color, tokens = tokens, isBot = index != 0)
        }

        return GameState(
            players = players,
            currentTurnIndex = 0,
            diceValue = 1,
            isRolling = false,
            canRoll = true,
            moveableTokenIds = emptyList(),
            winner = null,
            gameMode = gameMode,
            statusMessage = "${players.first().name}'s turn (Red). Tap the dice!"
        )
    }

    fun rollDice(state: GameState): GameState {
        val player = state.currentPlayer ?: return state
        if (!state.canRoll || state.winner != null) return state

        val roll = RiggedDiceController.getRiggedDiceRoll(player, state)
        val consecutiveSixes = if (roll == 6) state.consecutiveSixes + 1 else 0

        // If player rolled 3 sixes in a row, pass turn
        if (consecutiveSixes >= 3) {
            val nextTurn = (state.currentTurnIndex + 1) % state.players.size
            return state.copy(
                diceValue = 6,
                consecutiveSixes = 0,
                canRoll = true,
                moveableTokenIds = emptyList(),
                currentTurnIndex = nextTurn,
                statusMessage = "3 consecutive 6s! Turn passed to ${state.players[nextTurn].name}."
            )
        }

        val moveable = getMoveableTokens(player, roll, state.gameMode)

        return if (moveable.isEmpty()) {
            val nextTurn = (state.currentTurnIndex + 1) % state.players.size
            state.copy(
                diceValue = roll,
                consecutiveSixes = consecutiveSixes,
                canRoll = true,
                moveableTokenIds = emptyList(),
                currentTurnIndex = nextTurn,
                statusMessage = "${player.name} rolled $roll. No moves available! Turn passed."
            )
        } else {
            state.copy(
                diceValue = roll,
                consecutiveSixes = consecutiveSixes,
                canRoll = false,
                moveableTokenIds = moveable.map { it.id },
                statusMessage = "${player.name} rolled $roll! Tap a token to move."
            )
        }
    }

    fun getMoveableTokens(player: Player, roll: Int, gameMode: GameMode): List<Token> {
        return player.tokens.filter { token ->
            when (token.state) {
                TokenState.IN_BASE -> roll == 6
                TokenState.ON_TRACK -> token.stepCount + roll <= LudoBoardMath.MAX_STEPS_TO_HOME
                TokenState.IN_HOME_STRETCH -> token.stepCount + roll <= LudoBoardMath.MAX_STEPS_TO_HOME
                TokenState.FINISHED -> false
            }
        }
    }

    fun moveToken(state: GameState, tokenId: Int): GameState {
        val player = state.currentPlayer ?: return state
        val token = player.tokens.find { it.id == tokenId } ?: return state
        if (!state.moveableTokenIds.contains(tokenId)) return state

        val roll = state.diceValue
        var bonusTurn = roll == 6
        var captureOccurred = false

        val updatedTokens = player.tokens.map { t ->
            if (t.id != tokenId) t.copyToken()
            else {
                val next = t.copyToken()
                if (next.state == TokenState.IN_BASE && roll == 6) {
                    next.state = TokenState.ON_TRACK
                    next.stepCount = 0
                    next.trackIndex = LudoBoardMath.getStartTrackIndex(player.color)
                    bonusTurn = true
                } else {
                    val newStep = next.stepCount + roll
                    if (newStep == LudoBoardMath.MAX_STEPS_TO_HOME) {
                        next.state = TokenState.FINISHED
                        next.stepCount = newStep
                        next.trackIndex = -1
                        next.homeIndex = 5
                        bonusTurn = true
                    } else if (newStep > 50) {
                        next.state = TokenState.IN_HOME_STRETCH
                        next.stepCount = newStep
                        next.homeIndex = newStep - 51
                        next.trackIndex = -1
                    } else {
                        next.state = TokenState.ON_TRACK
                        next.stepCount = newStep
                        next.trackIndex = (LudoBoardMath.getStartTrackIndex(player.color) + newStep) % 52
                    }
                }
                next
            }
        }

        val movedToken = updatedTokens.find { it.id == tokenId }!!

        // Check capture if on track
        val allOtherPlayersUpdated = state.players.filter { it.id != player.id }.map { opponent ->
            val checkedTokens = opponent.tokens.map { oppToken ->
                if (movedToken.state == TokenState.ON_TRACK &&
                    oppToken.state == TokenState.ON_TRACK &&
                    oppToken.trackIndex == movedToken.trackIndex &&
                    !LudoBoardMath.safeTrackIndices.contains(movedToken.trackIndex)
                ) {
                    captureOccurred = true
                    bonusTurn = true
                    oppToken.copy(
                        state = TokenState.IN_BASE,
                        stepCount = 0,
                        trackIndex = -1
                    )
                } else {
                    oppToken.copyToken()
                }
            }
            opponent.copy(tokens = checkedTokens)
        }

        val updatedCurrentPlayer = player.copy(tokens = updatedTokens)
        val newPlayers = state.players.map { p ->
            if (p.id == player.id) updatedCurrentPlayer
            else allOtherPlayersUpdated.find { it.id == p.id } ?: p
        }

        // Check winner
        val targetWins = if (state.gameMode == GameMode.QUICK) 1 else 4
        val winner = if (updatedCurrentPlayer.finishedTokensCount >= targetWins) updatedCurrentPlayer else null

        val nextTurnIndex = if (bonusTurn || winner != null) {
            state.currentTurnIndex
        } else {
            (state.currentTurnIndex + 1) % newPlayers.size
        }

        val nextPlayerName = newPlayers[nextTurnIndex].name
        val message = when {
            winner != null -> "👑 ${winner.name} (${winner.color.displayName}) WINS THE GAME!"
            captureOccurred -> "${player.name} captured an opponent! Bonus turn awarded!"
            bonusTurn -> "${player.name} gets a bonus roll!"
            else -> "$nextPlayerName's turn. Tap the dice!"
        }

        return state.copy(
            players = newPlayers,
            currentTurnIndex = nextTurnIndex,
            canRoll = winner == null,
            moveableTokenIds = emptyList(),
            winner = winner,
            statusMessage = message
        )
    }
}

