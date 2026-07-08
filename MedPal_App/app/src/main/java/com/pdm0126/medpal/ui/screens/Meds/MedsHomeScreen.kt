package com.pdm0126.medpal.ui.screens.Meds

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.pdm0126.medpal.ui.components.AllMyMeds
import com.pdm0126.medpal.ui.components.AppScaffold
import com.pdm0126.medpal.ui.components.MedOfDayCard
import com.pdm0126.medpal.ui.components.TopBarCases
import androidx.compose.ui.platform.LocalContext
import com.pdm0126.medpal.data.notifications.AlertGlobalEvent

@Composable
fun MedsHomeScreen(
    viewModel: MedicationViewModel = viewModel(factory = MedicationViewModel.Factory),
    onNavigateToAddMedication: () -> Unit,
    onNavigateToProfile: () -> Unit,
    currentRoute: String,
    onNavigateToItemClick: (String) -> Unit,
) {
    val generalMedList by viewModel.generalMedList.collectAsState()
    val refresh by viewModel.refreshing.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refreshFromServer(context)
    }

    LaunchedEffect(key1 = true) {
        viewModel.event.collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(Unit) {
        AlertGlobalEvent.confirmations.collect { reminderId ->
            viewModel.toggleTakeStatus(reminderId)
        }
    }

    AppScaffold(
        title = "Medicamentos",
        currentRoute = currentRoute,
        onNavigationItemClick = onNavigateToItemClick,
        topBarScreenCase = TopBarCases.DEFAULT,
        onUserClick = onNavigateToProfile,
        floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAddMedication,
                    containerColor = colorResource(R.color.midnight_green),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add medication",
                        modifier = Modifier.size(32.dp)
                    )
            }
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = refresh,
            onRefresh = { viewModel.refreshFromServer(context) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Mis medicamentos del día",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 2000.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    items(generalMedList.dailyMedications) { item ->
                        MedOfDayCard(
                            name = item.name,
                            dose = item.dosage,
                            hour = item.time,
                            isTakenToday = item.isTaken,
                            onMarkTaken = { viewModel.toggleTakeStatus(item.reminderId) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                AllMyMeds(meds = generalMedList.allMedications)
            }
        }
    }
}