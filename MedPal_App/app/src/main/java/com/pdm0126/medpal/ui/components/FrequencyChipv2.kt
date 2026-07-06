package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import com.pdm0126.medpal.data.model.FrequencyReminder

@Composable
fun FrequencyChipv2(
    frequency: FrequencyReminder,
    selectedFrequency: FrequencyReminder,
    onClick: (FrequencyReminder) -> Unit,
    modifier: Modifier
) {
    val isSelected = frequency == selectedFrequency
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) colorResource(R.color.moss_green) else Color.LightGray,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable { onClick(frequency) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = frequency.toDisplayString(),
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}