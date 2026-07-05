package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import com.pdm0126.medpal.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.Instant
import java.time.ZoneId

@Composable
fun FormDatePicker(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    label: String,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val initialDate = value.toJavaLocalDate()
        .atStartOfDay(ZoneId.of("UTC"))
        .toInstant()
        .toEpochMilli()

    OutlinedTextField(
        value = "${value.day}/${value.month}/${value.year}",
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        enabled = true,
         modifier = Modifier.fillMaxWidth()
             .onFocusChanged { focusState ->
             if (focusState.isFocused) {
                 showDialog = true
                 focusManager.clearFocus()
             }
         },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.rosy_brown),
            unfocusedBorderColor = colorResource(R.color.midnight_green).copy(alpha = 0.3f)
        )
    )
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
        )

        DatePickerDialog(
            onDismissRequest = {showDialog = false},
            confirmButton =  {
                TextButton(
                    onClick = {datePickerState.selectedDateMillis?.let { millis ->
                    val date = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                        .toKotlinLocalDate()
                    onValueChange(date)}
                    showDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = {showDialog = false}) {
                    Text("Cancelar")
                }
            }
        ){
            DatePicker(datePickerState)
        }
    }

}