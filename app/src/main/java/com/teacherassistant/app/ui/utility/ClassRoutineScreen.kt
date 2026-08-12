package com.teacherassistant.app.ui.utility

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Routine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoutineScreen(
    viewModel: ClassRoutineViewModel = hiltViewModel()
) {
    val selectedDay by viewModel.selectedDay.collectAsState()
    val routines by viewModel.routines.collectAsState()
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Class Routine") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Routine")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Days selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daysOfWeek) { day ->
                    FilterChip(
                        selected = day == selectedDay,
                        onClick = { viewModel.selectDay(day) },
                        label = { Text(day) }
                    )
                }
            }

            // Routine List
            if (routines.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No classes scheduled for $selectedDay.", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(routines) { routine ->
                        RoutineCard(routine = routine, onDelete = { viewModel.deleteRoutine(routine) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var timeSlot by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Class for $selectedDay") },
            text = {
                Column {
                    OutlinedTextField(value = timeSlot, onValueChange = { timeSlot = it }, label = { Text("Time (e.g., 10:00 AM - 10:45 AM)") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject / Class Name") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note (Optional)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addRoutine(selectedDay, timeSlot, subject, note.ifBlank { null })
                    showAddDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun RoutineCard(routine: Routine, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(routine.timeSlot, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(routine.subject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (routine.note != null) {
                    Text(routine.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
