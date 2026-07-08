package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.ExamEntity
import com.pdm0126.medpal.data.local.database.entities.ExamWithReminders
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface ExamDao {
    @Transaction
    @Query(
        """
        SELECT examen.* 
        FROM examen 
        INNER JOIN cita ON cita.id = examen.id_cita 
        WHERE cita.id_usuario = :userId 
        ORDER BY cita.fecha ASC, cita.hora ASC
    """
    )
    fun getExamsWithRemindersByUser(userId: Long): Flow<List<ExamWithReminders>>

    @Query(
        """
        SELECT examen.* 
        FROM examen 
        INNER JOIN cita ON cita.id = examen.id_cita 
        WHERE cita.id_usuario = :userId 
        AND examen.estado_finalizacion = 0 
        ORDER BY cita.fecha ASC, cita.hora ASC
    """
    )
    fun getPendingExamsWithRemindersByUser(userId: Long): Flow<List<ExamWithReminders>>

    @Query(
        """
        SELECT examen.* 
        FROM examen 
        INNER JOIN cita ON cita.id = examen.id_cita 
        WHERE cita.id_usuario = :userId 
        AND examen.estado_finalizacion = 1 
        ORDER BY cita.fecha ASC, cita.hora ASC
    """
    )
    fun getCompletedExamsWithRemindersByUser(userId: Long): Flow<List<ExamWithReminders>>

    @Query(
        """
    SELECT examen.* 
    FROM examen 
    INNER JOIN cita ON cita.id = examen.id_cita 
    WHERE cita.id_usuario = :userId 
    AND cita.fecha = :date 
    AND examen.estado_finalizacion = 0 
    ORDER BY cita.hora ASC
"""
    )
    fun getTodayExamsWithRemindersByUser(
        userId: Long,
        date: LocalDate
    ): Flow<List<ExamWithReminders>>

    @Query(
        """
        SELECT examen .*
                FROM examen
                INNER JOIN cita ON cita.id = examen.id_cita
                WHERE cita.id_usuario = :userId
                AND cita . fecha BETWEEN
        :startDate AND :endDate
        AND examen . estado_finalizacion =
        0
                ORDER BY cita . fecha ASC,
        cita.hora ASC
                """
    )
    fun getWeeklyExamsWithRemindersByUser(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<ExamWithReminders>>

    @Query("UPDATE examen SET estado_finalizacion = 1 WHERE id = :examId")
    suspend fun completeExam(examId: Long)

    @Query("UPDATE examen SET estado_finalizacion = 0 WHERE id = :examId")
    suspend fun uncompletedExam(examId: Long)

    @Upsert
    suspend fun upsertExam(exam: ExamEntity)

    @Upsert
    suspend fun upsertExams(exam: List<ExamEntity>)

}