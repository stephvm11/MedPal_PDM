package com.pdm0126.medpal.data.notifications

import com.pdm0126.medpal.data.model.AlertData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

object AlertGlobalEvent {
    private val _events = MutableSharedFlow<AlertData>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _confirmations = Channel<Long>(Channel.BUFFERED)
    val confirmations = _confirmations.receiveAsFlow()

    private val _appointmentConfirmations = Channel<Long>(Channel.BUFFERED)
    val appointmentConfirmations = _appointmentConfirmations.receiveAsFlow()

    private val _examConfirmations = Channel<Long>(Channel.BUFFERED)
    val examConfirmations = _examConfirmations.receiveAsFlow()

    fun triggerAlert(alert: AlertData) {
        _events.tryEmit(alert)
    }

    fun hasActiveSubscribers(): Boolean {
        return _events.subscriptionCount.value > 0
    }

    fun confirmMedicationTake(reminderId: Long) {
        _confirmations.trySend(reminderId)
    }

    fun confirmAppointment(reminderId: Long) {
        _appointmentConfirmations.trySend(reminderId)
    }

    fun confirmExam(reminderId: Long) {
        _examConfirmations.trySend(reminderId)
    }
}