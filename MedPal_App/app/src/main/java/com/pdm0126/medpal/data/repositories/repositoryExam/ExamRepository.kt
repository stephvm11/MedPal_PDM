package com.pdm0126.medpal.data.repositories.repositoryExam

import com.pdm0126.medpal.data.local.database.entities.ExamWithReminders
import kotlinx.coroutines.flow.Flow

interface ExamRepository {

    fun getExamsWithRemindersNotifications(userId: Long): Flow<List<ExamWithReminders>>
    fun getExamsWithReminders(userId: Long): Flow<List<ExamWithReminders>>

    fun getPendingExamsWithReminders(userId: Long): Flow<List<ExamWithReminders>>

    suspend fun refresh(userId: Long): Result<Unit>

    suspend fun completeExam(examId: Long): Result<Unit>

    suspend fun uncompleteExam(examId: Long): Result<Unit>
}