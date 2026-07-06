package com.pdm0126.medpal.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pdm0126.medpal.data.local.database.dao.MedicationDao
import com.pdm0126.medpal.data.local.database.dao.MedicationReminderDao
import com.pdm0126.medpal.data.local.database.dao.UserDao
import com.pdm0126.medpal.data.local.database.dao.AdministrationRouteDao
import com.pdm0126.medpal.data.local.database.dao.AppointmentDao
import com.pdm0126.medpal.data.local.database.dao.AppointmentReminderDao
import com.pdm0126.medpal.data.local.database.entities.MedicationEntity
import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity
import com.pdm0126.medpal.data.local.database.entities.UserEntity
import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity
import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import com.pdm0126.medpal.data.local.database.entities.AppointmentReminderEntity

@Database(
    entities = [
        UserEntity::class,
        AdministrationRouteEntity::class,
        MedicationEntity::class,
        MedicationReminderEntity::class,
        AppointmentEntity::class,
        AppointmentReminderEntity::class
    ],
    version = 12,
exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun administrationRouteDao(): AdministrationRouteDao
    abstract fun medicationDao(): MedicationDao
    abstract fun medicationReminderDao(): MedicationReminderDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun appointmentReminderDao(): AppointmentReminderDao
    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context.applicationContext,
                klass = AppDataBase::class.java,
                name = "medpal_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}