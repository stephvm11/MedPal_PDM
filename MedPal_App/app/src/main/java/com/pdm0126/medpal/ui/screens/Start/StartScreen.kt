package com.pdm0126.medpal.ui.screens.Start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R

@Composable
fun StartScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .background(colorResource(R.color.midnight_green)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.medpal_logo),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, bottom = 10.dp, end = 35.dp),
            contentDescription = "Medpal logo"
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "¡Bienvenido!",
            fontFamily = FontFamily.SansSerif,
            style = MaterialTheme.typography.headlineSmall,
            color = colorResource(R.color.beige),
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Toca para ingresar",
            fontFamily = FontFamily.Default,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(R.color.beige),
        )

    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    StartScreen(onClick = {})
}