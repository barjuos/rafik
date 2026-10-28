package com.rafiq.adhkar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafiq.adhkar.data.model.RafiqCategories
import com.rafiq.adhkar.ui.MainViewModel
import com.rafiq.adhkar.ui.adhkar.MyAdhkarScreen
import com.rafiq.adhkar.ui.home.HomeScreen
import com.rafiq.adhkar.ui.tracker.TrackerScreen

@Composable
fun RafiqNavHost(
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory),
) {
    val navController = rememberNavController()
    val adhkar by viewModel.adhkar.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val theme by viewModel.theme.collectAsState()
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
                    if (category.id == "my-adhkar") {
                        navController.navigate("my-adhkar")
                    } else {
                        navController.navigate("category/${category.id}")
                    }
                },
            )
        }

        composable("category/{category}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("category") ?: return@composable
            val category = RafiqCategories.firstOrNull { it.id == categoryId }
            TrackerScreen(
                title = category?.detailTitle ?: "",
                adhkar = adhkar[categoryId].orEmpty(),
                theme = theme,
                onThemeChange = viewModel::setTheme,
                initialIndex = previewIndex,
                onBack = { navController.popBackStack() },
            )
        }

        composable("my-adhkar") {
            MyAdhkarScreen(
                custom = custom,
                theme = theme,
                onThemeChange = viewModel::setTheme,
                onAdd = viewModel::addCustom,
                onDelete = viewModel::deleteCustom,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
