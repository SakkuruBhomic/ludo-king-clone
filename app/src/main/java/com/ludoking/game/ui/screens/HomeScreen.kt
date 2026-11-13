package com.ludoking.game.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoking.game.R

@Composable
fun HomeScreen(
    onPassNPlayClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val pulseScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        pulseScale.animateTo(
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(600),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F2B68), Color(0xFF0B1938), Color(0xFF060D20))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF09142E).copy(alpha = 0.95f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC107))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF2E7D32))
                        .border(1.dp, Color(0xFF81C784), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PASS & PLAY EXCLUSIVE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF050B1A))
                        .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🪙 99,999",
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Official Logo
            Image(
                painter = painterResource(id = R.drawable.official_logo),
                contentDescription = "Ludo King Logo",
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .border(3.dp, Color(0xFFFFD54F), RoundedCornerShape(26.dp))
                    .shadow(12.dp, RoundedCornerShape(26.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "LUDO KING",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFD54F),
                letterSpacing = 3.sp
            )
            Text(
                text = "OFFLINE PASS 'N PLAY EDITION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF90CAF9),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PASS N PLAY EXCLUSIVE HERO BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(96.dp)
                    .scale(pulseScale.value)
                    .shadow(14.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFF57F17), Color(0xFFFFB300))
                        )
                    )
                    .border(3.dp, Color(0xFFFFD54F), RoundedCornerShape(20.dp))
                    .clickable(onClick = onPassNPlayClicked)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎲", fontSize = 42.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "PASS N PLAY",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFF9C4))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "★ TAP TO PLAY (LOCAL MULTIPLAYER) ★",
                                color = Color(0xFF0B1A42),
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rigged Rule Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFB71C1C))
                    .border(1.5.dp, Color(0xFFFF5252), RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👑 RIGGED RULE: RED IS 100% RIGGED TO WIN EVERY MATCH 👑",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Disconnected online modes as requested
            DisabledModeCard(icon = "🌐", title = "ONLINE MULTIPLAYER (DISCONNECTED)")
            Spacer(modifier = Modifier.height(10.dp))
            DisabledModeCard(icon = "👥", title = "PLAY WITH FRIENDS (OFFLINE ONLY)")

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Powered by Official Ludo King Assets",
                color = Color(0xFF546E7A),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DisabledModeCard(icon: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A183C).copy(alpha = 0.6f))
            .border(1.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            color = Color(0xFF78909C),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
