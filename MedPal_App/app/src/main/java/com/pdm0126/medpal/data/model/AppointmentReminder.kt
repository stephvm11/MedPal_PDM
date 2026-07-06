package com.pdm0126.medpal.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class AppointmentReminder(
    val id: Long = 0,
    val idAppointment: Long? = null,
    val idExam: Long? = null,
    val startDay: LocalDate,
    val time: LocalTime,
    val frequency: String
)