package com.hollowvyn.kneatr.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.model.RelativeDate
import com.hollowvyn.kneatr.domain.util.DateTimeUtils

@Composable
fun RelativeDate.getRelativeDateString(): String =
    when (this) {
        RelativeDate.Today -> stringResource(R.string.today)
        RelativeDate.Yesterday -> stringResource(R.string.yesterday)
        is RelativeDate.LastWeekday -> {
            val weekday = dayOfWeek.name.lowercase().replaceFirstChar { it.titlecase() }
            stringResource(R.string.last_x_week, weekday)
        }

        is RelativeDate.WeeksAgo -> {
            pluralStringResource(R.plurals.x_weeks_ago, count, count)
        }

        is RelativeDate.NextWeekday -> {
            val weekday = dayOfWeek.name.lowercase().replaceFirstChar { it.titlecase() }
            stringResource(R.string.next_x_week, weekday)
        }

        RelativeDate.Tomorrow -> {
            stringResource(R.string.tomorrow)
        }

        is RelativeDate.Weeks -> {
            pluralStringResource(R.plurals.x_weeks, count, count)
        }

        is RelativeDate.Absolute -> {
            DateTimeUtils.formatDate(date)
        }

        is RelativeDate.Overdue -> {
            stringResource(R.string.overdue)
        }
    }
