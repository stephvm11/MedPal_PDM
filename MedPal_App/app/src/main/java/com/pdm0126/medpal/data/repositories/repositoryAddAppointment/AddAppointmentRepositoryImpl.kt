package com.pdm0126.medpal.data.repositories.repositoryAddAppointment

import com.pdm0126.medpal.data.local.database.dao.AppointmentDao
import com.pdm0126.medpal.data.local.database.dao.AppointmentReminderDao
import com.pdm0126.medpal.data.remote.api.Appointment.AppointmentDto
import com.pdm0126.medpal.data.remote.api.Appointment.toEntity
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.AppointmentReminderDto
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.toEntity
import com.pdm0126.medpal.data.remote.api.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AddAppointmentRepositoryImpl(
    private val appointmentDao: AppointmentDao,
    private val appointmentReminderDao: AppointmentReminderDao
) : AddAppointmentRepository {

    override suspend fun createAppointment(
        title: String,
        specialist: String,
        place: String,
        date: String,
        time: String,
        userId: Long
    ): Result<Long> {
        return try {
            val jsonBody = buildJsonObject {
                put("titulo", title)
                put("especialista", specialist)
                put("lugar", place)
                put("fecha", date)
                put("hora", time)
                put("id_usuario", userId)
                put("estado_finalizacion", false)
            }
            val responseList: List<AppointmentDto> = KtorClient.client.post("rest/v1/cita") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                setBody(jsonBody)
            }.body()

            val response = responseList.first()

            appointmentDao.upsertAppointment(response.toEntity())

            Result.success(response.id.toLong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createReminder(
        appointmentId: Long?,
        examId: Long?,
        time: LocalTime,
        frequencyDays: String,
        daysBefore: Int
    ): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("dias_inicio", daysBefore)
                put("frecuencia", frequencyDays)
                put("hora", time.toString())
                if (appointmentId != null) {
                    put("id_cita", appointmentId.toLong())
                } else{
                    put("id_cita", null as Long?)
                }
                if (examId != null) {
                    put("id_examen", examId.toLong())
                } else{
                    put("id_examen", null as Long?)
                }

            }

            val responseList: List<AppointmentReminderDto> =
                KtorClient.client.post("rest/v1/recordatorio_cita") {
                    contentType(ContentType.Application.Json)
                    header("Prefer", "return=representation")
                    setBody(jsonBody)
                }.body()

            val response = responseList.first()

            appointmentReminderDao.upsertReminder(response.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
