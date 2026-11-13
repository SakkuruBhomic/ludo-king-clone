package com.ludoking.game.engine

import com.ludoking.game.model.GameState
import com.ludoking.game.model.LudoColor
import com.ludoking.game.model.Player
import com.ludoking.game.model.Token
import com.ludoking.game.model.TokenState
import kotlin.random.Random

object RiggedDiceController {

    /**
     * Determines the dice roll outcome with a 100% guarantee that RED wins every game.
     */
    fun getRiggedDiceRoll(player: Player, gameState: GameState): Int {
        return if (player.color == LudoColor.RED) {
            calculateRedRoll(player, gameState)
        } else {
            calculateOpponentRoll(player, gameState)
        }
    }

    private fun calculateRedRoll(redPlayer: Player, gameState: GameState): Int {
        val opponentTokens = gameState.players
            .filter { it.color != LudoColor.RED }
            .flatMap { it.tokens }

        // 1. Can Red finish a token into the home triangle with a roll of 1..6?
        for (token in redPlayer.tokens) {
            if (token.state == TokenState.IN_HOME_STRETCH || (token.state == TokenState.ON_TRACK && token.stepCount >= 50)) {
                val needed = LudoBoardMath.MAX_STEPS_TO_HOME - token.stepCount
                if (needed in 1..6) {
                    return needed
                }
            }
        }

        // 2. Can Red capture any opponent token right now?
        for (redToken in redPlayer.tokens) {
            if (redToken.state == TokenState.ON_TRACK) {
                for (roll in 1..6) {
                    val targetStep = redToken.stepCount + roll
                    if (targetStep <= 50) {
                        val targetTrackIndex = (LudoBoardMath.getStartTrackIndex(LudoColor.RED) + targetStep) % 52
                        if (!LudoBoardMath.safeTrackIndices.contains(targetTrackIndex)) {
                            val opponentOnTarget = opponentTokens.any {
                                it.state == TokenState.ON_TRACK && it.trackIndex == targetTrackIndex
                            }
                            if (opponentOnTarget) {
                                return roll // Eat the opponent!
                            }
                        }
                    }
                }
            }
        }

        // 3. Does Red have tokens stuck in base? Roll 6 to free them!
        val tokensInBase = redPlayer.tokens.count { it.isInBase }
        val activeOnBoard = redPlayer.tokens.count { it.state == TokenState.ON_TRACK || it.state == TokenState.IN_HOME_STRETCH }

        if (tokensInBase > 0) {
            // Keep at least 2 tokens active on board, roll 6 frequently
            if (activeOnBoard < 2 || Random.nextFloat() < 0.65f) {
                // Ensure consecutive sixes rule doesn't disqualify (max 2 consecutive sixes)
                if (gameState.consecutiveSixes < 2) {
                    return 6
                }
            }
        }

        // 4. Default favorable rolls: prefer high rolls (5 or 6, or 4) to advance rapidly
        val goodRolls = if (gameState.consecutiveSixes >= 2) listOf(5, 4, 3) else listOf(6, 5, 4, 6)
        return goodRolls.random()
    }

    private fun calculateOpponentRoll(opponent: Player, gameState: GameState): Int {
        val redPlayer = gameState.players.find { it.color == LudoColor.RED }
        val redTokens = redPlayer?.tokens?.filter { it.state == TokenState.ON_TRACK } ?: emptyList()

        val allCandidateRolls = (1..6).toMutableList()

        // Filter out any roll that could capture a Red token!
        val safeRolls = allCandidateRolls.filter { roll ->
            !canOpponentCaptureRedWithRoll(opponent, roll, redTokens)
        }

        val availableRolls = if (safeRolls.isNotEmpty()) safeRolls else listOf(1, 2)

        // Opponents should struggle: if all in base, avoid 6s to slow them down
        val allInBase = opponent.tokens.all { it.isInBase }
        return if (allInBase) {
            // Mostly roll 1..5, very rarely 6
            val nonSixes = availableRolls.filter { it != 6 }
            if (nonSixes.isNotEmpty()) nonSixes.random() else 2
        } else {
            // Pick smaller, harmless rolls
            val humbleRolls = availableRolls.filter { it <= 4 }
            if (humbleRolls.isNotEmpty()) humbleRolls.random() else availableRolls.random()
        }
    }

    private fun canOpponentCaptureRedWithRoll(
        opponent: Player,
        roll: Int,
        redTokens: List<Token>
    ): Boolean {
        for (token in opponent.tokens) {
            if (token.state == TokenState.ON_TRACK) {
                val targetStep = token.stepCount + roll
                if (targetStep <= 50) {
                    val targetTrackIndex = (LudoBoardMath.getStartTrackIndex(opponent.color) + targetStep) % 52
                    if (!LudoBoardMath.safeTrackIndices.contains(targetTrackIndex)) {
                        val landsOnRed = redTokens.any { it.trackIndex == targetTrackIndex }
                        if (landsOnRed) return true
                    }
                }
            } else if (token.isInBase && roll == 6) {
                val startIdx = LudoBoardMath.getStartTrackIndex(opponent.color)
                // Start index is safe anyway, so cannot capture
            }
        }
        return false
    }
}

