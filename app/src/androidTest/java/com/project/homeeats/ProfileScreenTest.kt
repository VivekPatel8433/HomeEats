package com.project.homeeats

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.project.homeeats.pages.theme.customer.ProfileScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

/**
 * Instrumentation tests for ProfileScreen composable.
 */
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_displaysUserName() {
        composeTestRule.setContent {
            ProfileScreen(userName = "Jane Smith", userRole = "Customer")
        }
        composeTestRule.onNodeWithText("Jane Smith").assertIsDisplayed()
    }

    @Test
    fun profileScreen_displaysLogOutOption() {
        composeTestRule.setContent {
            ProfileScreen(userName = "Jane Smith", userRole = "Customer")
        }
        composeTestRule.onNodeWithText("Log Out").assertIsDisplayed()
    }

    @Test
    fun profileScreen_logOutTriggersCallback() {
        var loggedOut = false
        composeTestRule.setContent {
            ProfileScreen(
                userName = "Jane Smith",
                userRole = "Customer",
                onLogOut = { loggedOut = true }
            )
        }
        composeTestRule.onNodeWithText("Log Out").performClick()
        assertTrue(loggedOut)
    }
}
