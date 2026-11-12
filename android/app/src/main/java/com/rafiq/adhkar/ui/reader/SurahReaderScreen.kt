package com.rafiq.adhkar.ui.reader

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafiq.adhkar.ui.components.ThemeMenu
import kotlinx.coroutines.launch

private val BookmarkHighlight = Color(0xFFFFD54F).copy(alpha = 0.45f)

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
    /** Saved verse index, or null while the saved value is still loading. */
    initialBookmark: Int?,
    onThemeChange: (String) -> Unit,
    onSaveBookmark: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val verses = remember(content) { splitVerses(content) }
    LaunchedEffect(verses.size) {
        android.util.Log.d("SurahReader", "$title -> ${verses.size} verses, bookmark=$initialBookmark")
    }
    var selectedBookmark by remember { mutableIntStateOf(initialBookmark ?: -1) }
    var appliedInitialBookmark by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Jump to the saved verse once, when the stored value first arrives.
    LaunchedEffect(initialBookmark) {
        if (!appliedInitialBookmark && initialBookmark != null) {
            appliedInitialBookmark = true
            selectedBookmark = initialBookmark
            if (initialBookmark in verses.indices) {
                listState.scrollToItem(initialBookmark)
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
                actions = {
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
                            fontSize = 24.sp,
                            lineHeight = 44.sp,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Justify,
                    )
                }
            }
        }
    }
}
