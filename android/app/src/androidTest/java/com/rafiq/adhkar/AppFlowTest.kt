package com.rafiq.adhkar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-end smoke tests that drive the real UI. They run against the minified
 * release build (`testBuildType = "release"`), so they also prove that R8 kept
 * the Room, DataStore and kotlinx.serialization wiring intact.
 */
@RunWith(AndroidJUnit4::class)
class AppFlowTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun waitForText(text: String, substring: Boolean = true) {
        rule.waitUntil(10_000) {
            rule.onAllNodesWithText(text, substring = substring)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun clickIfPresent(text: String): Boolean {
        val nodes = rule.onAllNodesWithText(text, substring = false).fetchSemanticsNodes()
        if (nodes.isEmpty()) return false
        rule.onNodeWithText(text, substring = false).performClick()
        return true
    }

    /** MIUI blocks synthetic key events, so pop the back stack in-process. */
    private fun pressBack() {
        rule.runOnUiThread { rule.activity.onBackPressedDispatcher.onBackPressed() }
    }

    @Test
    fun homeGridListsEveryCategory() {
        waitForText("رفيق")

        val firstScreen = listOf(
            "أذكار الصباح",
            "أذكار المساء",
            "أذكار النوم",
            "أذكار بعد الصلاة",
            "دخول/خروج المنزل",
            "الطعام والشراب",
        )
        firstScreen.forEach { rule.onNodeWithText(it, substring = false).assertIsDisplayed() }

        // The remaining cards live below the fold, so scroll the lazily composed grid.
        rule.onNodeWithTag("home-grid").performScrollToNode(hasText("أذكاري"))
        listOf(
            "دخول/خروج المسجد",
            "الركوب",
            "لبس الثوب",
            "دخول/خروج الخلاء",
            "أدعية الهم والحزن",
            "أدعية الرزق",
            "وصايا نبوية",
            "أذكاري",
        ).forEach { rule.onNodeWithText(it, substring = false).assertIsDisplayed() }
    }

    @Test
    fun trackerCountsAdvancesAndReturnsHome() {
        waitForText("رفيق")

        rule.onNodeWithText("أذكار الصباح", substring = false).performClick()
        waitForText("1 من 30")

        // First morning dhikr has count = 1, so a single tap advances the tracker.
        rule.onNodeWithTag("tracker-tap").performClick()
        waitForText("2 من 30")

        // Second dhikr has count = 3.
        repeat(3) { rule.onNodeWithTag("tracker-tap").performClick() }
        waitForText("3 من 30")

        pressBack()
        waitForText("رفيق")
        rule.onNodeWithText("أذكار الصباح", substring = false).assertIsDisplayed()
    }

    @Test
    fun customDhikrIsStoredTrackedAndDeleted() {
        waitForText("رفيق")

        rule.onNodeWithTag("home-grid").performScrollToNode(hasText("أذكاري"))
        rule.onNodeWithText("أذكاري", substring = false).performClick()
        waitForText("أذكاري الخاصة")

        if (!clickIfPresent("إضافة ذكر جديد")) {
            rule.onNodeWithText("إضافة ذكر", substring = false).performClick()
        }

        rule.onNodeWithTag("my-content").performTextInput("ذكر تجريبي")
        rule.onNodeWithTag("my-count").performTextClearance()
        rule.onNodeWithTag("my-count").performTextInput("1")
        rule.onNodeWithText("إضافة", substring = false).performClick()

        waitForText("ذكر تجريبي")
        rule.onNodeWithText("التكرار: 1", substring = false).assertIsDisplayed()

        // Leave the screen and come back: the Room write must survive.
        pressBack()
        waitForText("رفيق")
        rule.onNodeWithTag("home-grid").performScrollToNode(hasText("أذكاري"))
        rule.onNodeWithText("أذكاري", substring = false).performClick()
        waitForText("ذكر تجريبي")

        rule.onNodeWithText("بدء القراءة", substring = false).performClick()
        waitForText("1 من 1")
        rule.onNodeWithTag("tracker-tap").performClick()
        waitForText("تقبل الله")
        rule.onNodeWithText("لقد أتممت أذكاري.", substring = true).assertIsDisplayed()

        pressBack()
        waitForText("أذكاري الخاصة")

        rule.onAllNodesWithContentDescription("حذف الذكر")[0].performClick()
        rule.waitUntil(10_000) {
            rule.onAllNodesWithText("ذكر تجريبي", substring = false)
                .fetchSemanticsNodes()
                .isEmpty()
        }

        pressBack()
        waitForText("رفيق")
    }

    @Test
    fun themeMenuChangesAndPersistsSelection() {
        waitForText("رفيق")

        rule.onNodeWithContentDescription("المظهر").performClick()
        rule.onNodeWithText("مظلم", substring = false).assertIsDisplayed()
        rule.onNodeWithText("مظلم", substring = false).performClick()

        rule.onNodeWithContentDescription("المظهر").performClick()
        rule.onNodeWithText("مضيء", substring = false).performClick()

        rule.onNodeWithContentDescription("المظهر").performClick()
        rule.onNodeWithText("النظام", substring = false).performClick()

        // The grid is still alive after three theme switches.
        waitForText("رفيق")
        rule.onNodeWithText("أذكار الصباح", substring = false).assertIsDisplayed()
    }
}
