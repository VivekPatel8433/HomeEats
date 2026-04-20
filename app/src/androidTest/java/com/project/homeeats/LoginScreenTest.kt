package com.project.homeeats

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.project.homeeats.pages.theme.auth.LoginScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

/**
 * Instrumentation tests for LoginScreen composable.
 * These tests run on a real device / emulator and verify that
 * the UI renders correctly and responds to user interaction.
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysBrandName() {
        composeTestRule.setContent { LoginScreen() }
        composeTestRule.onNodeWithText("HomeEats").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysEmailLabel() {
        composeTestRule.setContent { LoginScreen() }
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysPasswordLabel() {
        composeTestRule.setContent { LoginScreen() }
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
    }

    @Test
    fun loginScreen_registerLinkTriggersCallback() {
        var registerClicked = false
        composeTestRule.setContent {
            LoginScreen(onRegisterClick = { registerClicked = true })
        }
        // The annotated string reads "Don't have an account? Register"
        composeTestRule
            .onNodeWithText("Register", substring = true)
            .performClick()
        assertTrue(registerClicked)
    }
}
