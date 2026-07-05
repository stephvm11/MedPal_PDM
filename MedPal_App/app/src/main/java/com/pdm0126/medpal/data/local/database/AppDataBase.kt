package com.pdm0126.medpal.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pdm0126.medpal.data.local.database.dao.MedicationDao
import com.pdm0126.medpal.data.local.database.dao.MedicationReminderDao
import com.pdm0126.medpal.data.local.database.dao.UserDao
import com.pdm0126.medpal.data.local.database.dao.AdministrationRouteDao
import com.pdm0126.medpal.data.local.database.entities.MedicationEntity
import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity
import com.pdm0126.medpal.data.local.database.entities.UserEntity
import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity

@Database(
    entities = [
        UserEntity::class,
        AdministrationRouteEntity::class,
        MedicationEntity::class,
        MedicationReminderEntity::class
    ],
    version = 7,
exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun administrationRouteDao(): AdministrationRouteDao
    abstract fun medicationDao(): MedicationDao
    abstract fun medicationReminderDao(): MedicationReminderDao
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