package com.pdm0126.medpal.data.repositories.repositoryRoom.Appointment

import com.pdm0126.medpal.data.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AppointmentRepository {
    fun getAppointments(): Flow<List<Appointment>>

    suspend fun addAppointment(title: String, specialist: String, place: String, date: LocalDate, time: LocalTime, userId: Int)

    suspend fun deleteAppointment(appointment: Appointment)

    suspend fun updateAppointment(appointment: Appointment)
}