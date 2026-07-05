package com.pdm0126.medpal.data

import android.content.Context
import com.pdm0126.medpal.data.local.AppDatabase
import com.pdm0126.medpal.data.local.database.AppDataBase
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepository
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepository
import com.pdm0126.medpal.data.repositories.repositoryMedication.MedicationRepositoryImpl
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentOfflineFirstRepository
import com.pdm0126.medpal.data.repositories.repositoryOfflineFirst.Appointment.AppointmentOfflineFirstRepositoryImpl
import com.pdm0126.medpal.data.session.SessionManager
import kotlinx.coroutines.flow.first

class AppProvider(context: Context){
    private val sessionManager = SessionManager(context)
    suspend fun loadSavedSession(){
        val savedToken = sessionManager.accessToken.first()
        if(savedToken != null ){
            KtorClient.accessToken = savedToken
        }
    }

    private val dataBase: AppDataBase by lazy { AppDatabase.getDatabase(context) }
    private val authRepository: AuthRepository = AuthRepositoryImpl(
        sessionManager = sessionManager,
        userDao = database.userDao())
    private val appointmentDao = appDatabase.appointmentDao()





    private val appointmentOfflineFirstRepository: AppointmentOfflineFirstRepository =
        AppointmentOfflineFirstRepositoryImpl(appointmentDao)

    private val database: AppDataBase by lazy {
        AppDataBase.getDatabase(context)
    }



    private val medicationRepository: MedicationRepository = MedicationRepositoryImpl(
        medicationDao = database.medicationDao(),
        reminderDao = database.medicationReminderDao(),
        administrationRouteDao = database.administrationRouteDao()
    )
    fun provideAuthRepository(): AuthRepository {
        return authRepository
    }

    fun provideMedicationRepository(): MedicationRepository{
        return medicationRepository
    }
    fun provideAppointmentOfflineFirstRepositoy(): AppointmentOfflineFirstRepository{
        return appointmentOfflineFirstRepository
    }

}