package com.pdm0126.medpal.data.model

import androidx.compose.runtime.Composable

data class AllMedItem(
    val reminderId: Long,
    val name: String,
    val dosage: String,
    val daysRemaining: Int,
    val time: String
)