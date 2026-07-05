package com.pdm0126.medpal.data.repositories.repositoryRoom.Appointment

import com.pdm0126.medpal.data.local.database.dao.AppointmentDao
import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class AppointmentRepositoryImpl(
    private val appointmentDao: AppointmentDao
) : AppointmentRepository {
    override fun getAppointments(): Flow<List<Appointment>> {
        return appointmentDao.getAppointments().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun addAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        userId: Int
    ) {
        appointmentDao.insertAppointment(
            AppointmentEntity(
                title = title,
                specialist = specialist,
                place = place,
                date = date,
                time = time,
                userId = userId
            )
        )
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment.toEntity())
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment.toEntity())
    }
}