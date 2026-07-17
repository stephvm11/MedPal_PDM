package com.pdm0126.medpal.ui.screens.Appoinments

import android.os.Build
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.R
import com.pdm0126.medpal.ui.components.AppScaffold
import com.pdm0126.medpal.ui.components.TopBarCases
import com.pdm0126.medpal.R.color
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.notifications.AlertGlobalEvent
import com.pdm0126.medpal.ui.components.AddCard
import com.pdm0126.medpal.ui.components.AppoinmentCard
import com.pdm0126.medpal.ui.components.ClosestAppoinment
import com.pdm0126.medpal.ui.components.ExamCard
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentsHomeScreen(

    appointmentViewModel: AppointmentViewModel = viewModel(factory = AppointmentViewModel.Factory),
    examViewModel: ExamViewModel = viewModel(factory = ExamViewModel.Factory),
    onNavigateToProfile: () -> Unit,
    currentRoute: String,
    onNavigateToItemClick: (String) -> Unit,
    onAddAppointmentClick: () -> Unit = {},
    onAddExamClick: () -> Unit = {},
    onNavigateToCalendar: () -> Unit
) {

    val context = LocalContext.current
    val now = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    val appointments by appointmentViewModel.appointments.collectAsStateWithLifecycle()
    val nextAppointment by appointmentViewModel.nextAppointment.collectAsStateWithLifecycle()
    val exams by examViewModel.exams.collectAsStateWithLifecycle()
    val isLoadingAppointments by appointmentViewModel.isLoading.collectAsState()
    val isLoadingExams by examViewModel.isLoading.collectAsState()
    val errorAppointments by appointmentViewModel.error.collectAsState()
    val errorExams by examViewModel.error.collectAsState()
    val refreshExams by examViewModel.refreshing.collectAsState()
    val refreshAppointments by appointmentViewModel.refreshing.collectAsState()

    val upcomingAppointments = remember(appointments, now){
        appointments.filter { appointment ->
            val appointmentDateTime = LocalDateTime(
                year = appointment.date.year,
                month = appointment.date.month,
                day = appointment.date.day,
                hour = appointment.time.hour,
                minute = appointment.time.minute
            )
            appointmentDateTime >= now
        }
    }

    val realNextAppointment = remember(upcomingAppointments) {
        upcomingAppointments
            .sortedWith(compareBy<Appointment> { it.date }.thenBy { it.time })
            .firstOrNull()
    }

    val upcomingExams = remember(exams, appointments, now) {
        exams.filter { exam ->
            val associatedAppointment = appointments.find { it.id == exam.appointmentId }
            if (associatedAppointment != null){
                val examDateTime = LocalDateTime(
                    year = associatedAppointment.date.year,
                    month = associatedAppointment.date.month,
                    day = associatedAppointment.date.day,
                    hour = associatedAppointment.time.hour,
                    minute = associatedAppointment.time.minute
                )
                examDateTime >= now
            } else{
                false
            }
        }
    }
    LaunchedEffect(Unit) {
        appointmentViewModel.refreshAppointments(context)
        examViewModel.refreshExams(context)
    }

    LaunchedEffect(key1 = true) {
        appointmentViewModel.event.collect { message ->
            makeText(context, message, LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = true) {
        appointmentViewModel.event.collect { message ->
            makeText(context, message, LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = true) {
        examViewModel.event.collect { message ->
            makeText(context, message, LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        AlertGlobalEvent.appointmentConfirmations.collect { appointmentId ->
            appointmentViewModel.completeAppointment(appointmentId)
        }
    }

    LaunchedEffect(Unit) {
        AlertGlobalEvent.examConfirmations.collect { examId ->
            examViewModel.completeExam(examId)
        }
    }


    AppScaffold(
        DateUtils.format(getCurrentDate()),
        TopBarCases.HOME,
        true,
        currentRoute,
        onNavigateToItemClick,
        onNavigateToCalendar,
        onNavigateToProfile,
    ) { paddingValues ->

        if (isLoadingAppointments && appointments.isEmpty() || (isLoadingExams && exams.isEmpty())) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(R.color.midnight_green),
                    modifier = Modifier.size(48.dp)
                )
            }
            return@AppScaffold
        }

        PullToRefreshBox(
            isRefreshing = refreshAppointments || refreshExams,
            onRefresh = {
                appointmentViewModel.clearError()
                examViewModel.clearError()
                appointmentViewModel.refreshAppointments(context)
                examViewModel.refreshExams(context)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                if (errorAppointments != null && appointments.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error al cargar citas: ${errorAppointments}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Desliza hacia abajo para reintentar",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.midnight_green).copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                } else {
                    ClosestAppoinment(realNextAppointment)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mis citas médicas próximas",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AddCard(onAddClick = onAddAppointmentClick)
                        Spacer(modifier = Modifier.width(8.dp))

                        if (upcomingAppointments.isEmpty()) {
                            Card(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                                border = BorderStroke(3.dp, colorResource(color.beige))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "No tienes citas proximamente",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Toca + para agregar.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            LazyRow(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(upcomingAppointments) { appointment ->
                                    AppoinmentCard(
                                        appointment = appointment,
                                        onToggleComplete = { appoinmentId ->
                                            val currentAppointment =
                                                appointments.find { it.id == appoinmentId }
                                            if (currentAppointment != null) {
                                                if (currentAppointment.status) {
                                                    appointmentViewModel.uncompleteAppointment(
                                                        appoinmentId
                                                    )
                                                } else {
                                                    appointmentViewModel.completeAppointment(
                                                        appoinmentId
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.width(6.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 10.dp,
                        bottom = 7.dp,
                        end = 16.dp
                    ),
                    thickness = 2.dp,
                    colorResource(color.midnight_green)
                )
                Spacer(modifier = Modifier.width(6.dp))

                if (errorExams != null && exams.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error al cargar exámenes: ${errorExams}")
                    }
                } else {
                    Text(
                        text = "Mis exámenes médicos próximos",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AddCard(onAddClick = onAddExamClick)
                        Spacer(modifier = Modifier.width(8.dp))
                        if (upcomingExams.isEmpty()) {
                            Card(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                                border = BorderStroke(3.dp, colorResource(color.beige))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "No tienes examenes proximamente",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Toca + para agregar.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            LazyRow(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(upcomingExams) { exam ->

                                    val associatedAppointment = appointments.find { it.id == exam.appointmentId }
                                    ExamCard(
                                        exam = exam,
                                        associatedAppointment = associatedAppointment,
                                        onToggleComplete = { examId ->
                                            val currentExam =
                                                exams.find { it.id == examId }
                                            if (currentExam != null) {
                                                if (currentExam.status) {
                                                    examViewModel.uncompleteExam(examId)
                                                } else {
                                                    examViewModel.completeExam(examId)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun getCurrentDate(): kotlinx.datetime.LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}

fun getDaysBetween(start: LocalDate, end: LocalDate): Int {
    return abs(start.daysUntil(end))
}

fun formatDate(date: LocalDate): String {
    return "${date.dayOfMonth}/${date.monthNumber}/${date.year.toString().takeLast(2)}"
}

fun formatTime(time: LocalTime): String {
    return "${time.hour.toString().padStart(2, 'o')}:${time.minute.toString().padStart(2, '0')}"
}

object DateUtils {
    private val formateer = DateTimeFormatter.ofPattern(
        "d 'de' MMMM", Locale("es", "ES")
    )

    fun format(date: LocalDate): String {
        val date = java.time.LocalDate.of(
            date.year, date.monthNumber, date.dayOfMonth
        )
        return date.format(formateer)
    }
}