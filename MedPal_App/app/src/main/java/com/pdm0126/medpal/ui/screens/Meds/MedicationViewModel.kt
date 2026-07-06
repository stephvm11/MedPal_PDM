package com.pdm0126.medpal.ui.screens.Meds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.data.model.AllMedItem
import com.pdm0126.medpal.data.model.MedDay
import com.pdm0126.medpal.data.model.MedGeneral
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.MedPalApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.collections.forEach

class MedicationViewModel(
    private val repository: MedicationRepository,
    private val userId: Long
) : ViewModel() {

    private val _generalMedList = MutableStateFlow(MedGeneral())
    val generalMedList = _generalMedList.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    init {
        loadData()
        refreshFromServer()
    }

    private fun loadData() {
        _generalMedList.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            repository.getMedicationsWithReminders(userId).collect { medicationsWithReminders ->

                val dailyItems = mutableListOf<MedDay>()
                val allItems = mutableListOf<AllMedItem>()

                val today = LocalDate.now()

                medicationsWithReminders.forEach { relation ->

                    val med = relation.medication

                    if (relation.reminders.isEmpty()) {
                        allItems.add(
                            AllMedItem(
                                reminderId = -1L,
                                name = med.name,
                                dosage = med.dosage,
                                daysRemaining = -1,
                                time = "--:--"
                            )
                        )
                    } else {
                        relation.reminders.forEach { reminder ->

                            val lastDoseDateTime = reminder.lastDose?.let {
                                try {
                                    LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            val lastDoseDate = lastDoseDateTime?.toLocalDate()

                            val localTime = try {
                                LocalTime.parse(reminder.time.take(5))
                            } catch (e: Exception) {
                                null
                            }


                            val isTakenToday = if (lastDoseDate?.isEqual(today) == true) {
                                if (lastDoseDateTime != null && localTime != null) {
                                    !(lastDoseDateTime.toLocalTime().hour == localTime.hour &&
                                            lastDoseDateTime.toLocalTime().minute == localTime.minute)
                                } else {
                                    true
                                }
                            } else {
                                false
                            }

                            val displayTime = if (isTakenToday && lastDoseDateTime != null) {
                                lastDoseDateTime.toLocalTime()
                                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                            } else {
                                reminder.time.take(5)
                            }

                            val isScheduledForToday = if (lastDoseDate == null) {
                                true
                            } else {
                                val daysSinceLastDose = ChronoUnit.DAYS.between(lastDoseDate, today)
                                daysSinceLastDose % reminder.frequencyDays == 0L
                            }

                            if (isScheduledForToday) {
                                dailyItems.add(
                                    MedDay(
                                        reminderId = reminder.id,
                                        name = med.name,
                                        dosage = med.dosage,
                                        time = displayTime,
                                        isTaken = isTakenToday
                                    )
                                )
                            } else {
                                val daysRemaining = if (lastDoseDate == null) {
                                    0
                                } else {
                                    val daysSinceLastDose =
                                        ChronoUnit.DAYS.between(lastDoseDate, today)
                                    val daysIntoCurrentCycle =
                                        daysSinceLastDose % reminder.frequencyDays
                                    (reminder.frequencyDays - daysIntoCurrentCycle).toInt()
                                }

                                allItems.add(
                                    AllMedItem(
                                        reminderId = reminder.id,
                                        name = med.name,
                                        dosage = med.dosage,
                                        daysRemaining = daysRemaining,
                                        time = displayTime
                                    )
                                )
                            }
                        }
                    }
                }

                _generalMedList.update {
                    it.copy(
                        isLoading = false,
                        dailyMedications = dailyItems,
                        allMedications = allItems
                    )
                }
            }
        }
    }

    fun refreshFromServer() {
        viewModelScope.launch {
            repository.refresh(userId).onFailure { error ->
                android.util.Log.e("MEDPAL_DEBUG", "¡La descarga de Supabase falló!", error)
                _event.emit("Error de sincronización: ${error.localizedMessage}")
            }
        }
    }

    fun toggleTakeStatus(reminderId: Long) {
        viewModelScope.launch {
            val currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

            repository.updateLastDose(reminderId, currentTimestamp)
                .onSuccess {
                    _event.emit("Medicamento registrado localmente")
                }
                .onFailure { error ->
                    _event.emit("Error local: ${error.localizedMessage}")
                }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication

                val medRepository = app.appProvider.provideMedicationRepository()
                val authRepository = app.appProvider.provideAuthRepository()

                val currentUserId = runBlocking {
                    authRepository.userId.first()?.toLongOrNull() ?: 0L
                }

                MedicationViewModel(
                    repository = medRepository,
                    userId = currentUserId
                )
            }
        }
    }
}
