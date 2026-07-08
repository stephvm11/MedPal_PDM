package com.pdm0126.medpal.data.repositories.repositorySync

import com.pdm0126.medpal.data.repositories.repositoryExam.ExamRepository
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepository
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepository
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SyncRepositoryImpl(
    private val appointmentRepository: AppointmentRepository,
    private val examRepository: ExamRepository,
    private val medicationRepository: MedicationRepository
) : SyncRepository {

    private val _isSyncing = MutableStateFlow(false)
    override val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    override suspend fun syncAllData(userId: Long): Result<Unit> {
        return try {
            _isSyncing.value = true

            appointmentRepository.refresh(userId).getOrThrow()

            examRepository.refresh(userId).getOrThrow()

            medicationRepository.refresh(userId).getOrThrow()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isSyncing.value = false
        }
    }
}

