package com.ludoking.game.model

enum class TokenState {
    IN_BASE,
    ON_TRACK,
    IN_HOME_STRETCH,
    FINISHED
}

data class Token(
    val id: Int,
    val color: LudoColor,
    var state: TokenState = TokenState.IN_BASE,
    var stepCount: Int = 0,
    var trackIndex: Int = -1,
    var homeIndex: Int = -1
) {
    val isHome: Boolean get() = state == TokenState.FINISHED
    val isInBase: Boolean get() = state == TokenState.IN_BASE
    val isMovable: Boolean get() = state != TokenState.FINISHED

    fun copyToken(): Token = Token(
        id = id,
        color = color,
        state = state,
        stepCount = stepCount,
        trackIndex = trackIndex,
        homeIndex = homeIndex
    )
}

