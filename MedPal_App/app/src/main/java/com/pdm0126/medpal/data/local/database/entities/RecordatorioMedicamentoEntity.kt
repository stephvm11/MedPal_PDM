package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.RecordatorioMedicamento

@Entity(
    tableName = "recordatorio_medicamento",
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idMedicamento"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idMedicamento")]
)

data class RecordatorioMedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hora: String,
    val frecuenciaDias: Int,
    val idMedicamento: Long
)

fun RecordatorioMedicamentoEntity.toModel(): RecordatorioMedicamento{
    return RecordatorioMedicamento(
        id = id,
        hora = hora,
        frecuenciaDias = frecuenciaDias,
        idMedicamento = idMedicamento
    )
}