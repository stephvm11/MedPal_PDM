package com.pdm0126.medpal.data.model

enum class AlertType { MEDICAMENTO, CITA, EXAMEN }

data class AlertData(
    val id: Long,
    val type: AlertType,
    val title: String,
    val subtitle: String,
    val extraInfo: String? = null
)