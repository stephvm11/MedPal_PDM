package com.pdm0126.medpal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserGuideModal(
    isVisible: Boolean,
    onCloseClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
        ) {
            Text(
                text = "Guía de usuario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar guía",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¡Bienvenido a MedPal!\nTu compañero inteligente para el cuidado de tu salud. Esta aplicación ha sido diseñada con el objetivo de ayudarte a organizar, gestionar y recordar de manera exacta todas tus tomas de medicamentos, examenesle pendientes y citas médicas pendientes.",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )

                Text(
                    text = "¿Qué puedes hacer en MedPal?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "• Control de Medicación: Registra tus tratamientos, asigna dosis específicas y define los intervalos de tiempo exactos en los que necesitas tomarlos.\n\n" +
                            "• Gestión de Citas Médicas: Añade tus próximas consultas especificando el título, lugar y el especialista que te atenderá. Puedes programar alertas previas para prepararte a tiempo.\n\n" +
                            "• Control de Exámenes Médicos: Agrega las órdenes de laboratorio que necesites realizarte y asócialas a su respectiva cita médica. Define recordatorios automáticos con días de anticipación para que nunca olvides ir a tomarte las muestras.\n\n" +
                            "• Alertas en Tiempo Real: Olvídate de los retrasos. MedPal generará notificaciones activas y alarmas en tu dispositivo en el minuto exacto en que corresponda tu dosis.\n\n" +
                            "• Sincronización Inteligente: Tus datos se guardan de forma segura en la nube y en tu base de datos local de manera simultánea.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}