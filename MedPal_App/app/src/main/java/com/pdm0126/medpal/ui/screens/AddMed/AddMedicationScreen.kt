package com.pdm0126.medpal.ui.screens.AddMed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm0126.medpal.R
import com.pdm0126.medpal.ui.components.FormDatePicker
import com.pdm0126.medpal.ui.components.FormTextField
import kotlin.time.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.pdm0126.medpal.ui.components.FormRoutePicker
import com.pdm0126.medpal.ui.components.FormTimePicker
import com.pdm0126.medpal.ui.components.FrequencyChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var administrationRoute by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var isReminderEnabled by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("Diario") }

    var startDate by remember { mutableStateOf(now.date) }
    var reminderTime by remember { mutableStateOf(LocalTime(8, 0)) }

    val routesOptions = listOf("Oral", "Cutánea", "Inhalatoria", "Intravenosa", "Oftálmica") // Ejemplo estático tempora

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                actions = {
                    Button(
                        onClick = { /* TODO: Guardar medicamento con repositorio */ },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.midnight_green)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Guardar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                value = name,
                onValueChange = {name = it},
                label = "Nombre del medicamento"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)){
                    FormTextField(
                        value = dose,
                        onValueChange = {dose = it},
                        label = "Dosis",
                        placeholder = "Ej. 400 mg"
                    )
                }
                Box(modifier = Modifier.weight(1.2f)){
                    FormRoutePicker(
                        options = routesOptions,
                        selectedOption = administrationRoute,
                        onOptionSelected = { administrationRoute = it },
                        label = "Vía"
                    )
                }
            }

            FormDatePicker(
                value = startDate,
                onValueChange = {startDate = it},
                label = "Fecha de inicio"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = if (isReminderEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                    contentDescription = null,
                    tint = if (isReminderEnabled) colorResource(R.color.rosy_brown) else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )

                Button(
                    onClick = {isReminderEnabled = !isReminderEnabled},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReminderEnabled) Color.Transparent else Color.LightGray,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50.dp),
                    border = if (isReminderEnabled) ButtonDefaults.outlinedButtonBorder else null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isReminderEnabled) "Recordatorio" else "Agregar recordatorio",
                        fontSize = 16.sp
                    )
                }

            }

            AnimatedVisibility(visible = isReminderEnabled) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FormTimePicker(
                        value = reminderTime,
                        onValueChange = { reminderTime = it },
                        label = "Hora de recordatorio"
                    )

                    Text(
                        text = "Frecuencia",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FrequencyChip("Diario", selectedFrequency) { selectedFrequency = it }
                            FrequencyChip("Semanal", selectedFrequency) { selectedFrequency = it }
                            FrequencyChip("Quincenal", selectedFrequency) { selectedFrequency = it }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FrequencyChip("Mensual", selectedFrequency) { selectedFrequency = it }
                            FrequencyChip("Personalizado", selectedFrequency) { selectedFrequency = it }
                        }
                    }
                }
            }
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Nota") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.rosy_brown),
                    unfocusedBorderColor = colorResource(R.color.midnight_green).copy(alpha = 0.3f)
                )
            )

        }
    }
}