package com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment

import com.pdm0126.medpal.data.local.database.entities.AppointmentWithReminders
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AppointmentRepository {
    fun getAppointmentsWithReminders(userId: Long): Flow<List<AppointmentWithReminders>>

    fun getPendingAppointmentsWithReminders(userId: Long): Flow<List<AppointmentWithReminders>>

    suspend fun refresh(userId: Long): Result<Unit>

    suspend fun completeAppointment(appointmentId: Long): Result<Unit>

    suspend fun uncompleteAppoinment(appointmentId: Long): Result<Unit>
}