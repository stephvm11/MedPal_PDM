package com.pdm0126.medpal.ui.screens.AddAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import com.pdm0126.medpal.data.model.FrequencyReminder
import com.pdm0126.medpal.data.repositories.repositoryAddAppointment.AddAppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

class AddAppointmentViewModel(
    private val repository: AddAppointmentRepository,
    private val userId: Long
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _event = MutableStateFlow<String>("")
    val event = _event.asSharedFlow()

    fun createAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        hasReminder: Boolean = false,
        reminderTime: LocalTime? = null,
        frequency: FrequencyReminder? = null,
        startDay: Int? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = repository.createAppointment(
                title = title,
                specialist = specialist,
                place = place,
                date = date.toString(),
                time = time.toString(),
                userId = userId.toInt()
            )

            result.onSuccess { appointmentId ->
                if (hasReminder && reminderTime != null && frequency != null && startDay != null) {
                    val startDate = date.minus(startDay.toLong(), DateTimeUnit.DAY)

                    repository.createReminder(
                        appointmentId = appointmentId,
                        examId = null,
                        startDay = startDate,
                        time = reminderTime,
                        frequencyDays = frequency.getDays() ?: 1,
                        daysBefore = startDay
                    ).onSuccess {
                        _event.emit("Cita y recordatorio creados exitosamente")
                    }.onFailure { error ->
                        _event.emit("Cita creada, pero error al crear recordatorio: ${error.message}")
                    }
                } else {
                    _event.emit("Cita creada exitosamente")
                }
                _isLoading.value = false
                onSuccess()
            }.onFailure { error ->
                _event.emit("Error al crear cita: ${error.message}")
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

                AddAppointmentViewModel(
                    repository = app.appProvider.provideAddAppointmentRepository(),
                    userId = currentUserId
                )
            }
        }
    }
}