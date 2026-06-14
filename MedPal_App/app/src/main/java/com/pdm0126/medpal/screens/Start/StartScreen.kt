package com.pdm0126.medpal.screens.Start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pdm0126.medpal.R

@Composable
fun StartScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(220.dp).
            aspectRatio(1f).
            clip(RoundedCornerShape(24.dp)),
            model = R.drawable.logo_home,
            contentDescription = "Logo de la app"
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "MedPal",
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.phthalo_green)
        )

    }
}