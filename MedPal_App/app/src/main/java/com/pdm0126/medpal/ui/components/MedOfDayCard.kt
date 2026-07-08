package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun MedOfDayCard(
    name: String,
    dose: String,
    hour: String,
    isTakenToday: Boolean,
    onMarkTaken: () -> Unit,
    modifier: Modifier = Modifier
){
    val containerColor = if (isTakenToday) colorResource(R.color.midnight_green) else colorResource(R.color.rosy_brown)
    val contentColor = Color.White

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        modifier = modifier.size(width = 160.dp, height = 160.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = if(isTakenToday) Icons.Default.SentimentSatisfiedAlt else Icons.Default.SentimentDissatisfied,
                contentDescription = "State of the medication intake",
                tint = contentColor,
                modifier = Modifier.size(56.dp).padding(top = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    color =  contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dose,
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, contentColor, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ){
                    Text(
                        text = hour,
                        color =  contentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isTakenToday) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = "Marcar estado",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp).clickable{ onMarkTaken()}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicamentoPendientePreview(){
    Box(modifier = Modifier.padding(16.dp)){
        MedOfDayCard(
            name = "Ibuprofeno",
            dose = "400mg",
            hour = "08:00 AM",
            isTakenToday = false,
            onMarkTaken = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MedicamentoTomamdoPreview(){
    Box(modifier = Modifier.padding(16.dp)){
        MedOfDayCard(
            name = "Ibuprofeno",
            dose = "400mg",
            hour = "08:00 AM",
            isTakenToday = true,
            onMarkTaken = {}
        )
    }
}
