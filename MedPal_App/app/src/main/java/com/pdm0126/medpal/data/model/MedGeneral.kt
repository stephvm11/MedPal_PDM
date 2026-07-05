package com.pdm0126.medpal.data.model

data class MedGeneral(
    val isLoading: Boolean = false,
    val dailyMedications: List<MedDay> = emptyList(),
    val allMedications: List<AllMedItem> = emptyList()
)
