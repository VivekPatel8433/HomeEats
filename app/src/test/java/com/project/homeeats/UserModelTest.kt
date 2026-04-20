package com.project.homeeats

import com.project.homeeats.data.model.User
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the User data model.
 */
class UserModelTest {

    @Test
    fun defaultUserRole_isCustomer() {
        val user = User()
        assertEquals("customer", user.role)
    }

    @Test
    fun userFields_areSetCorrectly() {
        val user = User(
            uid = "abc123",
            name = "John Doe",
            email = "john@example.com",
            phone = "555-1234",
            role = "chef"
        )
        assertEquals("abc123", user.uid)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
        assertEquals("555-1234", user.phone)
        assertEquals("chef", user.role)
    }

    @Test
    fun defaultUser_hasEmptyStrings() {
        val user = User()
        assertEquals("", user.uid)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("", user.phone)
    }
}
