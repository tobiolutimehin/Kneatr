package com.hollowvyn.kneatr.data.local.entity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.startPhoneCall(phone: String) {
    try {
        val intent =
            Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
        startActivity(intent)
    } catch (t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this, "Could not open dialer.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.startTextMessage(phoneNumber: String) {
    try {
        val intent =
            Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("sms", phoneNumber, null),
            ).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
        startActivity(intent)
    } catch (t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this, "Could not open messaging app.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.startEmail(emailAddress: String) {
    try {
        val intent =
            Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null)).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
        startActivity(intent)
    } catch (t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this, "Could not open email app.", Toast.LENGTH_SHORT).show()
    }
}
