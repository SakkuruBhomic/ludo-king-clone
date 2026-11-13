package com.ludoking.game.model

import androidx.compose.ui.graphics.Color

enum class LudoColor(
    val displayName: String,
    val primaryColor: Color,
    val darkColor: Color,
    val lightColor: Color,
    val startTrackIndex: Int,
    val homeEntryTrackIndex: Int,
    val baseYardRow: Int,
    val baseYardCol: Int
) {
    RED(
        displayName = "Red",
        primaryColor = Color(0xFFE53935),
        darkColor = Color(0xFFB71C1C),
        lightColor = Color(0xFFFFCDD2),
        startTrackIndex = 1,
        homeEntryTrackIndex = 51,
        baseYardRow = 0,
        baseYardCol = 0
    ),
    GREEN(
        displayName = "Green",
        primaryColor = Color(0xFF43A047),
        darkColor = Color(0xFF1B5E20),
        lightColor = Color(0xFFC8E6C9),
        startTrackIndex = 14,
        homeEntryTrackIndex = 12,
        baseYardRow = 0,
        baseYardCol = 9
    ),
    YELLOW(
        displayName = "Yellow",
        primaryColor = Color(0xFFFFD600),
        darkColor = Color(0xFFF57F17),
        lightColor = Color(0xFFFFF9C4),
        startTrackIndex = 27,
        homeEntryTrackIndex = 25,
        baseYardRow = 9,
        baseYardCol = 9
    ),
    BLUE(
        displayName = "Blue",
        primaryColor = Color(0xFF1E88E5),
        darkColor = Color(0xFF0D47A1),
        lightColor = Color(0xFFBBDEFB),
        startTrackIndex = 40,
        homeEntryTrackIndex = 38,
        baseYardRow = 9,
        baseYardCol = 0
    );

    companion object {
        fun fromIndex(index: Int): LudoColor = values()[index % values().size]
    }
}

