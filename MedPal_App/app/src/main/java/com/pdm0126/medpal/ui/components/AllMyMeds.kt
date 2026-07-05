package com.pdm0126.medpal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    meds: List<AllMedItem>,
    modifier: Modifier = Modifier
){

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {

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
        }
        AnimatedVisibility(visible = expanded) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Caben exactamente 3 por fila antes de bajar
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false, // Delega el scroll a la pantalla principal[cite: 3]
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp) // Evita colapsar y le permite expandirse hacia abajo[cite: 3]
                    .padding(top = 12.dp)
            ) {
                items(meds) { item ->
                    MedGeneralCard(
                        name = item.name,
                        hour = item.time,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

    }
}