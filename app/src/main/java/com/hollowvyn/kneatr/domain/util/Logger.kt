package com.hollowvyn.kneatr.domain.util

import android.util.Log
import com.hollowvyn.kneatr.domain.util.Logger.TAG_PREFIX

/**
 * A simple wrapper around [android.util.Log] to provide a consistent logging tag.
 *
 * This object standardizes logging across the application by providing a default
 * tag prefix, `KneatrApp`. This makes it easier to filter logs specific to this
 * application in Logcat.
 */
object Logger {
    private const val TAG_PREFIX = "KneatrApp"

    /**
     * Sends a `DEBUG` log message.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     */
    fun d(
        message: String,
        tag: String = TAG_PREFIX,
    ) {
        Log.d(tag, message)
    }

    /**
     * Sends an `ERROR` log message.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     * @param throwable An optional [Throwable] to log.
     */
    fun e(
        message: String,
        tag: String = TAG_PREFIX,
        throwable: Throwable? = null,
    ) {
        Log.e(tag, message, throwable)
    }

    /**
     * Sends an `INFO` log message.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     */
    fun i(
        message: String,
        tag: String = TAG_PREFIX,
    ) {
        Log.i(tag, message)
    }

    /**
     * Sends a `WARN` log message.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     */
    fun w(
        message: String,
        tag: String = TAG_PREFIX,
    ) {
        Log.w(tag, message)
    }

    /**
     * Sends a `VERBOSE` log message.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     */
    fun v(
        message: String,
        tag: String = TAG_PREFIX,
    ) {
        Log.v(tag, message)
    }

    /**
     * Sends a `WTF` (What a Terrible Failure) log message.
     * This is for reporting conditions that should never happen.
     *
     * @param message The message you would like logged.
     * @param tag The log tag to use. Defaults to [TAG_PREFIX].
     * @param throwable An optional [Throwable] to log.
     */
    fun wtf(
        message: String,
        tag: String = TAG_PREFIX,
        throwable: Throwable? = null,
    ) {
        Log.wtf(tag, message, throwable)
    }
}
