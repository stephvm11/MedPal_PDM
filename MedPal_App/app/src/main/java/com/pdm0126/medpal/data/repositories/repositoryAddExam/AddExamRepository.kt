package com.pdm0126.medpal.data.repositories.repositoryAddExam

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
        frequencyDays: Int,
        daysBefore: Int
    ): Result<Unit>
}