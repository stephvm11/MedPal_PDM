package com.pdm0126.medpal.data.repositories.repositoryMedication

import com.pdm0126.medpal.data.local.database.dao.AdministrationRouteDao
import com.pdm0126.medpal.data.local.database.dao.MedicationDao
import com.pdm0126.medpal.data.local.database.dao.MedicationReminderDao
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.model.Medication
import com.pdm0126.medpal.data.model.MedicationReminder
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.remote.api.Medication.MedicationDto
import com.pdm0126.medpal.data.remote.api.Medication.toEntity
import com.pdm0126.medpal.data.remote.api.MedicationReminder.MedicationReminderDto
import com.pdm0126.medpal.data.remote.api.MedicationReminder.toEntity
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.pdm0126.medpal.data.local.database.entities.MedicationWithReminders
import com.pdm0126.medpal.data.remote.api.AdministrationRoute.AdministrationRouteRemoteDto
import com.pdm0126.medpal.data.remote.api.AdministrationRoute.toEntity

class MedicationRepositoryImpl (
    private val medicationDao: MedicationDao,
    private val reminderDao: MedicationReminderDao,
    private val administrationRouteDao: AdministrationRouteDao
) : MedicationRepository {

    override fun getMedicationsWithReminders(userId: Long): Flow<List<MedicationWithReminders>> {
        return medicationDao.getMedicationsWithRemindersByUser(userId)
    }

    override suspend fun refresh(userId: Long): Result<Unit> {
        return try {

            val routes: List<AdministrationRouteRemoteDto> = KtorClient.client.get("rest/v1/via").body()

            val medications: List<MedicationDto> = KtorClient.client.get ("rest/v1/medicamento"){
                parameter("id_usuario","eq.$userId")
            }.body()

            val reminders: List<MedicationReminderDto> = KtorClient.client.get("rest/v1/recordatorio_medicamento").body()

            administrationRouteDao.upsertAdministrationRoutes(routes.map { it.toEntity() })
            medicationDao.upsertMedications(medications.map { it.toEntity() })
            reminderDao.upsertReminders(reminders.map { it.toEntity() })

            Result.success(Unit)

        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun createMedication(
        name: String,
        dosage: String,
        notes: String?,
        administrationRouteId: Long,
        userId: Long
    ): Result<Unit> {
        return try {
            val bodyMap = mapOf(
                "nombre" to name,
                "dosis" to dosage,
                "nota" to notes,
                "id_via" to administrationRouteId,
                "id_usuario" to userId
            )

            val response: MedicationDto = KtorClient.client.post("rest/v1/medicamento") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                setBody(bodyMap)
            }.body()

            medicationDao.upsertMedication(response.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMedication(id: Long): Result<Unit> {
        return try {
            KtorClient.client.delete("rest/v1/medicamento") {
                parameter("id", "eq.$id")
            }
            medicationDao.deleteMedicationById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createReminder(
        time: String,
        frequencyDays: Int,
        medicationId: Long
    ): Result<Unit> {
        return try {
            val bodyMap = mapOf(
                "hora" to time,
                "frecuencia_dias" to frequencyDays,
                "id_medicamento" to medicationId
            )

            val response: MedicationReminderDto = KtorClient.client.post("rest/v1/recordatorio_medicamento") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                setBody(bodyMap)
            }.body()

            reminderDao.upsertReminder(response.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReminder(id: Long): Result<Unit> {
        return try {
            KtorClient.client.delete("rest/v1/recordatorio_medicamento") {
                parameter("id", "eq.$id")
            }
            reminderDao.deleteReminderById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}