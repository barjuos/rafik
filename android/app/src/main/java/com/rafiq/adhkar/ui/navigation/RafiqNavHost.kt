package com.rafiq.adhkar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafiq.adhkar.data.model.RafiqCategories
import com.rafiq.adhkar.ui.MainViewModel
import com.rafiq.adhkar.ui.adhkar.MyAdhkarScreen
import com.rafiq.adhkar.ui.home.HomeScreen
import com.rafiq.adhkar.ui.reader.SurahReaderScreen
import com.rafiq.adhkar.ui.tracker.TrackerScreen

@Composable
fun RafiqNavHost(
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory),
) {
    val navController = rememberNavController()
    val adhkar by viewModel.adhkar.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val theme by viewModel.theme.collectAsState()
    val fontSize by viewModel.readerFontSize.collectAsState()
    val custom by viewModel.customAdhkar.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(
                isLoading = isLoading,
                categories = RafiqCategories,
                theme = theme,
                onThemeChange = viewModel::setTheme,
                onCategoryClick = { category ->
                    when {
                        category.id == "my-adhkar" -> navController.navigate("my-adhkar")
                        category.id.startsWith("surah-") -> navController.navigate("reader/${category.id}")
                        else -> navController.navigate("category/${category.id}")
                    }
                },
            )
        }

        composable("category/{category}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("category") ?: return@composable
            val category = RafiqCategories.firstOrNull { it.id == categoryId }
            val progress by remember(categoryId) {
                viewModel.getTrackerProgress(categoryId)
            }.collectAsState(initial = null)
            TrackerScreen(
                title = category?.detailTitle ?: "",
                adhkar = adhkar[categoryId].orEmpty(),
                theme = theme,
                onThemeChange = viewModel::setTheme,
                onBack = { navController.popBackStack() },
                savedProgress = progress,
                onSaveProgress = { index, count ->
                    viewModel.saveTrackerProgress(categoryId, index, count)
                },
                onResetProgress = { viewModel.clearTrackerProgress(categoryId) },
            )
        }

        composable("reader/{category}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("category") ?: return@composable
            val category = RafiqCategories.firstOrNull { it.id == categoryId }
            val surahContent = adhkar[categoryId]?.firstOrNull()?.content ?: ""
            val bookmark by remember(categoryId) {
                viewModel.getBookmark(categoryId)
            }.collectAsState(initial = null)
            val readingPosition by remember(categoryId) {
                viewModel.getReadingPosition(categoryId)
            }.collectAsState(initial = null)
            SurahReaderScreen(
                title = category?.detailTitle ?: "",
                content = surahContent,
                theme = theme,
                initialBookmark = bookmark,
                initialReadingPosition = readingPosition,
                fontSize = fontSize,
                onThemeChange = viewModel::setTheme,
                onSaveBookmark = { viewModel.saveBookmark(categoryId, it) },
                onSaveReadingPosition = { viewModel.saveReadingPosition(categoryId, it) },
                onFontSizeChange = viewModel::setReaderFontSize,
                onBack = { navController.popBackStack() },
            )
        }

        composable("my-adhkar") {
            val progress by remember {
                viewModel.getTrackerProgress("my-adhkar")
            }.collectAsState(initial = null)
            MyAdhkarScreen(
                custom = custom,
                savedProgress = progress,
                onSaveProgress = { index, count ->
                    viewModel.saveTrackerProgress("my-adhkar", index, count)
                },
                onResetProgress = { viewModel.clearTrackerProgress("my-adhkar") },
                theme = theme,
                onThemeChange = viewModel::setTheme,
                onAdd = viewModel::addCustom,
                onDelete = viewModel::deleteCustom,
                onBack = { navController.popBackStack() },
            )
        }
    }
}