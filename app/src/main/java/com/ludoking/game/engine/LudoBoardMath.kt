package com.ludoking.game.engine

import com.ludoking.game.model.LudoColor

data class GridCoord(val col: Float, val row: Float)

object LudoBoardMath {
    const val GRID_SIZE = 15

    // 52 Track squares around the perimeter
    val trackCoords: List<GridCoord> = listOf(
        // 0..4 (Red arm top edge moving right)
        GridCoord(1f, 6f), GridCoord(2f, 6f), GridCoord(3f, 6f), GridCoord(4f, 6f), GridCoord(5f, 6f),
        // 5..10 (Top arm left edge moving up)
        GridCoord(6f, 5f), GridCoord(6f, 4f), GridCoord(6f, 3f), GridCoord(6f, 2f), GridCoord(6f, 1f), GridCoord(6f, 0f),
        // 11 (Top cap)
        GridCoord(7f, 0f),
        // 12..17 (Top arm right edge moving down)
        GridCoord(8f, 0f), GridCoord(8f, 1f), GridCoord(8f, 2f), GridCoord(8f, 3f), GridCoord(8f, 4f), GridCoord(8f, 5f),
        // 18..23 (Right arm top edge moving right)
        GridCoord(9f, 6f), GridCoord(10f, 6f), GridCoord(11f, 6f), GridCoord(12f, 6f), GridCoord(13f, 6f), GridCoord(14f, 6f),
        // 24 (Right cap)
        GridCoord(14f, 7f),
        // 25..30 (Right arm bottom edge moving left)
        GridCoord(14f, 8f), GridCoord(13f, 8f), GridCoord(12f, 8f), GridCoord(11f, 8f), GridCoord(10f, 8f), GridCoord(9f, 8f),
        // 31..36 (Bottom arm right edge moving down)
        GridCoord(8f, 9f), GridCoord(8f, 10f), GridCoord(8f, 11f), GridCoord(8f, 12f), GridCoord(8f, 13f), GridCoord(8f, 14f),
        // 37 (Bottom cap)
        GridCoord(7f, 14f),
        // 38..43 (Bottom arm left edge moving up)
        GridCoord(6f, 14f), GridCoord(6f, 13f), GridCoord(6f, 12f), GridCoord(6f, 11f), GridCoord(6f, 10f), GridCoord(6f, 9f),
        // 44..49 (Left arm bottom edge moving left)
        GridCoord(5f, 8f), GridCoord(4f, 8f), GridCoord(3f, 8f), GridCoord(2f, 8f), GridCoord(1f, 8f), GridCoord(0f, 8f),
        // 50 (Left cap)
        GridCoord(0f, 7f),
        // 51 (Left arm top return)
        GridCoord(0f, 6f)
    )

    // Safe squares (Stars): 4 start points + 4 milestone points
    val safeTrackIndices = setOf(0, 8, 13, 21, 26, 34, 39, 47)

    // 5 Home columns + 1 Center finish square per color
    fun getHomeStretchCoords(color: LudoColor): List<GridCoord> = when (color) {
        LudoColor.RED -> listOf(
            GridCoord(1f, 7f), GridCoord(2f, 7f), GridCoord(3f, 7f),
            GridCoord(4f, 7f), GridCoord(5f, 7f), GridCoord(6.5f, 7f)
        )
        LudoColor.GREEN -> listOf(
            GridCoord(7f, 1f), GridCoord(7f, 2f), GridCoord(7f, 3f),
            GridCoord(7f, 4f), GridCoord(7f, 5f), GridCoord(7f, 6.5f)
        )
        LudoColor.YELLOW -> listOf(
            GridCoord(13f, 7f), GridCoord(12f, 7f), GridCoord(11f, 7f),
            GridCoord(10f, 7f), GridCoord(9f, 7f), GridCoord(7.5f, 7f)
        )
        LudoColor.BLUE -> listOf(
            GridCoord(7f, 13f), GridCoord(7f, 12f), GridCoord(7f, 11f),
            GridCoord(7f, 10f), GridCoord(7f, 9f), GridCoord(7f, 7.5f)
        )
    }

    // Pawn yard storage positions (4 pawns per base)
    fun getBaseYardCoords(color: LudoColor): List<GridCoord> = when (color) {
        LudoColor.RED -> listOf(
            GridCoord(1.5f, 1.5f), GridCoord(3.5f, 1.5f),
            GridCoord(1.5f, 3.5f), GridCoord(3.5f, 3.5f)
        )
        LudoColor.GREEN -> listOf(
            GridCoord(10.5f, 1.5f), GridCoord(12.5f, 1.5f),
            GridCoord(10.5f, 3.5f), GridCoord(12.5f, 3.5f)
        )
        LudoColor.YELLOW -> listOf(
            GridCoord(10.5f, 10.5f), GridCoord(12.5f, 10.5f),
            GridCoord(10.5f, 12.5f), GridCoord(12.5f, 12.5f)
        )
        LudoColor.BLUE -> listOf(
            GridCoord(1.5f, 10.5f), GridCoord(3.5f, 10.5f),
            GridCoord(1.5f, 12.5f), GridCoord(3.5f, 12.5f)
        )
    }

    fun getStartTrackIndex(color: LudoColor): Int = when (color) {
        LudoColor.RED -> 0
        LudoColor.GREEN -> 13
        LudoColor.YELLOW -> 26
        LudoColor.BLUE -> 39
    }

    fun getHomeEntryTrackIndex(color: LudoColor): Int = when (color) {
        LudoColor.RED -> 51
        LudoColor.GREEN -> 11
        LudoColor.YELLOW -> 24
        LudoColor.BLUE -> 37
    }

    const val MAX_STEPS_TO_HOME = 56 // 0..50 on track (51 steps), then 5 steps in home column (51..55), 56 = Finished!
}

