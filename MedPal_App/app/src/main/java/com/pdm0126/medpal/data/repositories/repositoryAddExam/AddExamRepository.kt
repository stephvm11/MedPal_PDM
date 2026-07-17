package com.pdm0126.medpal.data.repositories.repositoryAddExam

import com.pdm0126.medpal.data.model.Appointment
import kotlinx.datetime.LocalTime

interface AddExamRepository {
    suspend fun createExam(
        title: String,
        place: String,
        appointmentId: Long
    ): Result<Long>

    suspend fun createReminder(
        examId:Long?,
        appointmentId: Long?,
        time: LocalTime,
        frequencyDays: String,
        daysBefore: Int
    ): Result<Unit>

    suspend fun getUserAppointments(userId: Long): Result<List<Appointment>>
}