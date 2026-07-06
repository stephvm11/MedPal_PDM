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


@Composable
fun DaysChip(
    days: Int,
    selectedDays: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier
) {
    val isSelected = days == selectedDays

    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) colorResource(R.color.moss_green) else Color.LightGray,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable { onClick(days) }
            .padding(16.dp, 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$days días antes",
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}