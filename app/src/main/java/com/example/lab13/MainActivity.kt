package com.example.lab13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab13.ui.theme.Lab13Theme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

// Colores del Codelab
private val Seashell = Color(0xFFFFF0EC)
private val GreenLight = Color(0xFFEFF7EE)
private val PaleDogwood = Color(0xFFF2D1C9)
private val Green = Color(0xFF81C784)

enum class TabPage {
    Home, Work
}

data class Topic(val title: String, val body: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimationCodelabHome(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimationCodelabHome(modifier: Modifier = Modifier) {
    var tabPage by remember { mutableStateOf(TabPage.Home) }
    var editMessageShown by remember { mutableStateOf(false) }
    var isWeatherLoading by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val isFabExtended by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }

    val coroutineScope = rememberCoroutineScope()

    val tasks = remember {
        mutableStateListOf(
            "Buy milk",
            "Choose curtain",
            "Plant rosemary",
            "Finish the essay",
            "Receive new packages",
            "Take a photo"
        )
    }

    val topics = remember {
        listOf(
            Topic("2 new packages arrived", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
            Topic("DIY project recommendation", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident."),
            Topic("Festival next month", "Sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
            Topic("New flower seeds available", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium.")
        )
    }

    // TODO 1: animateColorAsState para animar el cambio de color de fondo entre pestañas
    val backgroundColor by animateColorAsState(
        targetValue = if (tabPage == TabPage.Home) Seashell else GreenLight,
        label = "background color"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            // TODO 2-1: AnimatedVisibility para expandir/contraer el texto del FAB
            FloatingActionButton(
                onClick = {
                    editMessageShown = true
                    coroutineScope.launch {
                        delay(3000)
                        editMessageShown = false
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    AnimatedVisibility(visible = isFabExtended) {
                        Text(
                            text = "EDIT",
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Barra de Pestañas con indicador animado (TODO 4)
                HomeTabBar(
                    tabPage = tabPage,
                    onTabSelected = { tabPage = it }
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Sección Clima (Weather)
                    item {
                        WeatherSection(
                            isLoading = isWeatherLoading,
                            onRefresh = {
                                isWeatherLoading = true
                                coroutineScope.launch {
                                    delay(2500)
                                    isWeatherLoading = false
                                }
                            }
                        )
                    }

                    // Sección Temas (Topics)
                    item {
                        Text(
                            text = "Topics",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(topics) { topic ->
                        TopicRow(topic = topic)
                    }

                    // Sección Tareas (Tasks con SwipeToDismiss)
                    item {
                        Text(
                            text = "Tasks",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(tasks, key = { it }) { task ->
                        TaskRow(
                            task = task,
                            onDismissed = { tasks.remove(task) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // TODO 2-2: AnimatedVisibility con deslizado superior para EditMessage
            AnimatedVisibility(
                visible = editMessageShown,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                ),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFCC80),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Edit feature is not supported",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// TODO 4: HomeTabBar usando updateTransition para animar múltiples valores del indicador
@Composable
fun HomeTabBar(
    tabPage: TabPage,
    onTabSelected: (TabPage) -> Unit
) {
    TabRow(
        selectedTabIndex = tabPage.ordinal,
        indicator = { tabPositions ->
            val transition = updateTransition(tabPage, label = "Tab indicator")

            val indicatorLeft by transition.animateDp(
                transitionSpec = {
                    if (TabPage.Home isTransitioningTo TabPage.Work) {
                        spring(stiffness = Spring.StiffnessVeryLow)
                    } else {
                        spring(stiffness = Spring.StiffnessMedium)
                    }
                },
                label = "Indicator left"
            ) { page ->
                tabPositions[page.ordinal].left
            }

            val indicatorRight by transition.animateDp(
                transitionSpec = {
                    if (TabPage.Home isTransitioningTo TabPage.Work) {
                        spring(stiffness = Spring.StiffnessMedium)
                    } else {
                        spring(stiffness = Spring.StiffnessVeryLow)
                    }
                },
                label = "Indicator right"
            ) { page ->
                tabPositions[page.ordinal].right
            }

            val color by transition.animateColor(
                label = "Border color"
            ) { page ->
                if (page == TabPage.Home) PaleDogwood else Green
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapTabIndicator(indicatorLeft, indicatorRight)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
            )
        },
        containerColor = Color.White
    ) {
        Tab(
            selected = tabPage == TabPage.Home,
            onClick = { onTabSelected(TabPage.Home) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Home")
                }
            }
        )
        Tab(
            selected = tabPage == TabPage.Work,
            onClick = { onTabSelected(TabPage.Work) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Work")
                }
            }
        )
    }
}

private fun Modifier.wrapTabIndicator(
    left: androidx.compose.ui.unit.Dp,
    right: androidx.compose.ui.unit.Dp
): Modifier = this.then(
    Modifier.offset(x = left).width(right - left)
)

// Sección del Clima (Weather) con TODO 5 (rememberInfiniteTransition)
@Composable
fun WeatherSection(
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weather",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                // TODO 5: LoadingRow animado indefinidamente
                LoadingRow()
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFB74D))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "18 °C",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// TODO 5: rememberInfiniteTransition para la animación en bucle del skeleton loader
@Composable
fun LoadingRow() {
    val infiniteTransition = rememberInfiniteTransition(label = "Loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
                0.9f at 800
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = alpha))
        )
    }
}

// TODO 3: TopicRow utilizando animateContentSize para expansión suave
@Composable
fun TopicRow(topic: Topic) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        // TODO 3: animateContentSize
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = topic.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = topic.body,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Elemento de tarea con el modificador swipeToDismiss (TODO 6-1 a 6-7)
@Composable
fun TaskRow(
    task: String,
    onDismissed: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .swipeToDismiss(onDismissed = onDismissed),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = task,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// TODO 6-1 a 6-7: Modificador personalizado swipeToDismiss con Animatable
private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // TODO 6-1: Animatable stores the horizontal offset
    val offsetX = remember { Animatable(0f) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                // TODO 6-2: Wait for touch down and interrupt ongoing animation
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetX.stop()

                val velocityTracker = VelocityTracker()

                // TODO 6-3: Handle drag events and update offsetX via snapTo
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }

                // TODO 6-4: Calculate velocity and target offset
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                // TODO 6-5: Set upper and lower bounds
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )

                // TODO 6-6: Animate back to default position or slide away and dismiss
                launch {
                    if (targetOffsetX.absoluteValue <= size.width.toFloat()) {
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
    // TODO 6-7: Apply offset modifier
    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

@Preview(showBackground = true)
@Composable
fun AnimationCodelabHomePreview() {
    Lab13Theme {
        AnimationCodelabHome()
    }
}