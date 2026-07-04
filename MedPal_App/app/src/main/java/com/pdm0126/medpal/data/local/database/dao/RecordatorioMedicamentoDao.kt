package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.RecordatorioMedicamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioMedicamentoDao{

    @Query("SELECT * FROM recordatorio_medicamento WHERE idMedicamento = :idMedicamento")
    fun getRecordatoriosByMedicamento(idMedicamento: Long): Flow<List<RecordatorioMedicamentoEntity>>

    @Upsert
    suspend fun upsertRecordatorio(recordatorio: RecordatorioMedicamentoEntity)

    @Upsert
    suspend fun upsertRecordatorios(recordatorios: List<RecordatorioMedicamentoEntity>)

    @Query("DELETE FROM recordatorio_medicamento WHERE id = :id")
    suspend fun deleteRecordatorioById(id: Long)

}