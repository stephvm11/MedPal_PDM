package com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment

import com.pdm0126.medpal.data.local.database.dao.AppointmentDao
import com.pdm0126.medpal.data.local.database.dao.AppointmentReminderDao
import com.pdm0126.medpal.data.local.database.entities.AppointmentWithReminders
import com.pdm0126.medpal.data.remote.api.Appointment.AppointmentDto
import com.pdm0126.medpal.data.remote.api.Appointment.toEntity
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.AppointmentReminderDto
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.toEntity
import com.pdm0126.medpal.data.remote.api.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class AppointmentRepositoryImpl(
    private val appointmentDao: AppointmentDao,
    private val reminderDao: AppointmentReminderDao
) : AppointmentRepository {
    override fun getAppointmentsWithRemindersNotifications(userId: Long): Flow<List<AppointmentWithReminders>> {
        return appointmentDao.getAppointmentsWithRemindersNotifications(userId)
    }

    override fun getAppointmentsWithReminders(userId: Long): Flow<List<AppointmentWithReminders>> {
        return appointmentDao.getAppointmentsWithRemindersByUser(userId)
    }

    override fun getPendingAppointmentsWithReminders(userId: Long): Flow<List<AppointmentWithReminders>> {
        return appointmentDao.getPendingAppointmentsWithReminderByUser(userId)
    }

    override suspend fun refresh(userId: Long): Result<Unit> {
        return try {
            val appointments: List<AppointmentDto> =
                KtorClient.client.get("rest/v1/cita") { parameter("id_usuario", "eq.$userId") }
                    .body()
            appointments.forEach { println("   - ${it.title} (${it.date})") }

            val reminders: List<AppointmentReminderDto> =
                KtorClient.client.get("rest/v1/recordatorio_cita"){
                    parameter("select", "*,cita!inner(id_usuario)")
                    parameter("cita.id_usuario", "eq.$userId")
                }.body()

            appointmentDao.upsertAppointments(appointments.map { it.toEntity() })

            reminderDao.upsertReminders(reminders.map { it.toEntity()})

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeAppointment(appointmentId: Long): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("estado_finalizacion", JsonPrimitive(true))
            }

            KtorClient.client.patch("rest/v1/cita?id=eq.$appointmentId") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            appointmentDao.completeAppointment(appointmentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uncompleteAppoinment(appointmentId: Long): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("estado_finalizacion", JsonPrimitive(false))
            }
            KtorClient.client.patch("rest/v1/cita?id=eq.$appointmentId") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            appointmentDao.uncompletedAppointment(appointmentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}