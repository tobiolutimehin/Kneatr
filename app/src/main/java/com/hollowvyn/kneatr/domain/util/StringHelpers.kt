package com.hollowvyn.kneatr.domain.util

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

/**
 * Formats a phone number string into the international format.
 * If parsing fails, it returns the original string.
 */
fun String.formatPhoneNumber(): String {
    if (this.isBlank()) return this
    return try {
        val numberProto = phoneUtil.parse(this, null)
        phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    } catch (e: NumberParseException) {
        e.printStackTrace()
        this
    }
}
