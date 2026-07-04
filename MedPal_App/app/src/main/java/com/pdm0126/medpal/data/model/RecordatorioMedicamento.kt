package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.RecordatorioMedicamentoEntity

data class RecordatorioMedicamento(
    val id: Long = 0,
    val hora: String,
    val frecuenciaDias: Int,
    val idMedicamento: Long
)

fun RecordatorioMedicamento.toEntity(): RecordatorioMedicamentoEntity {
    return RecordatorioMedicamentoEntity(
        id = id,
        hora = hora,
        frecuenciaDias = frecuenciaDias,
        idMedicamento = idMedicamento
    )
}