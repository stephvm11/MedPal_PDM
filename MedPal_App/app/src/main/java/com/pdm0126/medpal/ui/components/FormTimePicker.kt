package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.pdm0126.medpal.R
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTimePicker(
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    label: String
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = value.hour,
        initialMinute = value.minute,
        is24Hour = true
    )

    OutlinedTextField(
        value = "${value.hour.toString().padStart(2, '0')}:${
            value.minute.toString().padStart(2, '0')
        }",
        onValueChange = {},
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.rosy_brown),
            unfocusedBorderColor = colorResource(R.color.midnight_green).copy(alpha = 0.3f)
        )
    )

    if (showDialog) {
        TimePickerDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona la hora") },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange(LocalTime(timePickerState.hour, timePickerState.minute))
                    showDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }) {
                    Text("Cancelar")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}