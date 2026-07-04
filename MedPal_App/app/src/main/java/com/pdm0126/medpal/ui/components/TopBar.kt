package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm0126.medpal.R

enum class TopBarCases { HOME, NAVIGATION, FORM, DEFAULT }

@Composable
fun TopBar(
    title: String,
    case: TopBarCases = TopBarCases.DEFAULT,
    onUserClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(color = colorResource(R.color.midnight_green))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 5.dp, top = 10.dp, end = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when (case) {
                TopBarCases.NAVIGATION -> {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Atrás",
                            tint = colorResource(R.color.rosy_brown),
                            modifier = Modifier.height(50.dp)
                        )
                    }
                }

                TopBarCases.HOME -> {
                    IconButton(onClick = onCalendarClick) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendario",
                            tint = colorResource(R.color.rosy_brown),
                            modifier = Modifier.height(50.dp)
                        )
                    }
                }

                TopBarCases.FORM -> {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = colorResource(R.color.rosy_brown),
                            modifier = Modifier.height(50.dp)
                        )
                    }
                }

                TopBarCases.DEFAULT -> {}
            }

            Text(
                text = title,
                color = colorResource(R.color.rosy_brown),
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold

            )

            when (case) {
                TopBarCases.NAVIGATION -> {}
                TopBarCases.HOME, TopBarCases.DEFAULT -> {
                    IconButton(onClick = onUserClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = colorResource(R.color.rosy_brown),
                            modifier = Modifier.height(50.dp)
                        )
                    }
                }

                TopBarCases.FORM -> {
                    Button(
                        onClick = onSaveClick,
                        modifier = Modifier
                            .width(110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.rosy_brown),
                        )
                    ) {
                        Text(
                            text = "Guardar",
                            color = colorResource(R.color.midnight_green),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun topPreview() {
    TopBar(title = "", case = TopBarCases.FORM)

}