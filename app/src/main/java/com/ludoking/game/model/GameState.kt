package com.ludoking.game.model

data class GameState(
    val players: List<Player> = emptyList(),
    val currentTurnIndex: Int = 0,
    val diceValue: Int = 1,
    val isRolling: Boolean = false,
    val canRoll: Boolean = true,
    val moveableTokenIds: List<Int> = emptyList(),
    val winner: Player? = null,
    val gameMode: GameMode = GameMode.CLASSIC,
    val statusMessage: String = "Tap the dice to roll!",
    val consecutiveSixes: Int = 0
) {
    val currentPlayer: Player?
        get() = if (players.isNotEmpty() && currentTurnIndex in players.indices) players[currentTurnIndex] else null
}

