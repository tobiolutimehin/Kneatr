package com.hollowvyn.kneatr.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.hollowvyn.kneatr.R

private fun Context.safeStartActivity(
    intent: Intent,
    @StringRes toastText: Int,
) {
    try {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    } catch (t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}

fun Context.startPhoneCall(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
    safeStartActivity(intent, R.string.error_could_not_open_dialer)
}

fun Context.startTextMessage(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", phoneNumber, null))
    safeStartActivity(intent, R.string.error_could_not_open_messaging)
}

fun Context.startEmail(emailAddress: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null))
    safeStartActivity(intent, R.string.error_could_not_open_email)
}
