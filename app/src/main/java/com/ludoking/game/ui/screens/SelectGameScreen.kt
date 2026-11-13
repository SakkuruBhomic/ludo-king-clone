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
import com.ludoking.game.model.GameMode

@Composable
fun SelectGameScreen(
    selectedMode: GameMode,
    selectedTokenIndex: Int,
    onModeSelected: (GameMode) -> Unit,
    onTokenSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF071330), Color(0xFF0F3175), Color(0xFF0A1E4A))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Theme Banner
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0B1938))
                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(12.dp))
                .padding(horizontal = 24.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "SELECT THEME",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SELECT TOKEN Title
            Text(
                text = "SELECT TOKEN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFD54F),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Token Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0A1C44).copy(alpha = 0.8f))
                    .border(2.dp, Color(0xFF1E88E5), RoundedCornerShape(14.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "◀", color = Color(0xFFFFD54F), fontSize = 16.sp)

                TokenOption(icon = "📍", isSelected = selectedTokenIndex == 0, isLocked = false) {
                    onTokenSelected(0)
                }
                TokenOption(icon = "💿", isSelected = selectedTokenIndex == 1, isLocked = false) {
                    onTokenSelected(1)
                }
                TokenOption(icon = "🔨", isSelected = false, isLocked = true) {}
                TokenOption(icon = "🌀", isSelected = false, isLocked = true) {}
                TokenOption(icon = "🥷", isSelected = false, isLocked = true) {}

                Text(text = "▶", color = Color(0xFFFFD54F), fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // SELECT GAME Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SELECT GAME",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD54F),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E88E5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Mode 1: Classic (Matching ld2 checked)
            GameModeCard(
                title = "Classic",
                icon = "👥 VS 👥",
                isSelected = selectedMode == GameMode.CLASSIC,
                onClick = { onModeSelected(GameMode.CLASSIC) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mode 2: Classic Team Up
            GameModeCard(
                title = "Classic Team Up",
                icon = "🛡️ VS ⚔️",
                isSelected = selectedMode == GameMode.CLASSIC_TEAM_UP,
                onClick = { onModeSelected(GameMode.CLASSIC_TEAM_UP) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mode 3: Quick
            GameModeCard(
                title = "QUICK",
                icon = "⚡ 🔄",
                isSelected = selectedMode == GameMode.QUICK,
                onClick = { onModeSelected(GameMode.QUICK) }
            )

            Spacer(modifier = Modifier.height(26.dp))

            // NEXT BUTTON (ld2 -> ld3)
            Button(
                onClick = onNextClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(54.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .border(3.dp, Color(0xFFFFD54F), RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
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
private fun TokenOption(icon: String, isSelected: Boolean, isLocked: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF1976D2) else Color(0xFF0E2254))
            .border(
                1.5.dp,
                if (isSelected) Color(0xFFFFD54F) else Color(0xFF42A5F5).copy(alpha = 0.4f),
                RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isLocked, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 20.sp)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.TopEnd)
                    .background(Color(0xFF43A047), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✓", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        } else if (isLocked) {
            Text(
                text = "🔒",
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun GameModeCard(
    title: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = if (isSelected)
                        listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    else
                        listOf(Color(0xFF091636), Color(0xFF0E2356))
                )
            )
            .border(
                2.5.dp,
                if (isSelected) Color(0xFFFFD54F) else Color(0xFF1E88E5).copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Square
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF05112B)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )

        // Radio Checkmark
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF43A047) else Color.Transparent)
                .border(2.dp, if (isSelected) Color.White else Color(0xFF90A4AE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text(text = "✓", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}

