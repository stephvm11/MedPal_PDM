package com.pdm0126.medpal.data.repositories.repositoryAddMed

import com.pdm0126.medpal.data.local.database.dao.AdministrationRouteDao
import com.pdm0126.medpal.data.local.database.dao.MedicationDao
import com.pdm0126.medpal.data.local.database.dao.MedicationReminderDao
import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.remote.api.Medication.MedicationDto
import com.pdm0126.medpal.data.remote.api.Medication.toEntity
import com.pdm0126.medpal.data.remote.api.MedicationReminder.MedicationReminderDto
import com.pdm0126.medpal.data.remote.api.MedicationReminder.toEntity
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.pdm0126.medpal.data.model.AdministrationRoute
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AddMedRepositoryImpl (
    private val administrationRouteDao: AdministrationRouteDao,
    private val medicationDao: MedicationDao,
    private val medicationReminderDao: MedicationReminderDao
) : AddMedRepository {

    override fun getAllRoutes(): Flow<List<AdministrationRoute>> {
        return administrationRouteDao.getAllAdministrationRoutes().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun createMedication(
        name: String,
        dosage: String,
        notes: String?,
        administrationRouteId: Long,
        userId: Long
    ): Result<Long> {
        return try {
            val jsonBody = buildJsonObject {
                put("nombre", name)
                put("dosis", dosage)
                put("nota", notes)
                put("id_via", administrationRouteId)
                put("id_usuario", userId)
            }

            val responseList: List<MedicationDto> = KtorClient.client.post("rest/v1/medicamento") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                setBody(jsonBody)
            }.body()

            val response = responseList.first()

            medicationDao.upsertMedication(response.toEntity())

            Result.success(response.id.toLong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createReminder(
        time: String,
        frequencyDays: Int,
        medicationId: Long,
        startDate: String
    ): Result<Unit> {
        return try {

            val jsonBody = buildJsonObject {
                put("hora", time)
                put("frecuencia_dias", frequencyDays)
                put("id_medicamento", medicationId)
                put("fecha_inicio", startDate)
            }

            val responseList: List<MedicationReminderDto> = KtorClient.client.post("rest/v1/recordatorio_medicamento") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                setBody(jsonBody)
            }.body()

            val response = responseList.first()

            medicationReminderDao.upsertReminder(response.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}