package com.pdm0126.medpal.ui.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.R.color
import com.pdm0126.medpal.ui.screens.Appoinments.getCurrentDate
import com.pdm0126.medpal.ui.screens.Appoinments.getDaysBetween

@Composable
fun ClosestAppoinment(
    appointment: Appointment?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = CutCornerShape(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.1f to colorResource(color.midnight_green),
                            0.3f to colorResource(color.midnight_green).copy(alpha = 0.9f),
                            0.6f to colorResource(color.midnight_green).copy(alpha = 0.7f),
                            0.8f to colorResource(color.midnight_green).copy(alpha = 0.5f),
                            0.9f to colorResource(color.midnight_green).copy(alpha = 0.3f),
                            1.0f to MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (appointment != null){
                    Text(
                        text = appointment.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    val daysRemaining = getDaysBetween(getCurrentDate(), appointment.date)
                    Text(
                        text = when {
                            daysRemaining == 0 -> "¡Hoy es tu cita médica!"
                            daysRemaining == 1 -> "Mañana tienes tu cita médica"
                            else -> "En $daysRemaining días"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(color.rosy_brown).copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "No tienes citas próximas",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}