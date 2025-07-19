package com.hollowvyn.kneatr.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.dao.TagDao
import com.hollowvyn.kneatr.data.local.entity.CommunicationLog
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.TagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.data.local.typeconverter.CommunicationTypeConverters
import com.hollowvyn.kneatr.data.local.typeconverter.LocalDateConverters

@Database(
    entities = [
        ContactEntity::class,
        ContactTierEntity::class,
        TagEntity::class,
        ContactTagCrossRef::class,
        CommunicationLog::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(LocalDateConverters::class, CommunicationTypeConverters::class)
abstract class KneatrDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    abstract fun contactTierDao(): ContactTierDao

    abstract fun tagDao(): TagDao

    abstract fun contactTagCrossRefDao(): ContactTagCrossRefDao
}
