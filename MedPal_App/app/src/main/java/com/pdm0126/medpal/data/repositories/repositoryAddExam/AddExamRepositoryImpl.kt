package com.pdm0126.medpal.data.repositories.repositoryAddExam

import com.pdm0126.medpal.data.local.database.dao.AppointmentReminderDao
import com.pdm0126.medpal.data.local.database.dao.ExamDao
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.AppointmentReminderDto
import com.pdm0126.medpal.data.remote.api.Exam.ExamDto
import com.pdm0126.medpal.data.remote.api.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import com.pdm0126.medpal.data.remote.api.Exam.toEntity
import com.pdm0126.medpal.data.remote.api.AppointmentReminder.toEntity
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.put


class AddExamRepositoryImpl(
    private val examDao: ExamDao,
    private val appointmentReminderDao: AppointmentReminderDao
) : AddExamRepository {
    override suspend fun createExam(
        title: String,
        place: String,
        appointmentId: Long
    ): Result<Long> {
        return try {
            val jsonBody = buildJsonObject {
                put("titulo", title)
                put("id_cita", appointmentId)
                put("place", place)
                put("estado_finalizacion", false)
            }
            val responseList: List<ExamDto> = KtorClient.client.post("rest/v1/examen") {
                contentType(ContentType.Application.Json)
                header("Prefer", "return=representation")
                val jsonBody = null
                setBody(jsonBody)
            }.body()

            val response = responseList.first()

            examDao.upsertExam(response.toEntity())

            Result.success(response.id.toLong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createReminder(
        examId: Long?,
        appointmentId: Long?,
        time: LocalTime,
        frequencyDays: Int,
        daysBefore: Int
    ): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("dias_inicio", daysBefore)
                put("frecuencia", frequencyDays.toString())
                put("hora", time.toString())
                if (appointmentId != null) {
                    put("id_cita", appointmentId.toLong())
                } else {
                    put("id_cita", null as Long?)
                }
                if (examId != null) {
                    put("id_examen", examId.toLong())
                } else {
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

