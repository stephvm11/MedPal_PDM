package com.pdm0126.medpal.ui.screens.Profile

import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.pdm0126.medpal.ui.components.ImportantInfoModal
import com.pdm0126.medpal.ui.components.LogoutDialog
import com.pdm0126.medpal.ui.components.UserGuideModal

@Composable
fun ProfileScreen(
    onCloseClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState(initial = false)

    var isGuideVisible by remember{ mutableStateOf(false) }
    var isInfoVisible by remember{ mutableStateOf(false) }
    var isLogoutVisible by remember { mutableStateOf(false) }

    var isAnyModalOpen = isGuideVisible || isInfoVisible || isLogoutVisible || isSyncing

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.event.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        IconButton(
            onClick = { if (!isAnyModalOpen) onCloseClick()},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar perfil",
                tint = if (isAnyModalOpen) Color.LightGray else Color.DarkGray,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
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

            val syncButtonColors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.phthalo_green),
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.LightGray
            )

            Button(
                onClick = { isGuideVisible = true },
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape,
                enabled = !isAnyModalOpen
            ) {
                Text(text = "Guía de usuario", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = { viewModel.syncAppMedsAndAppointments(user?.id ?: 0L) },
                modifier = buttonModifier,
                colors = syncButtonColors,
                shape = buttonShape,
                enabled = !isAnyModalOpen
            ) {
                if (isSyncing) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.phthalo_green),
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Sincronizando...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.phthalo_green))
                    }
                } else {
                    Text(text = "Sincronizar Datos", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            Button(
                onClick = { isInfoVisible = true },
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape,
                enabled = !isAnyModalOpen
            ) {
                Text(
                    text = "Información\nImportante",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {isLogoutVisible = true},
                modifier = buttonModifier,
                colors = buttonColors,
                shape = buttonShape,
                enabled = !isAnyModalOpen
            ) {
                Text(text = "Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        UserGuideModal(
            isVisible = isGuideVisible,
            onCloseClick = { isGuideVisible = false }
        )

        ImportantInfoModal(
            isVisible = isInfoVisible,
            onCloseClick = { isInfoVisible = false }
        )

        LogoutDialog (
            isVisible = isLogoutVisible,
            onDismiss = { isLogoutVisible = false },
            onConfirm = {
                isLogoutVisible = false
                onLogoutClick()
            }
        )
    }
}