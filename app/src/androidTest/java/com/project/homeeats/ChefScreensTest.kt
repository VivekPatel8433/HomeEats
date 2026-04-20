package com.project.homeeats

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.project.homeeats.pages.theme.chef.AddDishScreen
import com.project.homeeats.pages.theme.chef.ChefHomeScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

/**
 * Instrumentation tests for ChefHomeScreen and AddDishScreen composables.
 */
@RunWith(AndroidJUnit4::class)
class ChefScreensTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── ChefHomeScreen ─────────────────────────────────────

    @Test
    fun chefHomeScreen_showsEmptyState() {
        composeTestRule.setContent {
            ChefHomeScreen(dishes = emptyList())
        }
        composeTestRule
            .onNodeWithText("No dishes yet", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun chefHomeScreen_showsMyDishesTitle() {
        composeTestRule.setContent {
            ChefHomeScreen(dishes = emptyList())
        }
        composeTestRule.onNodeWithText("My Dishes").assertIsDisplayed()
    }

    // ── AddDishScreen ──────────────────────────────────────

    @Test
    fun addDishScreen_displaysFormTitle() {
        composeTestRule.setContent {
            AddDishScreen()
        }
        composeTestRule.onNodeWithText("Add New Dish").assertIsDisplayed()
    }

    @Test
    fun addDishScreen_displaysSaveButton() {
        composeTestRule.setContent {
            AddDishScreen()
        }
        composeTestRule.onNodeWithText("Save Dish").assertIsDisplayed()
    }

    @Test
    fun addDishScreen_displaysAllFormLabels() {
        composeTestRule.setContent {
            AddDishScreen()
        }
        composeTestRule.onNodeWithText("Dish Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Price ($)", substring = true).assertIsDisplayed()
    }

    @Test
    fun addDishScreen_saveTriggerCallback() {
        var savedName = ""
        composeTestRule.setContent {
            AddDishScreen(
                onSave = { name, _, _, _ -> savedName = name }
            )
        }
        // Type a dish name into the name field
        composeTestRule
            .onNodeWithText("e.g. Butter Chicken")
            .performTextInput("Tacos")

        // Tap Save
        composeTestRule.onNodeWithText("Save Dish").performClick()

        assertEquals("Tacos", savedName)
    }
}
