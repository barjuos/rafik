package com.rafiq.adhkar.ui.reader

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafiq.adhkar.ui.components.ThemeMenu
import kotlinx.coroutines.launch

private val BookmarkHighlight = Color(0xFFFFD54F).copy(alpha = 0.45f)

/** Default reader text size, in sp. */
const val DefaultReaderFontSize = 28

private val ReaderFontSizes = listOf(20, 24, 28, 32, 36, 40, 46)

private val VerseMarker = Regex("\\((?:\\d|[\\u0660-\\u0669])+\\)")

/**
 * Splits the surah text into individual verses. A verse ends at its
 * parenthesised number, e.g. "… (١)" or "… (1)".
 */
private fun splitVerses(content: String): List<String> {
    val text = content.trim()
    if (text.isEmpty()) return emptyList()
    val result = mutableListOf<String>()
    var start = 0
    for (match in VerseMarker.findAll(text)) {
        val end = match.range.last + 1
        val verse = text.substring(start, end).trim()
        if (verse.isNotEmpty()) result.add(verse)
        start = end
    }
    if (start < text.length) {
        val tail = text.substring(start).trim()
        if (tail.isNotEmpty()) result.add(tail)
    }
    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahReaderScreen(
    title: String,
    content: String,
    theme: String,
    /** Manual bookmark (verse index), or null while loading. */
    initialBookmark: Int?,
    /** Last verse the user scrolled to, or null while loading. */
    initialReadingPosition: Int?,
    fontSize: Int,
    onThemeChange: (String) -> Unit,
    onSaveBookmark: (Int) -> Unit,
    onSaveReadingPosition: (Int) -> Unit,
    onFontSizeChange: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val verses = remember(content) { splitVerses(content) }
    var fontMenuOpen by remember { mutableStateOf(false) }
    var selectedBookmark by remember { mutableIntStateOf(initialBookmark ?: -1) }
    var restored by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Jump once to the saved place: the last reading position wins so the user
    // continues where they stopped, otherwise the manual bookmark is used.
    LaunchedEffect(initialBookmark, initialReadingPosition) {
        if (!restored && initialBookmark != null && initialReadingPosition != null) {
            restored = true
            selectedBookmark = initialBookmark
            val target = when {
                initialReadingPosition >= 0 -> initialReadingPosition
                initialBookmark >= 0 -> initialBookmark
                else -> -1
            }
            if (target in verses.indices) {
                listState.scrollToItem(target)
            }
        }
    }

    // Remember the last read verse whenever the user stops scrolling.
    var wasScrolling by remember { mutableStateOf(false) }
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            wasScrolling = true
        } else if (wasScrolling) {
            wasScrolling = false
            if (restored && verses.isNotEmpty()) {
                onSaveReadingPosition(listState.firstVisibleItemIndex.coerceIn(0, verses.lastIndex))
            }
        }
    }

    fun saveCurrentPosition() {
        if (restored && verses.isNotEmpty()) {
            onSaveReadingPosition(listState.firstVisibleItemIndex.coerceIn(0, verses.lastIndex))
        }
    }

    fun restartReading() {
        onSaveReadingPosition(0)
        scope.launch { listState.animateScrollToItem(0) }
        Toast.makeText(context, "تم الرجوع إلى أول السورة", Toast.LENGTH_SHORT).show()
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
                    IconButton(onClick = {
                        saveCurrentPosition()
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { fontMenuOpen = true }) {
                            Icon(
                                imageVector = Icons.Outlined.FormatSize,
                                contentDescription = "حجم الخط",
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        DropdownMenu(
                            expanded = fontMenuOpen,
                            onDismissRequest = { fontMenuOpen = false },
                        ) {
                            ReaderFontSizes.forEach { size ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "أ",
                                            fontSize = size.sp,
                                            fontWeight = if (size == fontSize) FontWeight.Bold else FontWeight.Normal,
                                            color = if (size == fontSize) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurface
                                            },
                                        )
                                    },
                                    trailingIcon = {
                                        if (size == fontSize) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                            )
                                        }
                                    },
                                    onClick = {
                                        onFontSizeChange(size)
                                        fontMenuOpen = false
                                    },
                                )
                            }
                        }
                    }
                    IconButton(onClick = {
                        if (selectedBookmark in verses.indices) {
                            scope.launch {
                                val distance = kotlin.math.abs(listState.firstVisibleItemIndex - selectedBookmark)
                                if (distance > 20) {
                                    listState.scrollToItem(selectedBookmark)
                                } else {
                                    listState.animateScrollToItem(selectedBookmark)
                                }
                            }
                            Toast.makeText(context, "تم الانتقال إلى الآية المعلّمة 🔖", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "اضغط على الآية أولاً لوضع العلامة", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "الانتقال إلى الآية المحددة",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    ThemeMenu(theme, onThemeChange)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = listState,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) {
            itemsIndexed(verses) { index, verse ->
                val isSelected = index == selectedBookmark
                val background by animateColorAsState(
                    targetValue = if (isSelected) BookmarkHighlight else Color.Transparent,
                    label = "verseHighlight",
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(background, RoundedCornerShape(10.dp))
                        .clickable {
                            if (selectedBookmark == index) {
                                selectedBookmark = -1
                                onSaveBookmark(-1)
                                Toast.makeText(context, "تم إزالة العلامة", Toast.LENGTH_SHORT).show()
                            } else {
                                selectedBookmark = index
                                onSaveBookmark(index)
                                Toast.makeText(context, "تم وضع العلامة على هذه الآية 🔖", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                ) {
                    Text(
                        text = verse,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSize.sp,
                            lineHeight = (fontSize * 1.9f).sp,
                            letterSpacing = 0.sp,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Justify,
                    )
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "تمّت السورة",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "يمكنك الرجوع إلى أول السورة لمتابعة القراءة من جديد.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { restartReading() }) {
                            Icon(
                                imageVector = Icons.Filled.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(Modifier.size(8.dp))
                            Text("العودة إلى أول السورة")
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}