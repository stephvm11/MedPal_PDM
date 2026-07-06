package com.pdm0126.medpal.data.model


enum class FrequencyReminder {
    DIARIO, CADA_3_DIAS, SEMANAL, QUINCENAL, MENSUAL, PERSONALIZADO;

    companion object {
        fun fromString(value: String): FrequencyReminder {
            return entries.find { it.name == value } ?: DIARIO
        }
    }

    fun toDisplayString(): String {
        return when (this) {
            DIARIO -> "Diario"
            CADA_3_DIAS -> "Cada 3 días"
            QUINCENAL -> "Quincenal"
            SEMANAL -> "Semanal"
            MENSUAL -> "Mensual"
            PERSONALIZADO -> "Personalizado"
        }
    }

    fun getDays(): Int?{
        return when (this) {
            DIARIO -> 1
            CADA_3_DIAS -> 3
            SEMANAL -> 7
            QUINCENAL -> 15
            MENSUAL -> 30
            PERSONALIZADO -> null
        }
    }
}
