package com.example.lab13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab13.ui.theme.Lab13Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedContentExercise(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// 1. Definición de los tres estados UI
enum class ScreenState {
    Loading, Content, Error
}

// Ejercicio 4: Cambio de Contenido con AnimatedContent
@Composable
fun AnimatedContentExercise(modifier: Modifier = Modifier) {
    var currentState by remember { mutableStateOf(ScreenState.Loading) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 4: AnimatedContent",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones para alternar entre los tres estados
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { currentState = ScreenState.Loading }) {
                Text("Cargando", fontSize = 12.sp)
            }
            Button(onClick = { currentState = ScreenState.Content }) {
                Text("Contenido", fontSize = 12.sp)
            }
            Button(onClick = { currentState = ScreenState.Error }) {
                Text("Error", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2 y 3. AnimatedContent con transición personalizada (fadeIn/fadeOut con tiempos)
        AnimatedContent(
            targetState = currentState,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 700)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 700))
            },
            label = "StateTransition"
        ) { targetState ->
            Box(
                modifier = Modifier
                    .size(240.dp, 160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (targetState) {
                            ScreenState.Loading -> Color(0xFFFFF3E0)
                            ScreenState.Content -> Color(0xFFE8F5E9)
                            ScreenState.Error -> Color(0xFFFFEBEE)
                        }
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (targetState) {
                    ScreenState.Loading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color(0xFFFB8C00))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Cargando datos...",
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    ScreenState.Content -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "¡Carga Exitosa!",
                                fontSize = 18.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Se mostró el contenido correctamente.",
                                color = Color(0xFF1B5E20),
                                fontSize = 12.sp
                            )
                        }
                    }
                    ScreenState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error de Conexión",
                                fontSize = 18.sp,
                                color = Color(0xFFC62828),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No se pudo cargar la información.",
                                color = Color(0xFFB71C1C),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// Ejercicio 3: Animación de Tamaño y Posición
@Composable
fun AnimateSizeAndPositionExercise(modifier: Modifier = Modifier) {
    var isMovedAndExpanded by remember { mutableStateOf(false) }

    val boxSize by animateDpAsState(
        targetValue = if (isMovedAndExpanded) 200.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "boxSize"
    )

    val offsetX by animateDpAsState(
        targetValue = if (isMovedAndExpanded) 60.dp else 0.dp,
        animationSpec = tween(durationMillis = 600),
        label = "offsetX"
    )

    val offsetY by animateDpAsState(
        targetValue = if (isMovedAndExpanded) 40.dp else 0.dp,
        animationSpec = tween(durationMillis = 600),
        label = "offsetY"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 3: Tamaño y Posición",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { isMovedAndExpanded = !isMovedAndExpanded }) {
            Text(text = if (isMovedAndExpanded) "Restablecer" else "Mover y Agrandar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY)
                    .size(boxSize)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isMovedAndExpanded) "Grande" else "Pequeño",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Ejercicio 2: animateColorAsState
@Composable
fun AnimateColorExercise(modifier: Modifier = Modifier) {
    var isBlue by remember { mutableStateOf(true) }

    val animatedColor by animateColorAsState(
        targetValue = if (isBlue) Color(0xFF2196F3) else Color(0xFF4CAF50),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ColorAnimation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 2: animateColorAsState",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { isBlue = !isBlue }) {
            Text(text = if (isBlue) "Cambiar a Verde" else "Cambiar a Azul")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(animatedColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isBlue) "Azul" else "Verde",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Ejercicio 1: AnimatedVisibility
@Composable
fun AnimatedVisibilityExercise(modifier: Modifier = Modifier) {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 1: AnimatedVisibility",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { isVisible = !isVisible }) {
            Text(text = if (isVisible) "Ocultar Cuadro" else "Mostrar Cuadro")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                    slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut(animationSpec = tween(durationMillis = 600)) +
                    slideOutVertically(targetOffsetY = { -it / 2 })
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Hola!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedContentExercisePreview() {
    Lab13Theme {
        AnimatedContentExercise()
    }
}

@Preview(showBackground = true)
@Composable
fun AnimateSizeAndPositionExercisePreview() {
    Lab13Theme {
        AnimateSizeAndPositionExercise()
    }
}

@Preview(showBackground = true)
@Composable
fun AnimateColorExercisePreview() {
    Lab13Theme {
        AnimateColorExercise()
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedVisibilityExercisePreview() {
    Lab13Theme {
        AnimatedVisibilityExercise()
    }
}