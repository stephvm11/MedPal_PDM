package com.pdm0126.medpal.data.model

data class MedDay(
    val reminderId: Long,
    val name: String,
    val dosage: String,
    val time: String,
    val isTaken: Boolean
)