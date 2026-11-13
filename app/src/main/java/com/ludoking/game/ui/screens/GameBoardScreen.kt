package com.ludoking.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoking.game.model.GameState
import com.ludoking.game.model.LudoColor
import com.ludoking.game.model.Player
import com.ludoking.game.ui.components.DiceView
import com.ludoking.game.ui.components.LudoBoardCanvas
import com.ludoking.game.ui.components.WinnerDialog

@Composable
fun GameBoardScreen(
    gameState: GameState,
    onDiceClicked: () -> Unit,
    onTokenClicked: (Int) -> Unit,
    onPlayAgain: () -> Unit,
    onHomeClicked: () -> Unit
) {
    val player1 = gameState.players.getOrNull(0)
    val player2 = gameState.players.getOrNull(1)

    val isP1Turn = gameState.currentPlayer?.id == player1?.id
    val isP2Turn = gameState.currentPlayer?.id == player2?.id

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D2560), Color(0xFF133E8D), Color(0xFF091C4A))
                )
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Top Section: Top Bar & Player 2 Panel (Top Right - Green)
            Column(modifier = Modifier.fillMaxWidth()) {
                // Ad Banner Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF060F26).copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "★ LUDO KING CLASSIC ★",
                        color = Color(0xFFFFD54F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Player 2 Box (Top Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    player2?.let { p2 ->
                        PlayerPanel(
                            player = p2,
                            isCurrentTurn = isP2Turn,
                            diceValue = gameState.diceValue,
                            isRolling = gameState.isRolling && isP2Turn,
                            canRoll = gameState.canRoll && isP2Turn,
                            onDiceClick = onDiceClicked
                        )
                    }
                }
            }

            // 2. Middle Section: The 15x15 Ludo Board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                LudoBoardCanvas(
                    gameState = gameState,
                    onTokenClicked = onTokenClicked
                )
            }

            // 3. Status Bar Notification
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF081434).copy(alpha = 0.9f))
                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(10.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = gameState.statusMessage,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // 4. Bottom Section: Player 1 Panel (Bottom Left - Red) & Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Menu icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0B1938))
                        .border(1.5.dp, Color(0xFFFFD54F), CircleShape)
                        .clickable(onClick = onHomeClicked),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "☰", color = Color(0xFFFFD54F), fontSize = 20.sp)
                }

                // Player 1 Box (Bottom Center / Left)
                player1?.let { p1 ->
                    PlayerPanel(
                        player = p1,
                        isCurrentTurn = isP1Turn,
                        diceValue = gameState.diceValue,
                        isRolling = gameState.isRolling && isP1Turn,
                        canRoll = gameState.canRoll && isP1Turn,
                        onDiceClick = onDiceClicked
                    )
                }

                // No Ads badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD32F2F))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🚫\nADS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        // 5. Winner Victory Celebration Dialog
        gameState.winner?.let { winner ->
            WinnerDialog(
                winner = winner,
                onPlayAgain = onPlayAgain,
                onHome = onHomeClicked
            )
        }
    }
}

@Composable
private fun PlayerPanel(
    player: Player,
    isCurrentTurn: Boolean,
    diceValue: Int,
    isRolling: Boolean,
    canRoll: Boolean,
    onDiceClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    colors = if (isCurrentTurn)
                        listOf(Color(0xFF1976D2), Color(0xFF0D47A1))
                    else
                        listOf(Color(0xFF081636), Color(0xFF0E2356))
                )
            )
            .border(
                2.dp,
                if (isCurrentTurn) Color(0xFFFFD54F) else Color(0xFF42A5F5).copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        // Player Pin Icon
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(player.color.primaryColor)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "📍", fontSize = 16.sp)
        }

        Column {
            Text(
                text = player.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = player.color.displayName,
                color = player.color.primaryColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Dice Roller Box
        DiceView(
            value = diceValue,
            isRolling = isRolling,
            canRoll = canRoll,
            isCurrentTurn = isCurrentTurn,
            onClick = onDiceClick,
            size = 50.dp
        )
    }
}

