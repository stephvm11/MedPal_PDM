package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.ui.screens.Appoinments.formatDate
import com.pdm0126.medpal.ui.screens.Appoinments.getCurrentDate

@Composable
fun AppointmentDropdown(
    appointments: List<Appointment>,
    selectedAppointment: Appointment?,
    onAppointmentSelected: (Appointment) -> Unit,
    label: String = "Cita asociada",
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    filterPast: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val today = getCurrentDate()
    val filterAppointments = if (filterPast) {
        appointments.filter { it.date >= today }
    } else {
        appointments
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedAppointment?.let {
                "${it.title} - ${it.date.day}/${it.date.month}/${it.date.year}"
            } ?: "",
            onValueChange = {},
            label = { Text(text = label) },
            readOnly = true,
            isError = isError,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar cita asociada",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.rosy_brown),
                unfocusedBorderColor = colorResource(R.color.midnight_green).copy(alpha = 0.3f),
                errorBorderColor = colorResource(R.color.rosy_brown)
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (appointments.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "No hay citas disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.rosy_brown)
                        )
                    },
                    onClick = { expanded = false }
                )
            } else {
                appointments.forEach { appointment ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = appointment.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = formatDate(appointment.date),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black.copy(alpha = 0.6f)
                                )
                            }
                        },
                        onClick = {
                            onAppointmentSelected(appointment)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}