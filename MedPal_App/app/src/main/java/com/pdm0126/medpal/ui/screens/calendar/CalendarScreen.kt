package com.pdm0126.medpal.ui.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.ui.components.AppScaffold
import com.pdm0126.medpal.ui.components.CalendarEventsBottomSheet
import com.pdm0126.medpal.ui.components.CalendarFab
import com.pdm0126.medpal.ui.components.TopBarCases
import com.pdm0126.medpal.ui.screens.Appoinments.AppointmentViewModel
import com.pdm0126.medpal.ui.screens.Appoinments.ExamViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun CalendarScreen(
    appointmentViewModel: AppointmentViewModel = viewModel(factory = AppointmentViewModel.Factory),
    examViewModel: ExamViewModel = viewModel(factory = ExamViewModel.Factory),
    onAddAppointmentClick: () -> Unit,
    onAddExamClick: () -> Unit,
    onBack: () -> Unit
) {
    val appointments by appointmentViewModel.appointments.collectAsStateWithLifecycle()
    val exams by examViewModel.exams.collectAsStateWithLifecycle()

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var selectedDate by remember { mutableStateOf(today) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var fabExpanded by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.todayIn(TimeZone.UTC)
            .atStartOfDayIn(TimeZone.UTC)
            .toEpochMilliseconds()
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val kotlinxInstant = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)

            selectedDate = kotlinxInstant.toLocalDateTime(TimeZone.UTC).date
            showBottomSheet = true
        }
    }

    LaunchedEffect(selectedDate) {
        if (selectedDate == null) {
            showBottomSheet = false
        }
    }

    AppScaffold(
        title = "Mis citas y exámenes",
        topBarScreenCase = TopBarCases.NAVIGATION,
        onBackClick = onBack,
        floatingActionButton = {
            CalendarFab(
                fabExpanded = fabExpanded,
                onFabToggle = { fabExpanded = !fabExpanded },
                onAddAppointment = onAddAppointmentClick,
                onAddExam = onAddExamClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = null,
                headline = null
            )
        }

        if (showBottomSheet) {
            val appointmentsToday = appointments.filter { it.date == selectedDate }
            val examsToday = exams.filter { exam ->
                appointments.any { appointment ->
                    appointment.id == exam.appointmentId && appointment.date == selectedDate
                }
            }

            CalendarEventsBottomSheet(
                selectedDate = selectedDate,
                appointments = appointmentsToday,
                exams = examsToday,
                onDismiss = { showBottomSheet = false }
            )
        }
    }

}

