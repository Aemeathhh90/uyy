package com.kakaanime.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF020A16)) {
                    PremiumAniLabSplash {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}

private val SplashBackground = Color(0xFF020A16)
private val ElectricBlue = Color(0xFF48BFFF)
private val IceBlue = Color(0xFFD8F7FF)

@Composable
private fun PremiumAniLabSplash(onFinished: () -> Unit) {
    val ambient = rememberInfiniteTransition(label = "ambient")
    val orbitScale by ambient.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbitScale"
    )
    val shimmer by ambient.animateFloat(
        initialValue = -1.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    LaunchedEffect(Unit) {
        delay(2300)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0A2A49), SplashBackground, Color(0xFF01050C)),
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        StarField(modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .size(330.dp)
                .blur(75.dp)
                .alpha(0.42f)
                .background(Brush.radialGradient(listOf(Color(0xFF087DDB), Color.Transparent)))
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AniLabMark(
                modifier = Modifier
                    .size(250.dp)
                    .graphicsLayer {
                        scaleX = orbitScale
                        scaleY = orbitScale
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "AniLab",
                color = Color.White,
                fontSize = 39.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.2).sp,
                modifier = Modifier.graphicsLayer {
                    alpha = 0.92f
                    translationY = shimmer * 2f
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .width(174.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.13f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .graphicsLayer { translationX = shimmer * 70f }
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, ElectricBlue, IceBlue, ElectricBlue, Color.Transparent)
                            )
                        )
                )
            }
        }
    }
}

@Composable
private fun StarField(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val stars = listOf(
            0.08f to 0.16f, 0.18f to 0.29f, 0.29f to 0.12f, 0.41f to 0.23f,
            0.57f to 0.11f, 0.71f to 0.22f, 0.90f to 0.14f, 0.14f to 0.61f,
            0.32f to 0.78f, 0.69f to 0.68f, 0.84f to 0.51f, 0.93f to 0.77f
        )
        stars.forEachIndexed { index, (x, y) ->
            drawCircle(
                color = Color(0xFF9DE3FF).copy(alpha = 0.35f + (index % 3) * 0.16f),
                radius = if (index % 4 == 0) 2.4f else 1.2f,
                center = Offset(size.width * x, size.height * y)
            )
        }
    }
}

@Composable
private fun AniLabMark(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        drawCircle(Color(0xFF2EABFF).copy(alpha = 0.14f), 88f, Offset(cx, cy))
        drawArc(
            color = Color(0xFF89E2FF),
            startAngle = -164f,
            sweepAngle = 238f,
            useCenter = false,
            topLeft = Offset(cx - 108f, cy - 92f),
            size = Size(216f, 184f),
            style = Stroke(6f)
        )
        drawArc(
            color = Color(0xFF1D74FF).copy(alpha = 0.9f),
            startAngle = 12f,
            sweepAngle = 212f,
            useCenter = false,
            topLeft = Offset(cx - 116f, cy - 105f),
            size = Size(232f, 210f),
            style = Stroke(4f)
        )

        val a = Path().apply {
            moveTo(cx - 77f, cy + 73f)
            lineTo(cx, cy - 82f)
            lineTo(cx + 77f, cy + 73f)
            moveTo(cx - 35f, cy + 6f)
            lineTo(cx + 35f, cy + 6f)
        }
        drawPath(
            path = a,
            brush = Brush.linearGradient(listOf(Color(0xFFE9FCFF), ElectricBlue, Color(0xFF1271E7))),
            style = Stroke(14f)
        )
        drawPath(path = a, color = Color.White.copy(alpha = 0.62f), style = Stroke(3f))

        drawPath(
            path = Path().apply {
                moveTo(cx + 72f, cy - 30f)
                lineTo(cx + 112f, cy + 10f)
                lineTo(cx + 72f, cy + 50f)
                close()
            },
            color = Color(0xFFBFF3FF)
        )

        drawCircle(Color.White, 4f, Offset(cx + 74f, cy - 78f))
        drawLine(Color.White, Offset(cx + 74f, cy - 94f), Offset(cx + 74f, cy - 62f), 2.4f)
        drawLine(Color.White, Offset(cx + 58f, cy - 78f), Offset(cx + 90f, cy - 78f), 2.4f)
        drawCircle(Color(0xFFBFF3FF), 3f, Offset(cx + 108f, cy - 45f))
    }
}
