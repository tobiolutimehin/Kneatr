package com.hollowvyn.kneatr.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hollowvyn.kneatr.data.local.dao.CommunicationLogDao
import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagDao
import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.data.local.typeconverter.CommunicationTypeConverters
import com.hollowvyn.kneatr.data.local.typeconverter.LocalDateConverters

@Database(
    entities = [
        ContactEntity::class,
        ContactTierEntity::class,
        ContactTagEntity::class,
        ContactTagCrossRef::class,
        CommunicationLogEntity::class,
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(LocalDateConverters::class, CommunicationTypeConverters::class)
abstract class KneatrDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    abstract fun contactTierDao(): ContactTierDao

    abstract fun tagDao(): ContactTagDao

    abstract fun contactTagCrossRefDao(): ContactTagCrossRefDao

    abstract fun communicationLogDao(): CommunicationLogDao
}
