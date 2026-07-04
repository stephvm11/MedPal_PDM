package com.pdm0126.medpal.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pdm0126.medpal.data.local.database.dao.MedicamentoDao
import com.pdm0126.medpal.data.local.database.dao.RecordatorioMedicamentoDao
import com.pdm0126.medpal.data.local.database.dao.UsuarioDao
import com.pdm0126.medpal.data.local.database.dao.ViaDao
import com.pdm0126.medpal.data.local.database.entities.MedicamentoEntity
import com.pdm0126.medpal.data.local.database.entities.RecordatorioMedicamentoEntity
import com.pdm0126.medpal.data.local.database.entities.UsuarioEntity
import com.pdm0126.medpal.data.local.database.entities.ViaEntity

@Database(
    entities = [
        UsuarioEntity::class,
        ViaEntity::class,
        MedicamentoEntity::class,
        RecordatorioMedicamentoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao
    abstract fun viaDao(): ViaDao
    abstract fun medicamentoDao(): MedicamentoDao
    abstract fun recordatorioMedicamentoDao(): RecordatorioMedicamentoDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatbase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDataBase::class.java,
                    name = "medpal_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }

}