package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.MedicamentoEntity

data class Medicamento(
    val id: Long = 0,
    val nombre: String,
    val dosis: String,
    val nota: String? = null,
    val ultimaDosis: String? = null,
    val idVia: Long,
    val idUsuario: Long
)

fun Medicamento.toEntity(): MedicamentoEntity {
    return MedicamentoEntity(
        id = id,
        nombre = nombre,
        dosis = dosis,
        nota = nota,
        ultimaDosis = ultimaDosis,
        idVia = idVia,
        idUsuario = idUsuario
    )
}