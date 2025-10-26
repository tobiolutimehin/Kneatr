package com.hollowvyn.kneatr.domain.util

import android.util.Log
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class LoggerTest {
    @Test
    fun `d sends a DEBUG log message with the correct tag`() {
        val message = "debug message"
        Logger.d(tag = "KneatrApp:LoggerTest", message = message)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.DEBUG, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
    }

    @Test
    fun `e sends an ERROR log message with the correct tag`() {
        val message = "error message"
        val throwable = RuntimeException("test exception")
        Logger.e(tag = "KneatrApp:LoggerTest", message = message, throwable = throwable)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.ERROR, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
        assertEquals(throwable, latestLog.throwable)
    }

    @Test
    fun `i sends an INFO log message with the correct tag`() {
        val message = "info message"
        Logger.i(tag = "KneatrApp:LoggerTest", message = message)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.INFO, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
    }

    @Test
    fun `w sends a WARN log message with the correct tag`() {
        val message = "warn message"
        Logger.w(tag = "KneatrApp:LoggerTest", message = message)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.WARN, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
    }

    @Test
    fun `v sends a VERBOSE log message with the correct tag`() {
        val message = "verbose message"
        Logger.v(tag = "KneatrApp:LoggerTest", message = message)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.VERBOSE, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
    }

    @Test
    fun `wtf sends an WTF log message with the correct tag`() {
        val message = "error message"
        val throwable = RuntimeException("test exception")
        Logger.wtf(tag = "KneatrApp:LoggerTest", message = message, throwable = throwable)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals(Log.ASSERT, latestLog.type)
        assertEquals("KneatrApp:LoggerTest", latestLog.tag)
        assertEquals(message, latestLog.msg)
        assertEquals(throwable, latestLog.throwable)
    }

    @Test
    fun `log without tag defaults to KneatrApp tag`() {
        val message = "error message"
        val throwable = RuntimeException("test exception")
        Logger.wtf(message = message, throwable = throwable)

        val latestLog = ShadowLog.getLogs().last()
        assertEquals("KneatrApp", latestLog.tag)
    }
}
