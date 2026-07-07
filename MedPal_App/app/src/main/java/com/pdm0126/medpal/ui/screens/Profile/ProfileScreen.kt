package com.pdm0126.medpal.ui.screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.R
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun ProfileScreen(
    onCloseClick: () -> Unit,
    onNavigateToGuide: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onSyncData: () -> Unit = {},
    onNavigateToInfo: () -> Unit = {},
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar perfil",
                tint = Color.DarkGray,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = colorResource(id = R.color.midnight_green),
                modifier = Modifier.size(140.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user?.firstName ?: "Cargando...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = user?.lastName ?: "..",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val buttonModifier = Modifier
                .fillMaxWidth(0.75f)
                .heightIn(min = 48.dp)

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.phthalo_green),
                    contentColor = Color.White
            )
            val buttonShape = RoundedCornerShape(24.dp)

            Button(
                onClick = onNavigateToGuide,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape
            ) {
                Text(text = "Guía de usuario", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = onNavigateToSettings,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape
            ) {
                Text(text = "Configuración", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = onSyncData,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape
            ) {
                Text(text = "Sincronizar Datos", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = onNavigateToInfo,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape
            ) {
                Text(
                    text = "Información\nImportante",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = onLogoutClick,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape
            ) {
                Text(text = "Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}