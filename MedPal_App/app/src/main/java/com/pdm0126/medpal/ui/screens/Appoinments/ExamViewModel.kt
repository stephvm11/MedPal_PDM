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
import com.pdm0126.medpal.data.model.Exam
import com.pdm0126.medpal.data.notifications.AlertGlobalEvent
import com.pdm0126.medpal.data.notifications.ReminderAlarmManager
import com.pdm0126.medpal.data.repositories.repositoryExam.ExamRepository
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import kotlin.time.Clock

class ExamViewModel(
    private val examRepository: ExamRepository,
    private val appointmentRepository: AppointmentRepository,
    private val userId: Long
) : ViewModel() {

    private val appointments: StateFlow<List<Appointment>> =
        appointmentRepository.getAppointmentsWithReminders(userId)
            .map { withReminders ->
                withReminders.map { it.appointment.toModel() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing = _refreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            AlertGlobalEvent.appointmentConfirmations.collect { examId ->
                completeExam(examId)
            }
        }
    }

    val exams: StateFlow<List<Exam>> = combine(
        examRepository.getExamsWithReminders(userId)
            .map { list -> list.map { it.exam.toModel() } },
        appointments
    ) { examList, appointmentList ->
        val appointmentMap = appointmentList.associateBy { it.id }
        val today = getCurrentDate()
        examList.sortedWith(
            compareBy(
                { exam ->
                    val date = appointmentMap[exam.appointmentId]?.date
                    date != null && date < today
                },
                { it.status },
                { exam -> appointmentMap[exam.appointmentId]?.date ?: LocalDate.MAX }
            ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val nextExam: StateFlow<Exam?> = exams.map { list ->
        list.filter { exam ->
            val date = appointments.value.find { it.id == exam.appointmentId }?.date
            !exam.status && (date == null || date >= getCurrentDate())
        }.firstOrNull()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        loadExams()
    }

    fun loadExams() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            examRepository.refresh(userId)
                .onFailure { error ->
                    _error.value = "Error al cargar citas: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun refreshExams(context: Context) {
        viewModelScope.launch {
            _refreshing.value = true
            _error.value = null

            examRepository.refresh(userId)
                .onSuccess {
                    val examsWithReminders = examRepository.getExamsWithRemindersNotifications(userId).first()
                    val appointmentMap = appointments.value.associateBy { it.id }

                    examsWithReminders.forEach { relation ->
                        val exam = relation.exam
                        val associatedAppointment = appointmentMap[exam.appointmentId]

                        if (associatedAppointment != null) {
                            relation.reminders.forEach { reminder ->
                                val alarmDate = associatedAppointment.date.minus(reminder.startDay, DateTimeUnit.DAY)

                                val scheduledAlarmDateTime = LocalDateTime(
                                    year = alarmDate.year,
                                    month = alarmDate.monthNumber,
                                    day = alarmDate.dayOfMonth,
                                    hour = reminder.time.hour,
                                    minute = reminder.time.minute
                                )
                                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                                if (scheduledAlarmDateTime >= now) {
                                    ReminderAlarmManager.scheduleExamAlarm(
                                        context = context,
                                        examId = exam.id,
                                        title = "Recordatorio de Examen: ${exam.title}",
                                        description = "Tu examen en ${exam.place} está programado junto a tu cita ${associatedAppointment.title} del ${associatedAppointment.date}",
                                        hour = reminder.time.hour,
                                        minute = reminder.time.minute,
                                        examDate = associatedAppointment.date,
                                        daysBefore = reminder.startDay,
                                        frequency = reminder.frequency
                                    )
                                }
                            }
                        }
                    }
                    _event.emit("¡Exámenes y recordatorios sincronizados!")
                }
                .onFailure { error ->
                    _error.value = "Error al actualizar: ${error.message}"
                    _event.emit("Error de sincronización en exámenes.")
                }
            _refreshing.value = false
        }
    }

    fun completeExam(examId: Long) {
        viewModelScope.launch {
            examRepository.completeExam(examId)
                .onSuccess { }
                .onFailure { error ->
                    _error.value = "Error al completar examen: ${error.message}"
                }
        }
    }

    fun uncompleteExam(examId: Long) {
        viewModelScope.launch {
            examRepository.uncompleteExam(examId)
                .onFailure { error ->
                    _error.value = "Error al desmarcar completación de examen: ${error.message}"
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
                ExamViewModel(
                    app.appProvider.provideExamRepository(),
                    appointmentRepository = app.appProvider.provideAppointmentRepository(),
                    userId = userId
                )
            }
        }
    }

}