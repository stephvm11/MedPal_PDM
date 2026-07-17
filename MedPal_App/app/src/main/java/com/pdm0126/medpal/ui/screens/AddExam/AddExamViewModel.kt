package com.pdm0126.medpal.ui.screens.AddExam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.data.model.FrequencyReminder
import com.pdm0126.medpal.data.repositories.repositoryAddExam.AddExamRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.MedPalApplication
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalTime

class AddExamViewModel(
    private val repository: AddExamRepository,
    private val userId: Long
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    fun loadAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUserAppointments(userId)
                .onSuccess { appointments ->
                    _appointments.value = appointments
                }
                .onFailure { error ->
                    _error.value = "Error al cargar citas: ${error.message}"
                }

            _isLoading.value = false
        }
    }

    fun createExam(
        title: String,
        place: String,
        apointmentId: Long,
        hasReminder: Boolean = false,
        reminderTime: LocalTime? = null,
        frequency: FrequencyReminder? = null,
        startDay: Int? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.createExam(
                title = title,
                place = place,
                appointmentId = apointmentId,
            )

            result.onSuccess { examId ->
                if (hasReminder && reminderTime != null && frequency != null && startDay != null) {
                    delay(300)

                    repository.createReminder(
                        appointmentId = null,
                        examId = examId,
                        time = reminderTime,
                        frequencyDays = frequency.name,
                        daysBefore = startDay
                    ).onSuccess {
                        _event.send("Examen y recordatorio creados exitosamente")
                    }.onFailure { error ->
                        _event.send("Examen creado pero error al crear recordatorio: ${error.message}")
                    }
                } else {
                    _event.send("Examen creado exitosamente")
                }
                _isLoading.value = false
                onSuccess()
            }.onFailure { error ->
                _event.send("Error al crear examen: ${error.message}")
                _isLoading.value = false
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication
                val authRepository = app.appProvider.provideAuthRepository()

                val currentUserId = runBlocking {
                    authRepository.userId.first()?.toLongOrNull() ?: 0L
                }

                AddExamViewModel(
                    repository = app.appProvider.provideAddExamRepository(),
                    userId = currentUserId
                )
            }
        }
    }
}
