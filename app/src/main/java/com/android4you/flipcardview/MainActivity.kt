package com.android4you.flipcardview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android4you.flipcardview.ui.theme.FlipCardViewTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isClockWiseVertical by remember { mutableStateOf(false) }
            var isClockWiseHorizontal by remember { mutableStateOf(false) }

            FlipCardViewTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("My App") },
                            navigationIcon = {
                                IconButton(onClick = { /* Handle navigation icon press */ }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        // Your main content here
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            Text(text = "Vertical Flip")

                            FlippableCardVertical(
                                frontContent = { FrontCardContent() },
                                backContent = { BackCardContent() },
                                isClockWiseStatus = isClockWiseVertical
                            )

                            Button(onClick = {
                                if (isClockWiseVertical)
                                    isClockWiseVertical = false
                                else isClockWiseVertical = true
                            }) {
                                Text(text = if (isClockWiseVertical) "ClockWise" else "Anti Clockwise")
                            }
                            Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                            Text(text = "Horizontal Flip")
                            FlippableCardHorizontal(
                                frontContent = { FrontCardContent() },
                                backContent = { BackCardContent() },
                                isClockWiseStatus = isClockWiseHorizontal
                            )

                            Button(onClick = {
                                if (isClockWiseHorizontal)
                                    isClockWiseHorizontal = false
                                else isClockWiseHorizontal = true
                            }) {
                                Text(text = if (isClockWiseHorizontal) "ClockWise" else "Anti Clockwise")
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FlippableCardVertical(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    isClockWiseStatus: Boolean = false,
) {
    var isFlipped by remember { mutableStateOf(false) }
    var flipClockwise = isClockWiseStatus //by remember { mutableStateOf(isClockWiseStatus) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 1600)
    )

    val effectiveRotationY = if (flipClockwise) rotation else -rotation

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                isFlipped = !isFlipped
                flipClockwise = !flipClockwise
            }
            .graphicsLayer {
                rotationY = effectiveRotationY
                cameraDistance = 12f * density
            }
            .background(Color.Gray)
    ) {
        if (rotation <= 90f && rotation >= -90f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                frontContent()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = if (flipClockwise) 180f else -180f
                    }
            ) {
                backContent()
            }
        }
    }
}

@Composable
fun FlippableCardHorizontal(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    isClockWiseStatus: Boolean = false,
) {
    var isFlipped by remember { mutableStateOf(false) }
    var flipClockwise = isClockWiseStatus
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 1600)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                isFlipped = !isFlipped
                flipClockwise = !flipClockwise
            }
            .graphicsLayer {
                rotationX = if (flipClockwise) rotation else -rotation
                cameraDistance = 12f * density
            }
            .background(Color.Gray)
    ) {
        if (rotation <= 90f && rotation >= -90f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                frontContent()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationX = if (flipClockwise) 180f else -180f
                    }
            ) {
                backContent()
            }
        }
    }
}

@Composable
fun FrontCardContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Front",
            style = TextStyle(color = Color.White, fontSize = 24.sp)
        )
    }
}

@Composable
fun BackCardContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Back",
            style = TextStyle(color = Color.White, fontSize = 24.sp)
        )
    }
}