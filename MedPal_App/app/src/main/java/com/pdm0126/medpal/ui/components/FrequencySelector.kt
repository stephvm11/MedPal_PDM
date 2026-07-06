package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.data.model.FrequencyReminder

@Composable
fun FrequencySelector(
    selectedFrequency: FrequencyReminder,
    onFrequencySelected: (FrequencyReminder) -> Unit,
    frequencies: List<FrequencyReminder> = emptyList(),
    title: String = "Frecuencia"
){
    if (frequencies.isEmpty()) return

    val rows = frequencies.chunked(3)

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )

        rows.forEach { rowFrequencies ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowFrequencies.forEach { frequency ->
                    FrequencyChipv2(
                        frequency = frequency,
                        selectedFrequency = selectedFrequency,
                        onClick = onFrequencySelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}