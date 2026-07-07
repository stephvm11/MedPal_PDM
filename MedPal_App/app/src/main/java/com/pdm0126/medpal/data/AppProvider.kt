package com.pdm0126.medpal.data

import android.content.Context
import android.util.Log
import com.pdm0126.medpal.data.local.database.AppDataBase
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.remote.api.SupabaseClient
import com.pdm0126.medpal.data.repositories.repositoryAddAppointment.AddAppointmentRepository
import com.pdm0126.medpal.data.repositories.repositoryAddAppointment.AddAppointmentRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryAddExam.AddExamRepository
import com.pdm0126.medpal.data.repositories.repositoryAddExam.AddExamRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryAddMed.AddMedRepository
import com.pdm0126.medpal.data.repositories.repositoryAddMed.AddMedRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepository
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryExam.ExamRepository
import com.pdm0126.medpal.data.repositories.repositoryExam.ExamRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepository
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepository
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentRepositoryImpl
import com.pdm0126.medpal.data.session.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.first

class AppProvider(context: Context){
    private val sessionManager = SessionManager(context)
    suspend fun loadSavedSession(){
        val savedToken = sessionManager.accessToken.first()
        val savedRefresh = sessionManager.resfreshToken.first()
        if(savedToken != null && savedRefresh != null ){
            KtorClient.accessToken = savedToken
            try {
                val userSession = UserSession(
                    accessToken = savedToken,
                    refreshToken = savedRefresh,
                    expiresIn = 3600L,
                    tokenType = "bearer",
                    user = null
                )

                SupabaseClient.client.auth.importSession(session = userSession)

            } catch (e: Exception) {
                Log.e("MEDPAL_DEBUG", "Error al restaurar sesión en Supabase", e)
            }
        }
    }

    private val database: AppDataBase by lazy {
        AppDataBase.getDatabase(context)
    }
    private val authRepository: AuthRepository = AuthRepositoryImpl(
        sessionManager = sessionManager,
        userDao = database.userDao()
    )
    private val appointmentRepository: AppointmentRepository =
        AppointmentRepositoryImpl(
            appointmentDao = database.appointmentDao()
        )
    private val addAppointmentRepository: AddAppointmentRepository = AddAppointmentRepositoryImpl(
        appointmentDao = database.appointmentDao(),
        appointmentReminderDao = database.appointmentReminderDao()
    )
    private val medicationRepository: MedicationRepository = MedicationRepositoryImpl(
        medicationDao = database.medicationDao(),
        reminderDao = database.medicationReminderDao(),
        administrationRouteDao = database.administrationRouteDao()
    )

    private val addMedRepository: AddMedRepository = AddMedRepositoryImpl(
        administrationRouteDao = database.administrationRouteDao(),
        medicationDao = database.medicationDao(),
        medicationReminderDao = database.medicationReminderDao()
    )

    private val addExamRepository: AddExamRepository = AddExamRepositoryImpl(
        examDao = database.examDao(),
        appointmentReminderDao = database.appointmentReminderDao(),
        appointmentDao = database.appointmentDao()
    )

    private val examRepository: ExamRepository = ExamRepositoryImpl(
        examDao = database.examDao()
    )

    fun provideAuthRepository(): AuthRepository {
        return authRepository
    }
    fun provideMedicationRepository(): MedicationRepository {
        return medicationRepository
    }
    fun provideAppointmentRepository(): AppointmentRepository {
        return appointmentRepository
    }
    fun provideAddAppointmentRepository(): AddAppointmentRepository {
        return addAppointmentRepository
    }
    fun provideAddMedRepository(): AddMedRepository {
        return addMedRepository
    }
    fun provideExamRepository(): ExamRepository {
        return examRepository
    }
    fun provideAddExamRepository(): AddExamRepository {
        return addExamRepository
    }

}