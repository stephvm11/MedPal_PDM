package com.pdm0126.medpal.data.repositories.repositoryExam

import com.pdm0126.medpal.data.local.database.dao.ExamDao
import com.pdm0126.medpal.data.local.database.entities.ExamWithReminders
import com.pdm0126.medpal.data.remote.api.Exam.ExamDto
import com.pdm0126.medpal.data.remote.api.Exam.toEntity
import com.pdm0126.medpal.data.remote.api.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class ExamRepositoryImpl(
    private val examDao: ExamDao
) : ExamRepository {

    override fun getExamsWithReminders(userId: Long): Flow<List<ExamWithReminders>> {
        return examDao.getExamsWithRemindersByUser(userId)
    }

    override fun getPendingExamsWithReminders(userId: Long): Flow<List<ExamWithReminders>> {
        return examDao.getPendingExamsWithRemindersByUser(userId)
    }

    override suspend fun refresh(userId: Long): Result<Unit> {
        return try {
            val exams: List<ExamDto> =
                KtorClient.client.get("rest/v1/examen") {
                    parameter("select", "*,cita!inner(id_usuario)")
                    parameter("cita.id_usuario", "eq.$userId")
                }
                    .body()
            exams.forEach { println("   - ${it.title} (${it.place})") }

            examDao.upsertExams(exams.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeExam(examId: Long): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("estado_finalizacion", JsonPrimitive(true))
            }

            KtorClient.client.patch("rest/v1/examen?id=eq.$examId") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            examDao.completeExam(examId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uncompleteExam(examId: Long): Result<Unit> {
        return try {
            val jsonBody = buildJsonObject {
                put("estado_finalizacion", JsonPrimitive(false))
            }
            KtorClient.client.patch("rest/v1/examen?id=eq.$examId") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            examDao.uncompletedExam(examId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}