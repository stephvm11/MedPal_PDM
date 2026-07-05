package com.pdm0126.medpal.ui.screens.Appoinments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.browser.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.ui.components.AppScaffold
import com.pdm0126.medpal.ui.components.TopBarCases
import com.pdm0126.medpal.R.color
import com.pdm0126.medpal.ui.components.AddCard
import com.pdm0126.medpal.ui.components.ClosestAppoinment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentsHomeScreen(
    viewModel: AppointmentViewModel = viewModel(factory = AppointmentViewModel.Factory),
    onLogout: () -> Unit,
) {
    val appointments by viewModel.appointments.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsState()
    val refresh by viewModel.refreshing.collectAsState()

    val nextAppointment =
        appointments.filter { it.date >= getCurrentDate() }.minByOrNull { it.date }

    AppScaffold(
        DateUtils.format(getCurrentDate()),
        TopBarCases.HOME,
        {},
        { onLogout() }) { paddingValues ->

        if (error != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${error}")
                Button(
                    onClick = { viewModel.refreshAppointments() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            color.rosy_brown
                        )
                    )
                ) {
                    Text(
                        text = "Reintentar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        PullToRefreshBox(
            isRefreshing = refresh,
            onRefresh = { viewModel.refreshAppointments() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (appointments.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ClosestAppoinment(nextAppointment)
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
                            AddCard(onAddClick = {})
                            Spacer(modifier = Modifier.width(8.dp))
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
                                        text = "No tienes nada por el momento.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Toca para agregar.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
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
                            AddCard(onAddClick = {})
                            Spacer(modifier = Modifier.width(8.dp))
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
                                        text = "No tienes nada por el momento.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Toca para agregar.",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
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