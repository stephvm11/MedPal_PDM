package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.pdm0126.medpal.R


@Composable
fun MedGeneralCard(
    name: String,
    dosage: String,
    daysRemaining: Int,
    time: String,
    modifier: Modifier = Modifier
){
    val remaining = when (daysRemaining) {
        0 -> "Toca hoy"
        1 -> "Toca mañana"
        else -> "Faltan $daysRemaining días"
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.moss_green)
        ),
        modifier = modifier.height(145.dp)
    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, start = 14.dp, end = 14.dp, bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = "Medicamento general",
                tint = Color.White,
                modifier = Modifier.size(33.dp)
            )

            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.midnight_green),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Dosis: $dosage",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.midnight_green),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$remaining | $time ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.midnight_green),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}