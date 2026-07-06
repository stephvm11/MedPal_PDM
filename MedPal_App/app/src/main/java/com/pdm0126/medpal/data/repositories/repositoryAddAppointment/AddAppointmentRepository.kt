package com.pdm0126.medpal.data.repositories.repositoryAddAppointment

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AddAppointmentRepository {
    suspend fun createAppointment(
        title: String,
        specialist: String,
        place: String,
        date: String,
        time: String,
        userId: Int
    ): Result<Long>

    suspend fun createReminder(
        appointmentId: Long?,
        examId: Long?,
        startDay: LocalDate,
        time: LocalTime,
        frequencyDays: Int,
        daysBefore: Int
    ): Result<Unit>
}