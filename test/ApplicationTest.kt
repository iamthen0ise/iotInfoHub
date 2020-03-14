package com.github.iamthen0ise

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import com.github.iamthen0ise.DatabaseFactory
import org.junit.After
import org.junit.Before

class ApplicationTest {

    @Before
    fun setup() {
        DatabaseFactory.TESTING = true
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testHealth() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "healthcheck").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

}
