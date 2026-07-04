package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Medicamento

@Entity(
    tableName = "medicamento",
    foreignKeys = [
        ForeignKey(
            entity = ViaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idVia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("idVia"),
        Index("idUsuario")
    ]
)

data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val dosis: String,
    val nota: String? = null,
    val ultimaDosis: String? = null,
    val idVia: Long,
    val idUsuario: Long
)

fun MedicamentoEntity.toModel(): Medicamento {
    return Medicamento(
        id = id,
        nombre = nombre,
        dosis = dosis,
        nota = nota,
        ultimaDosis = ultimaDosis,
        idVia = idVia,
        idUsuario = idUsuario
    )
}