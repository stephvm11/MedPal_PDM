package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R

@Composable
fun AddCard(onAddClick: () -> Unit) {
    OutlinedCard(onClick = onAddClick, modifier = Modifier.padding(start = 16.dp)
        .size(100.dp, 200.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = colorResource(R.color.moss_green)),
        border = BorderStroke(width = 3.dp, colorResource(R.color.beige)),
        shape = CardDefaults.outlinedShape
    ) {
        IconButton(onClick = onAddClick, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar cita",
                tint = colorResource(R.color.beige),
                modifier = Modifier.size(50.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddPre(){
    AddCard(onAddClick = {})
}
