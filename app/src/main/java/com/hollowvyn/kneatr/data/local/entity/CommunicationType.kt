package com.hollowvyn.kneatr.data.local.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hollowvyn.kneatr.R

enum class CommunicationType(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    PHONE_CALL(
        label = R.string.phone_call,
        icon = R.drawable.call_24px,
    ),
    MESSAGE(
        label = R.string.message,
        icon = R.drawable.chat_bubble_24px,
    ),
    VIDEO_CALL(
        label = R.string.video_call,
        icon = R.drawable.video_chat_24px,
    ),
    EMAIL(
        label = R.string.email,
        icon = R.drawable.mail_24px,
    ),
    OTHER(
        label = R.string.other,
        icon = R.drawable.other_admission_24px,
    ),
}
