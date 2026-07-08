package com.pdm0126.medpal.ui.screens.Appoinments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.data.local.database.entities.toModel
import com.pdm0126.medpal.data.model.Exam
import com.pdm0126.medpal.data.repositories.repositoryExam.ExamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExamViewModel(
    private val examRepository: ExamRepository,
    private val userId: Long
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing = _refreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val exams: StateFlow<List<Exam>> =
        examRepository.getExamsWithReminders(userId)
            .map { examWithReminders ->
                examWithReminders.map { it.exam.toModel() }
                    .sortedWith(
                        compareBy(
                            { it.status }
                        ))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
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

    fun refreshExams() {
        viewModelScope.launch {
            _refreshing.value = true
            _error.value = null

            examRepository.refresh(userId)
                .onFailure { error ->
                    _error.value = "Error al actualizar: ${error.message}"
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
                    userId = userId
                )
            }
        }
    }

}