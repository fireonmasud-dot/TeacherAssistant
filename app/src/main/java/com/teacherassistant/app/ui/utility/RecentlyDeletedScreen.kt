package com.teacherassistant.app.ui.utility

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Student
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyDeletedScreen(
    viewModel: RecentlyDeletedViewModel = hiltViewModel()
) {
    val deletedStudents by viewModel.deletedStudents.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recently Deleted") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            )
        }
    ) { paddingValues ->
        if (deletedStudents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Recycle Bin is empty.", color = MaterialTheme.colorScheme.outline)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Students will be permanently deleted after 30 days.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                items(deletedStudents) { student ->
                    DeletedStudentCard(
                        student = student,
                        onRestore = { viewModel.restoreStudent(student) },
                        onPermanentDelete = { viewModel.permanentlyDelete(student) }
                    )
                }
            }
        }
    }
}

@Composable
fun DeletedStudentCard(student: Student, onRestore: () -> Unit, onPermanentDelete: () -> Unit) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val deletedDateStr = student.deletedAt?.let { formatter.format(Date(it)) } ?: "Unknown Date"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(student.studentName, fontWeight = FontWeight.Bold)
                Text("Roll: ${student.rollNumber} | Class: ${student.className}", style = MaterialTheme.typography.bodySmall)
                Text("Deleted on: $deletedDateStr", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
            
            IconButton(onClick = onRestore) {
                Icon(Icons.Default.Restore, contentDescription = "Restore", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onPermanentDelete) {
                Icon(Icons.Default.DeleteForever, contentDescription = "Permanent Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
