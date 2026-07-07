package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.ExamEntity

data class Exam(
    val id: Long = 0,
    val title: String,
    val place: String,
    val appointmentId: Long,
    val status: Boolean = false

)
