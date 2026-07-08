package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.Instant
import java.time.ZoneId

@Composable
fun FormDatePicker(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val initialDate = value.toJavaLocalDate()
        .atStartOfDay(ZoneId.of("UTC"))
        .toInstant()
        .toEpochMilli()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorResource(R.color.beige),
        unfocusedTextColor = colorResource(R.color.beige),
        focusedLabelColor = colorResource(R.color.beige),
        unfocusedLabelColor = colorResource(R.color.beige),
        focusedBorderColor = colorResource(R.color.beige),
        unfocusedBorderColor = colorResource(R.color.beige)
    )

    val backgroundColor = colorResource(id = R.color.midnight_green)
    val letterColors = colorResource(id = R.color.beige)
    val backgroundSelectedColor = colorResource(id = R.color.phthalo_green)
    val letterSelectedColor = colorResource(id =R.color.beige)

    OutlinedTextField(
        value = "${value.day}/${value.month}/${value.year}",
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        enabled = true,
         modifier = Modifier.width(200.dp)
             .onFocusChanged { focusState ->
             if (focusState.isFocused) {
                 showDialog = true
                 focusManager.clearFocus()
             }
         },
        colors = textFieldColors
    )
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
        )
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                surface = backgroundColor,
                onSurface = letterColors,
                onSurfaceVariant = letterColors,
                primary = letterColors,
                outline = letterColors
            )
        ) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                    .toKotlinLocalDate()
                                onValueChange(date)
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = backgroundColor,
                        titleContentColor = letterColors,
                        headlineContentColor = letterColors,
                        weekdayContentColor = letterColors,
                        subheadContentColor = letterColors,
                        navigationContentColor = letterColors,
                        yearContentColor = letterColors,
                        dayContentColor = letterColors,
                        selectedDayContainerColor = backgroundSelectedColor,
                        selectedDayContentColor = letterSelectedColor,
                        todayContentColor = letterColors,
                        todayDateBorderColor = backgroundSelectedColor
                    )
                )
            }
        }
    }
}