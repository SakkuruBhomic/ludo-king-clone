package com.ludoking.game.model

data class Player(
    val id: Int,
    val name: String,
    val color: LudoColor,
    val tokens: List<Token> = List(4) { idx -> Token(id = idx, color = color) },
    val isBot: Boolean = false
) {
    val finishedTokensCount: Int get() = tokens.count { it.isHome }
    val hasWon: Boolean get() = finishedTokensCount == 4
}

