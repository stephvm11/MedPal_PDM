package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import com.pdm0126.medpal.data.model.Appointment
import com.pdm0126.medpal.data.model.Exam
import com.pdm0126.medpal.ui.screens.Appoinments.getCurrentDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun ExamCard(
    exam: Exam,
    associatedAppointment: Appointment?,
    onToggleComplete: (Long) -> Unit
) {
    val isDone = exam.status
    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

    val isPast = if (associatedAppointment != null){
        val examDateTime = LocalDateTime(
            year = associatedAppointment.date.year,
            month = associatedAppointment.date.month,
            day = associatedAppointment.date.day,
            hour = associatedAppointment.time.hour,
            minute = associatedAppointment.time.minute
        )
        examDateTime < now
    }else{
        false
    }
    val isExpired = isPast && !isDone

    OutlinedCard(
        modifier = Modifier.size(200.dp, 200.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = when {
                isPast -> colorResource(R.color.rosy_brown).copy(alpha = 0.5f)
                isDone -> colorResource(R.color.moss_green).copy(alpha = 0.5f)
                else -> colorResource(R.color.moss_green)
            }
        ),
        border = BorderStroke(
            width = 3.dp,
            color = when {
                isPast -> colorResource(R.color.beige).copy(alpha = 0.5f)
                isDone -> colorResource(R.color.beige).copy(alpha = 0.5f)
                else -> colorResource(R.color.beige)
            }
        ),
        shape = CardDefaults.outlinedShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = exam.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.beige),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "De cita: ${associatedAppointment?.title?: ""}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    thickness = 1.dp,
                    color = colorResource(R.color.beige)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = colorResource(R.color.beige),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = exam.place,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))

                IconButton(
                    onClick = {
                        if (!isPast) {
                            onToggleComplete(exam.id)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End),
                    enabled = !isPast
                ) {
                    when {
                        isExpired -> {
                            Icon(
                                imageVector = Icons.Outlined.DisabledByDefault,
                                contentDescription = "Examen vencida sin completar",
                                tint = colorResource(R.color.beige).copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        isDone -> {
                            Icon(
                                imageVector = Icons.Filled.CheckBox,
                                contentDescription = "Examen completado",
                                tint = colorResource(R.color.beige).copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                                contentDescription = "Examen sin completar",
                                tint = colorResource(R.color.beige),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}