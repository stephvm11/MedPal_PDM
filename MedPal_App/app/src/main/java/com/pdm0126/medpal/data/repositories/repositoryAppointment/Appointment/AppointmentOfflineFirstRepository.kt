package com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment

import com.pdm0126.medpal.data.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AppointmentOfflineFirstRepository {
    fun getAppointments(): Flow<List<Appointment>>

    suspend fun refresh(): Result<Unit>
    suspend fun refreshAppointment(id: Int): Result<Unit>
    suspend fun createAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        userId: Int
    ): Result<Unit>
    suspend fun updateAppointment(id: Int, title: String, specialist: String,
                                  place: String, date: LocalDate, time: LocalTime, userId: Int): Result<Unit>
    suspend fun deleteAppointment(id: Int): Result<Unit>
}