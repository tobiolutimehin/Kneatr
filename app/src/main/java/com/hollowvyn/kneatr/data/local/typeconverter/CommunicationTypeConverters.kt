package com.hollowvyn.kneatr.data.local.typeconverter

import androidx.room.TypeConverter
import com.hollowvyn.kneatr.data.local.entity.CommunicationType

object CommunicationTypeConverters {
    @TypeConverter
    fun toCommunicationType(value: String) = enumValueOf<CommunicationType>(value)

    @TypeConverter
    fun fromCommunicationType(value: CommunicationType) = value.name
}
