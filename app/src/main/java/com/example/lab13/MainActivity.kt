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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
                    GamePrototypeExercise(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Estados del Videojuego
enum class GameState {
    Menu, Battle, GameOver
}

enum class HeroMode {
    Normal, Super
}

// Ejercicio Final: Prototipo de Videojuego con Animaciones Combinadas
@Composable
fun GamePrototypeExercise(modifier: Modifier = Modifier) {
    var gameState by remember { mutableStateOf(GameState.Menu) }
    var isAttacking by remember { mutableStateOf(false) }
    var heroMode by remember { mutableStateOf(HeroMode.Normal) }
    var showSpecialBanner by remember { mutableStateOf(false) }
    var monsterHp by remember { mutableIntStateOf(100) }

    // 1. animateColorAsState para el Aura del Héroe
    val heroAuraColor by animateColorAsState(
        targetValue = when (heroMode) {
            HeroMode.Normal -> Color(0xFF2196F3) // Azul
            HeroMode.Super -> Color(0xFFFFD700)  // Dorado / Super
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "heroAuraColor"
    )

    // 1. animateColorAsState para la barra de vida del Monstruo
    val monsterHpColor by animateColorAsState(
        targetValue = when {
            monsterHp > 50 -> Color(0xFF4CAF50) // Verde
            monsterHp > 20 -> Color(0xFFFF9800) // Naranja
            else -> Color(0xFFF44336)            // Rojo peligro
        },
        animationSpec = tween(500),
        label = "monsterHpColor"
    )

    // 2. animateDpAsState para Movimiento y Tamaño del Héroe (al atacar o potenciar)
    val heroOffsetX by animateDpAsState(
        targetValue = if (isAttacking) 70.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "heroOffsetX"
    )

    val heroOffsetY by animateDpAsState(
        targetValue = if (isAttacking) (-20).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "heroOffsetY"
    )

    val heroSize by animateDpAsState(
        targetValue = if (isAttacking) 105.dp else (if (heroMode == HeroMode.Super) 95.dp else 80.dp),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heroSize"
    )

    val monsterHpWidth by animateDpAsState(
        targetValue = (monsterHp * 2).dp,
        animationSpec = tween(400),
        label = "monsterHpWidth"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Título del juego
        Text(
            text = "⚔️ HERO COMBAT PROTOTYPE ⚔️",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // 3. AnimatedContent para cambiar entre Pantalla de Menú, Batalla y Fin de Juego
        AnimatedContent(
            targetState = gameState,
            transitionSpec = {
                fadeIn(tween(600)) togetherWith fadeOut(tween(600))
            },
            modifier = Modifier.weight(1f),
            label = "GameStateTransition"
        ) { state ->
            when (state) {
                GameState.Menu -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🎮 MENÚ PRINCIPAL",
                            color = Color(0xFFFFD700),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                monsterHp = 100
                                heroMode = HeroMode.Normal
                                showSpecialBanner = false
                                gameState = GameState.Battle
                            }
                        ) {
                            Text("¡INICIAR BATALLA!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                GameState.Battle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        // Barra de Vida del Monstruo (Animada con animateColorAsState y animateDpAsState)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "👹 JEFE MONSTRUO (HP: $monsterHp%)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(200.dp, 16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.DarkGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(monsterHpWidth, 16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(monsterHpColor)
                                )
                            }
                        }

                        // 4. AnimatedVisibility: Banner de Super Modo
                        AnimatedVisibility(
                            visible = showSpecialBanner,
                            enter = fadeIn(tween(400)) + slideInVertically(initialOffsetY = { -it }),
                            exit = fadeOut(tween(400))
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFD700))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "🔥 ¡ MODO SUPER ACTIVADO ! 🔥",
                                    color = Color.Black,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Arena de Batalla (Héroe vs Monstruo)
                        Row(
                            modifier = Modifier
                                .fillMaxSize(0.85f)
                                .height(160.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Héroe (con offset, tamaño y color de aura animados)
                            Box(
                                modifier = Modifier
                                    .offset(x = heroOffsetX, y = heroOffsetY)
                                    .size(heroSize)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(heroAuraColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (heroMode == HeroMode.Super) "⚡ HERO" else "🛡️ HERO",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            // Monstruo
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFD32F2F)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "👹 MONSTER",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Botones de acción del jugador
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = {
                                    isAttacking = true
                                    val damage = if (heroMode == HeroMode.Super) 30 else 15
                                    monsterHp = (monsterHp - damage).coerceAtLeast(0)
                                    if (monsterHp <= 0) {
                                        gameState = GameState.GameOver
                                    }
                                }
                            ) {
                                Text("🗡️ ATACAR")
                            }

                            Button(
                                onClick = {
                                    heroMode = if (heroMode == HeroMode.Normal) HeroMode.Super else HeroMode.Normal
                                    showSpecialBanner = (heroMode == HeroMode.Super)
                                }
                            ) {
                                Text(if (heroMode == HeroMode.Normal) "⚡ SUPER MODO" else "🛡️ NORMAL")
                            }
                        }
                    }
                }

                GameState.GameOver -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🏆 ¡VICTORIA! 🏆",
                            color = Color(0xFF4CAF50),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "¡Has derrotado al Monstruo!",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { gameState = GameState.Menu }
                        ) {
                            Text("VOLVER AL MENÚ")
                        }
                    }
                }
            }
        }
    }

    // Regresa al héroe a su posición inicial tras atacar
    LaunchedEffect(isAttacking) {
        if (isAttacking) {
            kotlinx.coroutines.delay(300)
            isAttacking = false
        }
    }
}

// Ejercicio 4: AnimatedContent
enum class ScreenState {
    Loading, Content, Error
}

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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

// Previews
@Preview(showBackground = true)
@Composable
fun GamePrototypeExercisePreview() {
    Lab13Theme {
        GamePrototypeExercise()
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedContentExercisePreview() {
    Lab13Theme {
        AnimatedContentExercise()
    }
}