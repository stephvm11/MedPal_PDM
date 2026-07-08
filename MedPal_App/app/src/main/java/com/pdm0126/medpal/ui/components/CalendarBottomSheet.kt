package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEventsBottomSheet(
    selectedDate: LocalDate,
    appointments: List<com.pdm0126.medpal.data.model.Appointment>, // Ajusta a tu paquete de modelos
    exams: List<com.pdm0126.medpal.data.model.Exam>,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    val appointmentsToday = remember(selectedDate, appointments) {
        appointments.filter { appointment ->
            appointment.date == selectedDate
        }
    }

    val examsToday = remember(appointmentsToday, exams) {
        val todayAppointmentIds = appointmentsToday.map { it.id }.toSet()
        exams.filter { exam -> exam.appointmentId in todayAppointmentIds }
    }

    val isEmpty = appointmentsToday.isEmpty() && examsToday.isEmpty()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            Text(
                text = "Agenda del día (${selectedDate.dayOfMonth}/${selectedDate.monthNumber}/${selectedDate.year})",
                color = colorResource(R.color.midnight_green),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes ninguna cita ni examen programado para este día.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    items(appointmentsToday) { appointment ->
                        CalendarEventCard(
                            title = appointment.title,
                            time = appointment.time.toString(),
                            place = appointment.place,
                            badgeColor = colorResource(id = R.color.midnight_green),
                            badgeText = "Cita"
                        )
                    }

                    items(examsToday) { exam ->
                        val associatedAppointment = appointmentsToday.find { it.id == exam.appointmentId }

                        CalendarEventCard(
                            title = exam.title,
                            time = associatedAppointment?.time?.toString() ?: "Hora no definida",
                            place = associatedAppointment?.place ?: "Lugar no definido",
                            badgeColor = colorResource(id = R.color.rosy_brown),
                            badgeText = "Examen"
                        )
                    }
                }
            }
        }
    }
}