package com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment

import com.pdm0126.medpal.data.local.database.dao.AppointmentDao
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.remote.api.Appointment.AppointmentDto
import com.pdm0126.medpal.data.remote.api.Appointment.toEntity
import com.pdm0126.medpal.data.remote.api.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import io.ktor.http.ContentType

class AppointmentOfflineFirstRepositoryImpl(
    private val appointmentDao: AppointmentDao
) : AppointmentOfflineFirstRepository {
    override fun getAppointments(): Flow<List<Appointment>> {
        return appointmentDao.getAppointments().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun refresh(): Result<Unit> {
        try {
            val appointments: List<AppointmentDto> = KtorClient.client.get("rest/v1/cita").body()

            appointmentDao.upsertAppointments(appointments.map { appointmentDto -> appointmentDto.toEntity() })
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun refreshAppointment(id: Int): Result<Unit> {
        try {
            val response: List<AppointmentDto> =
                KtorClient.client.get("rest/v1/cita/$id").body()
            appointmentDao.upsertAppointments(response.map { appointmentDto -> appointmentDto.toEntity() })
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun createAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        userId: Int
    ): Result<Unit> {
        try {
            val response: AppointmentDto = KtorClient.client.post("rest/v1/cita"){
                contentType(ContentType.Application.Json)
                setBody(
                    AppointmentDto(
                        id = 0,
                        idUsuario = userId,
                        titulo = title,
                        especialista = specialist,
                        lugar = place,
                        fecha = date,
                        hora = time
                    )
                )
            }.body()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateAppointment(
        id: Int,
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        userId: Int
    ): Result<Unit> {
        try {
            val response: AppointmentDto = KtorClient.client.put("rest/v1/cita/$id") {
                contentType(ContentType.Application.Json)
                setBody(AppointmentDto(
                    id = id,
                    titulo = title,
                    especialista = specialist,
                    lugar = place,
                    fecha = date,
                    hora = time,
                    idUsuario = userId
                ))
        }. body()
            appointmentDao.upsertAppointment(response.toEntity())
            return Result.success(Unit)
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

    override suspend fun deleteAppointment(id: Int): Result<Unit> {
        try{
            KtorClient.client.delete("rest/v1/cita/$id")
            appointmentDao.deleteAppointmentById(id)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}