package com.rafiq.adhkar.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.Wc
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    /** Label shown on the home grid. */
    val title: String,
    val icon: ImageVector,
    /** Heading inside the tracker: the web app uses a fuller title here. */
    val detailTitle: String = title,
)

/**
 * Home-screen categories, in the same order as the web app's home page.
 */
val RafiqCategories: List<Category> = listOf(
    Category("morning", "أذكار الصباح", Icons.Outlined.WbSunny),
    Category("evening", "أذكار المساء", Icons.Outlined.NightsStay),
    Category("sleep", "أذكار النوم", Icons.Outlined.Bedtime),
    Category("after-prayer", "أذكار بعد الصلاة", Icons.Outlined.TaskAlt),
    Category("home", "دخول/خروج المنزل", Icons.Outlined.Home, "أذكار المنزل"),
    Category("food", "الطعام والشراب", Icons.Outlined.Restaurant, "أذكار الطعام والشراب"),
    Category("mosque", "دخول/خروج المسجد", Icons.Outlined.AccountBalance, "أذكار المسجد"),
    Category("travel", "الركوب", Icons.Outlined.DirectionsCar, "أذكار الركوب"),
    Category("clothing", "لبس الثوب", Icons.Outlined.Checkroom, "أذكار لبس الثوب"),
    Category("toilet", "دخول/خروج الخلاء", Icons.Outlined.Wc, "أذكار الخلاء"),
    Category("relief", "أدعية الهم والحزن", Icons.Outlined.SentimentSatisfied),
    Category("rizq", "أدعية الرزق", Icons.Outlined.Spa),
    Category("advice", "وصايا نبوية", Icons.Outlined.Star),
    Category("my-adhkar", "أذكاري", Icons.AutoMirrored.Outlined.PlaylistAdd),
)
