package com.project.homeeats

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.project.homeeats.pages.theme.auth.RegisterScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

/**
 * Instrumentation tests for RegisterScreen composable.
 */
@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysTitle() {
        composeTestRule.setContent { RegisterScreen() }
        // "Create Account" appears as both title and button text,
        // so we assert that at least one instance is displayed
        composeTestRule
            .onAllNodesWithText("Create Account")
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun registerScreen_displaysAllInputLabels() {
        composeTestRule.setContent { RegisterScreen() }
        composeTestRule.onNodeWithText("Full Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone Number").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
    }

    @Test
    fun registerScreen_displaysRoleToggle() {
        composeTestRule.setContent { RegisterScreen() }
        composeTestRule.onNodeWithText("Customer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chef").assertIsDisplayed()
    }
}
