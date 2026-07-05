package com.pdm0126.medpal.ui.screens.Appoinments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentOfflineFirstRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class AppointmentViewModel(
    private val appointmentOfflineFirstRepository: AppointmentOfflineFirstRepository
) : ViewModel() {
    val appointments: StateFlow<List<Appointment>> =
        appointmentOfflineFirstRepository.getAppointments()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    private val _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing = _refreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        refreshAppointments()
    }

    fun refreshAppointments() {
        viewModelScope.launch {
            _error.value = null
            _refreshing.value = true

            appointmentOfflineFirstRepository.refresh()
                .onSuccess {
                }
                .onFailure { error ->
                    _error.value = "Error: ${error}"
                }
            _refreshing.value = false
        }
    }

    fun addAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        userId: Int
    ) {
        viewModelScope.launch {
            appointmentOfflineFirstRepository.createAppointment(
                title = title,
                specialist = specialist,
                place = place,
                date = date,
                time = time,
                userId = userId
            )
        }
    }

    fun DeleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentOfflineFirstRepository.deleteAppointment(appointment.id)
        }
    }

    fun UpdateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentOfflineFirstRepository.updateAppointment(
                appointment.id,
                appointment.title,
                appointment.specialist,
                appointment.place,
                appointment.date,
                appointment.time,
                appointment.userId
            )
        }

    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication
                AppointmentViewModel(app.appProvider.provideAppointmentOfflineFirstRepositoy())
            }
        }
    }
}
