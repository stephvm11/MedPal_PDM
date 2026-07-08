package com.pdm0126.medpal.ui.screens.Appoinments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.notifications.ReminderAlarmManager
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class AppointmentViewModel(
    private val appointmentRepository: AppointmentRepository,
    private val userId: Long
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing = _refreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    val appointments: StateFlow<List<Appointment>> =
        appointmentRepository.getAppointmentsWithReminders(userId)
            .map { withReminders ->
                withReminders.map { it.appointment.toModel() }
                    .sortedWith(
                        compareBy(
                            { it.date < getCurrentDate() },
                            { it.status },
                            { it.date },
                            { it.time }
                        ))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val allAppointments: StateFlow<List<Appointment>> =
        appointmentRepository.getAppointmentsWithReminders(userId)
            .map { withReminders ->
                withReminders.map { it.appointment.toModel() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val nextAppointment: StateFlow<Appointment?> = appointments
        .map { list ->
            list.filter { it.date >= getCurrentDate() && !it.status }
                .minByOrNull { it.date }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            appointmentRepository.refresh(userId)
                .onFailure { error ->
                    _error.value = "Error al cargar citas: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun refreshAppointments(context: Context) {

        viewModelScope.launch {
            _refreshing.value = true
            _error.value = null

            appointmentRepository.refresh(userId)
                .onSuccess {
                    val appointmentsWithReminders = appointmentRepository.getAppointmentsWithRemindersNotifications(userId).first()

                    appointmentsWithReminders.forEach { relation ->
                        val appointment = relation.appointment

                        relation.reminders.forEach { reminder ->

                            val alarmDate = appointment.date.minus(reminder.startDay, DateTimeUnit.DAY)

                            val today = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                            if (alarmDate >= today) {
                                ReminderAlarmManager.scheduleAppointmentAlarm(
                                    context = context,
                                    appointmentId = appointment.id,
                                    title = "Recordatorio de Cita: ${appointment.title}",
                                    description = "Tu cita con ${appointment.specialist} en ${appointment.place} es el ${appointment.date}",
                                    hour = reminder.time.hour,
                                    minute = reminder.time.minute,
                                    startDate = alarmDate
                                )
                            }
                        }
                    }
                    _event.emit("¡Citas y recordatorios actualizados correctamente!")
                }
                .onFailure { error ->
                    _error.value = "Error al actualizar: ${error.message}"
                    _event.emit("Error de sincronización en citas.")
                }
            _refreshing.value = false
        }
    }

    fun completeAppointment(appointmentId: Long) {
        viewModelScope.launch {
            appointmentRepository.completeAppointment(appointmentId)
                .onSuccess { }
                .onFailure { error ->
                    _error.value = "Error al completar cita: ${error.message}"
                }
        }
    }

    fun uncompleteAppointment(appointmentId: Long) {
        viewModelScope.launch {
            appointmentRepository.uncompleteAppoinment(appointmentId)
                .onFailure { error ->
                    _error.value = "Error al desmarcar completación de cita: ${error.message}"
                }
        }
    }

    fun clearError() {
        _error.value = null
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication
                val authRepository = app.appProvider.provideAuthRepository()

                val userId = runBlocking {
                    authRepository.userId.first()?.toLongOrNull() ?: 0L
                }
                AppointmentViewModel(
                    app.appProvider.provideAppointmentRepository(),
                    userId = userId
                )
            }
        }
    }
}
