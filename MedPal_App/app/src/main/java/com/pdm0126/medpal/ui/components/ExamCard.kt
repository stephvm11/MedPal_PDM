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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import com.pdm0126.medpal.data.model.Exam

@Composable
fun ExamCard(
    exam: Exam,
    onToggleComplete: (Long) -> Unit
) {
    val isDone = exam.status

    OutlinedCard(
        modifier = Modifier.size(200.dp, 200.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isDone) colorResource(R.color.moss_green).copy(alpha = 0.5f)
            else
                colorResource(R.color.moss_green)
        ),
        border = BorderStroke(
            width = 3.dp,
            color = if (isDone) colorResource(R.color.beige).copy(alpha = 0.5f) else colorResource(R.color.beige)
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
                    text = " De: Cita asociada",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
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
                    onClick = { onToggleComplete(exam.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.Filled.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                        contentDescription = if (isDone) "Marcar como pendiente" else "Marcar como completada",
                        tint = if (isDone) colorResource(R.color.beige).copy(alpha = 0.5f) else colorResource(
                            R.color.beige
                        ),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}