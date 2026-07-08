package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R

data class NavigationItem(
    val label: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val route: String
)

@Composable
fun CustomNavigationBar(currentRoute: String, onItemClick: (String) -> Unit) {
    val items = listOf(
        NavigationItem(
            "Citas / Exámenes",
            Icons.Outlined.MedicalInformation,
            Icons.Default.MedicalInformation,
            "appointments"
        ),
        NavigationItem(
            "Medicamentos",
            Icons.Outlined.Medication,
            Icons.Default.Medication,
            "medication"
        )
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.midnight_green),
                    selectedTextColor = colorResource(R.color.midnight_green),
                    indicatorColor = colorResource(R.color.beige),
                    unselectedIconColor = colorResource(R.color.midnight_green),
                    unselectedTextColor = colorResource(R.color.midnight_green)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomNavigationBarPre() {
    CustomNavigationBar("medication",{} )

}