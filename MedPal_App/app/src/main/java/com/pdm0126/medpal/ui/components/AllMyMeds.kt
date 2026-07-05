package com.pdm0126.medpal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.pdm0126.medpal.data.model.AllMedItem
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items

@Composable
fun AllMyMeds(
    meds: List<AllMedItem>
){

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ expanded = !expanded}
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ver todos mis medicamentos",
                fontSize = 18.sp
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Colapsar" else "Expandir"
            )

            AnimatedVisibility(visible = expanded) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meds) { item ->
                        MedGeneralCard(
                            name = item.name,
                            hour = item.time
                        )

                    }
                }

            }

        }

    }

}