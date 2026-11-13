package com.ludoking.game.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ludoking.game.R

@Composable
fun DiceView(
    value: Int,
    isRolling: Boolean,
    canRoll: Boolean,
    isCurrentTurn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val rotation = remember { Animatable(0f) }
    val arrowOffset = remember { Animatable(0f) }

    LaunchedEffect(isRolling) {
        if (isRolling) {
            rotation.animateTo(
                targetValue = 720f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
            rotation.snapTo(0f)
        }
    }

    LaunchedEffect(isCurrentTurn && canRoll) {
        if (isCurrentTurn && canRoll) {
            arrowOffset.animateTo(
                targetValue = 8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            arrowOffset.snapTo(0f)
        }
    }

    val diceRes = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Active Turn Pointer
        if (isCurrentTurn && canRoll) {
            Box(
                modifier = Modifier
                    .offset(x = (-size / 2 - 12.dp + arrowOffset.value.dp))
                    .size(24.dp)
                    .background(Color(0xFFFFC107), shape = RoundedCornerShape(4.dp))
                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "▶", color = Color(0xFF1A237E), fontSize = 14.sp)
            }
        }

        // Dice Body displaying Official Dice Texture
        Box(
            modifier = Modifier
                .size(size)
                .rotate(rotation.value)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(
                    2.5.dp,
                    if (isCurrentTurn && canRoll) Color(0xFFFFC107) else Color(0xFF90A4AE),
                    RoundedCornerShape(12.dp)
                )
                .clickable(enabled = canRoll && isCurrentTurn, onClick = onClick)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = diceRes),
                contentDescription = "Dice $value",
                modifier = Modifier.size(size * 0.85f)
            )
        }
    }
}
