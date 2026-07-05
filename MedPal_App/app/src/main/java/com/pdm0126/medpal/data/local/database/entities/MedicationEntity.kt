package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Medication

@Entity(
    tableName = "medicamento",
    foreignKeys = [
        ForeignKey(
            entity = AdministrationRouteEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_via"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_via"),
        Index("id_usuario")
    ]
)
data class MedicationEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "nombre")
    val name: String,
    @ColumnInfo(name = "dosis")
    val dosage: String,
    @ColumnInfo(name = "nota")
    val notes: String? = null,
    @ColumnInfo(name = "ultima_dosis")
    val lastDose: String? = null,
    @ColumnInfo(name = "id_via")
    val administrationRouteId: Long,
    @ColumnInfo(name = "id_usuario")
    val userId: Long
)

fun MedicationEntity.toModel(): Medication {
    return Medication(
        id = id,
        name = name,
        dosage = dosage,
        notes = notes,
        lastDose = lastDose,
        administrationRouteId = administrationRouteId,
        userId = userId
    )
}