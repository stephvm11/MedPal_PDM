package com.pdm0126.medpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pdm0126.medpal.navigation.MedPal_App
import com.pdm0126.medpal.ui.theme.MedPalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedPalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MedPal_App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

