package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DaysSelector(
    selectedDays: Int,
    onDaysSelected: (Int) -> Unit,
    daysOptions: List<Int> = listOf(7, 14, 21)
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        daysOptions.forEach { days ->
            DaysChip(
                days = days,
                selectedDays = selectedDays,
                onClick = onDaysSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }

}