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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoking.game.model.LudoColor

@Composable
fun PlayerSetupScreen(
    onPlayClicked: (playerCount: Int, oneTokenOut: Boolean, p1Name: String, p1Color: LudoColor, p2Name: String, p2Color: LudoColor) -> Unit,
    onBackClicked: () -> Unit
) {
    var playerCount by remember { mutableStateOf(2) }
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    var player1Color by remember { mutableStateOf(LudoColor.RED) }
    var player2Color by remember { mutableStateOf(LudoColor.GREEN) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF071434), Color(0xFF0F3278), Color(0xFF07112B))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Load Saved Games Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C2459))
                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Load Saved Games",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title: CHOOSE COLOR AND NAME
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CHOOSE COLOR AND NAME",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD54F),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "🤖 0/0", color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Player 1 Row
            PlayerConfigRow(
                playerName = player1Name,
                onNameChange = { player1Name = it },
                color = player1Color,
                onColorChange = { player1Color = it },
                isSelected = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Player 2 Row
            PlayerConfigRow(
                playerName = player2Name,
                onNameChange = { player2Name = it },
                color = player2Color,
                onColorChange = { player2Color = it },
                isSelected = false
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Player Count Pills (2P, 3P, 4P, 5P, 6P)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(2, 3, 4, 5, 6).forEach { count ->
                    val isSelected = count == playerCount
                    Box(
                        modifier = Modifier
                            .size(52.dp, 40.dp)
                            .shadow(if (isSelected) 8.dp else 2.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    Brush.verticalGradient(listOf(Color(0xFF1976D2), Color(0xFF0D47A1)))
                                else
                                    Brush.verticalGradient(listOf(Color(0xFF0B1938), Color(0xFF060E21)))
                            )
                            .border(
                                2.dp,
                                if (isSelected) Color(0xFFFFD54F) else Color(0xFF1E88E5).copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { playerCount = count },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${count}P",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // PLAY BUTTON (ld3 -> ld4)
            Button(
                onClick = {
                    onPlayClicked(playerCount, false, player1Name, player1Color, player2Name, player2Color)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(52.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .border(2.5.dp, Color(0xFFFFD54F), RoundedCornerShape(14.dp))
            ) {
                Text(
                    text = "Play",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PLAY - ONE TOKEN OUT - BUTTON
            Button(
                onClick = {
                    onPlayClicked(playerCount, true, player1Name, player1Color, player2Name, player2Color)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(52.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .border(2.dp, Color(0xFF64B5F6), RoundedCornerShape(14.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Play",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "- One Token Out -",
                        color = Color(0xFFFFD54F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Circular Back Button (Bottom Left)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(48.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFFFC107))
                .border(2.dp, Color.White, CircleShape)
                .clickable(onClick = onBackClicked),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "↩", color = Color(0xFF0F1E4A), fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun PlayerConfigRow(
    playerName: String,
    onNameChange: (String) -> Unit,
    color: LudoColor,
    onColorChange: (LudoColor) -> Unit,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0C1E4A).copy(alpha = 0.85f))
            .border(2.dp, Color(0xFF1E88E5).copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF43A047) else Color.Transparent)
                .border(2.dp, if (isSelected) Color.White else Color(0xFF78909C), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text(text = "✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Color Pin (tappable to switch color)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.primaryColor)
                .border(2.dp, Color.White, CircleShape)
                .clickable {
                    val all = LudoColor.values()
                    val nextIdx = (all.indexOf(color) + 1) % all.size
                    onColorChange(all[nextIdx])
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "📍", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Dotted Border Name Field
        OutlinedTextField(
            value = playerName,
            onValueChange = onNameChange,
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFFD54F),
                unfocusedBorderColor = Color(0xFF64B5F6)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Dice Icon
        Text(text = "🎲", fontSize = 24.sp)
    }
}

