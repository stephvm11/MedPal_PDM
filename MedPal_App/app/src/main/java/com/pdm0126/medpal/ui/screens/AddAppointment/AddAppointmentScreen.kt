package com.pdm0126.medpal.ui.screens.AddAppointment

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.R
import com.pdm0126.medpal.data.model.FrequencyReminder
import com.pdm0126.medpal.data.notifications.ReminderAlarmManager
import com.pdm0126.medpal.ui.components.AppScaffold
import com.pdm0126.medpal.ui.components.DaysChip
import com.pdm0126.medpal.ui.components.DaysSelector
import com.pdm0126.medpal.ui.components.FormDatePicker
import com.pdm0126.medpal.ui.components.FormTextField
import com.pdm0126.medpal.ui.components.FormTimePicker
import com.pdm0126.medpal.ui.components.FrequencySelector
import com.pdm0126.medpal.ui.components.TopBarCases
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.internal.missingFieldExceptionWithNewMessage
import kotlin.time.Clock

@Composable
fun AddAppointmentScreen(
    viewModel: AddAppointmentViewModel = viewModel(factory = AddAppointmentViewModel.Factory),
    onSave: () -> Unit,
    onClose: () -> Unit,
) {

    val context = LocalContext.current
    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var specialist by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(now.date) }
    var time by remember { mutableStateOf(now.time) }
    var reminderTime by remember { mutableStateOf(now.time) }

    var isReminderEnabled by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf(FrequencyReminder.DIARIO) }
    var startDay by remember { mutableIntStateOf(7) }

    val isFormValid = title.isNotBlank() && specialist.isNotBlank() && place.isNotBlank()

    LaunchedEffect(Unit) {
        viewModel.event.collect { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                if (message.contains("exitosamente") && isReminderEnabled) {

                    val alarmDate = date.minus(startDay, DateTimeUnit.DAY)
                    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                    if (alarmDate >= today) {
                        ReminderAlarmManager.scheduleAppointmentAlarm(
                            context = context,
                            appointmentId = 0L,
                            title = "Recordatorio de Cita: $title",
                            description = "Tu cita con $specialist en $place es el $date",
                            hour = reminderTime.hour,
                            minute = reminderTime.minute,
                            appointmentDate = date,
                            daysBefore = startDay,
                            frequency = selectedFrequency.name
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    AppScaffold(
        "Nueva cita",
        topBarScreenCase = TopBarCases.FORM,
        false,
        isSaveEnabled = isFormValid && !isLoading,
        onSaveClick = {
            if (isFormValid && !isLoading) {
                viewModel.createAppointment(
                    title = title,
                    specialist = specialist,
                    place = place,
                    date = date,
                    time = time,
                    hasReminder = isReminderEnabled,
                    reminderTime = if (isReminderEnabled) reminderTime else null,
                    frequency = if (isReminderEnabled) selectedFrequency else null,
                    startDay = if (isReminderEnabled) startDay else null,
                    onSuccess = onSave
                )
            }
        },
        onCloseClick = onClose
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(
                    rememberScrollState()
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                value = title,
                onValueChange = { title = it },
                label = "Titulo de la cita médica",
                placeholder = "Ej. Consulta con cardiólogo"
            )
            FormTextField(
                value = specialist,
                onValueChange = { specialist = it },
                label = "Nombre del especialista",
                placeholder = "Ej. Dr. Martínez"
            )
            FormTextField(
                value = place,
                onValueChange = { place = it },
                label = "Lugar de la cita",
                placeholder = "Ej. Hospital Rosales"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FormDatePicker(
                    value = date,
                    onValueChange = { date = it },
                    label = "Fecha de la cita",
                    modifier = Modifier.weight(1f)
                )
                FormTimePicker(
                    value = time,
                    onValueChange = { time = it },
                    label = "Hora de la cita",
                    modifier = Modifier.weight(2f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isReminderEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                    contentDescription = null,
                    tint = if (isReminderEnabled) colorResource(R.color.moss_green) else colorResource(
                        R.color.rosy_brown
                    ), modifier = Modifier.size(28.dp)
                )
                Button(
                    onClick = { isReminderEnabled = !isReminderEnabled },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReminderEnabled) colorResource(R.color.moss_green) else colorResource(
                            R.color.rosy_brown
                        ), contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp),
                    border = if (isReminderEnabled) ButtonDefaults.outlinedButtonBorder(enabled = true) else null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isReminderEnabled) "Recordatorio" else "Agregar recordatorio",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            AnimatedVisibility(visible = isReminderEnabled) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FormTimePicker(
                        value = reminderTime,
                        onValueChange = { reminderTime = it },
                        label = "Hora de recodatorio"
                    )
                    Text(
                        text = "¿Desde cuándo te empezamos a recordar?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    DaysSelector(
                        selectedDays = startDay,
                        onDaysSelected = { startDay = it }
                    )

                    FrequencySelector(
                        selectedFrequency = selectedFrequency,
                        onFrequencySelected = { selectedFrequency = it },
                        frequencies = listOf(
                            FrequencyReminder.DIARIO,
                            FrequencyReminder.CADA_3_DIAS,
                            FrequencyReminder.SEMANAL
                        ),
                        title = "Frecuencia"
                    )
                }
            }
        }

    }
}