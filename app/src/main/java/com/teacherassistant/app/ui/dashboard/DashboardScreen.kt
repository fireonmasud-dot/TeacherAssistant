package com.teacherassistant.app.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToStudents: () -> Unit,
    onNavigateToTiffinTracker: () -> Unit,
    onNavigateToMonthlyReport: () -> Unit,
    onNavigateToResultEntry: () -> Unit,
    onNavigateToExamRanking: () -> Unit,
    onNavigateToRoutine: () -> Unit,
    onNavigateToTodos: () -> Unit,
    onNavigateToDeleted: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onNavigateToTeacherProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Assistant", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToTeacherProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Teacher Profile", modifier = Modifier.size(32.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        val dashboardItems = listOf(
            DashboardItem("Students", Icons.Default.Groups, MaterialTheme.colorScheme.primaryContainer, onNavigateToStudents),
            DashboardItem("Tiffin Tracker", Icons.Default.DirectionsRun, MaterialTheme.colorScheme.errorContainer, onNavigateToTiffinTracker),
            DashboardItem("Tiffin Report", Icons.Default.Summarize, MaterialTheme.colorScheme.errorContainer, onNavigateToMonthlyReport),
            DashboardItem("Result Entry", Icons.Default.EditDocument, MaterialTheme.colorScheme.tertiaryContainer, onNavigateToResultEntry),
            DashboardItem("Fair Ranking", Icons.Default.Leaderboard, MaterialTheme.colorScheme.tertiaryContainer, onNavigateToExamRanking),
            DashboardItem("AI Assistant", Icons.Default.AutoAwesome, MaterialTheme.colorScheme.tertiaryContainer, onNavigateToAiAssistant),
            DashboardItem("Class Routine", Icons.Default.CalendarMonth, MaterialTheme.colorScheme.secondaryContainer, onNavigateToRoutine),
            DashboardItem("To-Do List", Icons.Default.Checklist, MaterialTheme.colorScheme.secondaryContainer, onNavigateToTodos),
            DashboardItem("Recycle Bin", Icons.Default.DeleteSweep, MaterialTheme.colorScheme.surfaceVariant, onNavigateToDeleted)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dashboardItems) { item ->
                    DashboardCard(item)
                }
            }
            
            Text(
                text = "Developed by Mx Masud Rana",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val containerColor: androidx.compose.ui.graphics.Color,
    val onClick: () -> Unit
)

@Composable
fun DashboardCard(item: DashboardItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { item.onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = item.containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.contentColorFor(item.containerColor)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.contentColorFor(item.containerColor)
            )
        }
    }
}
