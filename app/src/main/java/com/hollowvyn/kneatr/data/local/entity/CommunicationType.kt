package com.hollowvyn.kneatr.data.local.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class CommunicationType(
    @StringRes val label: Int? = null,
    @DrawableRes val icon: Int? = null,
) {
    PHONE_CALL,
    VIDEO_CALL,
    EMAIL,
    SOCIAL_MEDIA,
    MESSAGE,
    OTHER,
}
