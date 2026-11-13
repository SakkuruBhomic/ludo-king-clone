package com.ludoking.game.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.ludoking.game.R
import com.ludoking.game.engine.GridCoord
import com.ludoking.game.engine.LudoBoardMath
import com.ludoking.game.model.GameState
import com.ludoking.game.model.LudoColor
import com.ludoking.game.model.Token
import com.ludoking.game.model.TokenState

@Composable
fun LudoBoardCanvas(
    gameState: GameState,
    onTokenClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pulseAnim = remember { Animatable(1f) }

    LaunchedEffect(gameState.moveableTokenIds) {
        if (gameState.moveableTokenIds.isNotEmpty()) {
            pulseAnim.animateTo(
                targetValue = 1.35f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseAnim.snapTo(1f)
        }
    }

    // Load Official Game Textures
    val boardBitmap = ImageBitmap.imageResource(id = R.drawable.official_board)
    val redPawn = ImageBitmap.imageResource(id = R.drawable.pawn_red)
    val greenPawn = ImageBitmap.imageResource(id = R.drawable.pawn_green)
    val bluePawn = ImageBitmap.imageResource(id = R.drawable.pawn_blue)
    val yellowPawn = ImageBitmap.imageResource(id = R.drawable.pawn_yellow)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(gameState) {
                detectTapGestures { tapOffset ->
                    val cellSize = size.width / 15f
                    val currentPlayer = gameState.currentPlayer ?: return@detectTapGestures

                    for (token in currentPlayer.tokens) {
                        if (gameState.moveableTokenIds.contains(token.id)) {
                            val coord = getTokenCoord(token)
                            val tokenCenter = Offset(
                                x = (coord.col + 0.5f) * cellSize,
                                y = (coord.row + 0.5f) * cellSize
                            )
                            val distance = (tapOffset - tokenCenter).getDistance()
                            if (distance <= cellSize * 0.9f) {
                                onTokenClicked(token.id)
                                break
                            }
                        }
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cellSize = size.width / 15f
            val boardSize = size.width.toInt()

            // 1. Draw Official Ludo King Board Texture
            drawImage(
                image = boardBitmap,
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(boardSize, boardSize)
            )

            // 2. Draw Official Pawns / Tokens
            for (player in gameState.players) {
                val pawnBitmap = when (player.color) {
                    LudoColor.RED -> redPawn
                    LudoColor.GREEN -> greenPawn
                    LudoColor.BLUE -> bluePawn
                    LudoColor.YELLOW -> yellowPawn
                }

                for (token in player.tokens) {
                    if (token.state != TokenState.FINISHED) {
                        val coord = getTokenCoord(token)
                        val centerX = (coord.col + 0.5f) * cellSize
                        val centerY = (coord.row + 0.5f) * cellSize
                        val isMoveable = gameState.moveableTokenIds.contains(token.id) && player.id == gameState.currentPlayer?.id

                        // Draw Moveable Pulsing Halo
                        if (isMoveable) {
                            drawCircle(
                                color = Color(0xFFFFD700).copy(alpha = 0.65f),
                                radius = cellSize * 0.65f * pulseAnim.value,
                                center = Offset(centerX, centerY)
                            )
                        }

                        // Draw official pawn sprite
                        val pw = (cellSize * 0.95f).toInt()
                        val ph = (cellSize * 1.35f).toInt()
                        drawImage(
                            image = pawnBitmap,
                            dstOffset = IntOffset(
                                (centerX - pw / 2).toInt(),
                                (centerY - ph / 2 - 4).toInt()
                            ),
                            dstSize = IntSize(pw, ph)
                        )
                    }
                }
            }
        }
    }
}

private fun getTokenCoord(token: Token): GridCoord {
    return when (token.state) {
        TokenState.IN_BASE -> {
            val baseCoords = LudoBoardMath.getBaseYardCoords(token.color)
            baseCoords[token.id % baseCoords.size]
        }
        TokenState.ON_TRACK -> {
            val idx = (token.trackIndex.coerceIn(0, 51))
            LudoBoardMath.trackCoords[idx]
        }
        TokenState.IN_HOME_STRETCH -> {
            val homeCoords = LudoBoardMath.getHomeStretchCoords(token.color)
            val hIdx = token.homeIndex.coerceIn(0, homeCoords.lastIndex)
            homeCoords[hIdx]
        }
        TokenState.FINISHED -> {
            val homeCoords = LudoBoardMath.getHomeStretchCoords(token.color)
            homeCoords.last()
        }
    }
}
