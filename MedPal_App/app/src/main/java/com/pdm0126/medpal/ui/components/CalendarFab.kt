package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
fun CalendarFab(
    fabExpanded: Boolean,
    onFabToggle: () -> Unit,
    onAddAppointment: () -> Unit,
    onAddExam: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (fabExpanded) {
            FloatingActionButton(
                onClick = {
                    onFabToggle()
                    onAddExam()
                },
                containerColor = colorResource(R.color.rosy_brown),
                modifier = Modifier.size(70.dp).padding(5.dp)
            ) {
                Text(
                    "Examen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.beige)
                )
            }
            FloatingActionButton(
                onClick = {
                    onFabToggle()
                    onAddAppointment()
                },
                containerColor = colorResource(R.color.rosy_brown),
                modifier = Modifier.size(70.dp).padding(5.dp)
            ) {
                Text("Cita",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.beige))
            }
        }
        FloatingActionButton(
            onClick = onFabToggle,
            containerColor = colorResource(R.color.midnight_green),
            contentColor = colorResource(R.color.beige),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = if (fabExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Agregar",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}