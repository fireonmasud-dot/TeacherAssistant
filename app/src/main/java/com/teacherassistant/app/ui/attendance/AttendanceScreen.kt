package com.teacherassistant.app.ui.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Attendance") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is AttendanceUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is AttendanceUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AttendanceUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.students) { student ->
                            AttendanceCard(
                                student = student,
                                onMark = { status ->
                                    viewModel.markAttendance(student.id, status)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceCard(student: Student, onMark: (String) -> Unit) {
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${student.rollNumber}. ${student.studentName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AttendanceButton(
                    text = "Present",
                    isSelected = selectedStatus == "Present",
                    selectedColor = MaterialTheme.colorScheme.secondary,
                    onClick = { 
                        selectedStatus = "Present"
                        onMark("Present")
                    }
                )
                AttendanceButton(
                    text = "Absent",
                    isSelected = selectedStatus == "Absent",
                    selectedColor = MaterialTheme.colorScheme.error,
                    onClick = { 
                        selectedStatus = "Absent"
                        onMark("Absent")
                    }
                )
                AttendanceButton(
                    text = "Late",
                    isSelected = selectedStatus == "Late",
                    selectedColor = Color(0xFFF9A825), // Amber
                    onClick = { 
                        selectedStatus = "Late"
                        onMark("Late")
                    }
                )
            }
        }
    }
}

@Composable
fun AttendanceButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}
