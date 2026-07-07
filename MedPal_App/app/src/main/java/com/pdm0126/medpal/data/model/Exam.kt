package com.pdm0126.medpal.data.model

data class Exam(
    val id: Long = 0,
    val title: String,
    val place: String,
    val status: Boolean = false,
    val appointmentId: Long
)