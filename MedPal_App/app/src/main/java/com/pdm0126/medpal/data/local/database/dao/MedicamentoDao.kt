package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.MedicamentoEntity
import com.pdm0126.medpal.data.model.Medicamento

import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDao{

    @Query("SELECT * FROM medicamento WHERE idUsuario =:idUsuario")
    fun getMedicamentosByUsuario(idUsuario: Long): Flow<List<MedicamentoEntity>>

    @Query("SELECT * FROM medicamento WHERE id = :id LIMIT 1")
    suspend fun getMedicamentoById(id: Long): MedicamentoEntity

    @Upsert
    suspend fun upsertMedicamento(medicamento: MedicamentoEntity)

    @Upsert
    suspend fun upsertMedicamento(medicamentos: List<MedicamentoEntity>)

    @Query("DELETE FROM medicamento WHERE id = :id")
    suspend fun deleteMedicamentoById(id: Long)

}