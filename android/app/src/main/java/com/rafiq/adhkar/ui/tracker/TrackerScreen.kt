package com.rafiq.adhkar.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafiq.adhkar.data.model.Dhikr
import com.rafiq.adhkar.ui.components.CountRing
import com.rafiq.adhkar.ui.components.ThemeMenu
import com.rafiq.adhkar.util.Haptics
import com.rafiq.adhkar.util.SoundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TrackerStateSaver = listSaver<TrackerState, Any>(
    save = { listOf(it.index, it.count, it.finished) },
    restore = { TrackerState(it[0] as Int, it[1] as Int, it[2] as Boolean) },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    title: String,
    adhkar: List<Dhikr>,
    theme: String,
    onThemeChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    var state by rememberSaveable(title, stateSaver = TrackerStateSaver) {
        mutableStateOf(TrackerState(index = 0))
    }
    var advancing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val soundPlayer = remember { SoundPlayer(context) }
    val haptics = remember { Haptics(context) }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose { soundPlayer.release() }
    }

    val currentDhikr = adhkar.getOrNull(state.index)

    val onTap: () -> Unit = {
        val dhikr = adhkar.getOrNull(state.index)
        if (dhikr != null && !state.finished && !advancing) {
            when (val outcome = TrackerLogic.tap(state, adhkar.size, dhikr.count)) {
                is TapOutcome.Counting -> state = outcome.state
                is TapOutcome.DhikrDone -> {
                    state = outcome.state
                    advancing = true
                    soundPlayer.playTick()
                    haptics.tick()
                    scope.launch {
                        delay(250)
                        state = TrackerLogic.advance(state, adhkar.size)
                        advancing = false
                    }
                }
                is TapOutcome.AllDone -> {
                    state = outcome.state
                    soundPlayer.playCompletion()
                    haptics.complete()
                }
                TapOutcome.Ignored -> Unit
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                actions = { ThemeMenu(theme, onThemeChange) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.finished -> CompletionContent(title = title)
                currentDhikr == null -> EmptyContent()
                else -> Column(modifier = Modifier.fillMaxSize()) {
                    ProgressHeader(
                        title = title,
                        current = state.index + 1,
                        total = adhkar.size,
                        progress = state.index.toFloat() / adhkar.size,
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .testTag("tracker-tap")
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onTap,
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TrackerCard(dhikr = currentDhikr, count = state.count)
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressHeader(
    title: String,
    current: Int,
    total: Int,
    progress: Float,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = "$title - $current من $total",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
        )
    }
}

@Composable
private fun TrackerCard(
    dhikr: Dhikr,
    count: Int,
) {
    Card(
        modifier = Modifier
            .widthIn(max = 640.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            CountRing(count = count, target = dhikr.count)
            Spacer(Modifier.height(20.dp))
            Text(
                text = dhikr.content,
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 26.sp,
                    lineHeight = 46.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            if (dhikr.description.isNotBlank() || dhikr.reference.isNotBlank()) {
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.65f))
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (dhikr.description.isNotBlank()) {
                        Text(
                            text = dhikr.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                    if (dhikr.reference.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = dhikr.reference,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            } else {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CompletionContent(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "تقبل الله",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "لقد أتممت $title.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "لا يوجد أذكار",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "لم يتم العثور على أذكار في هذه القائمة.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
