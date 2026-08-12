package com.teacherassistant.app.ui.tracking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Student
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiffinTrackerScreen(
    viewModel: TiffinTrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var description by remember { mutableStateOf("") }
    var teacherNote by remember { mutableStateOf("") }
    var voiceUri by remember { mutableStateOf<String?>(null) }

    val audioRecordLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            voiceUri = result.data?.data?.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tiffin Incident Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val state = uiState) {
                is TiffinTrackerUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is TiffinTrackerUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
                is TiffinTrackerUiState.Success -> {
                    Text("Select Student:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Student List for Selection
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.students) { student ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedStudent = student },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedStudent?.id == student.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text(
                                    text = "${student.rollNumber}. ${student.studentName}",
                                    modifier = Modifier.padding(16.dp),
                                    color = if (selectedStudent?.id == student.id) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Incident Entry Form
                    if (selectedStudent != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Record Incident for: ${selectedStudent?.studentName}", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text("Description (Where did they go?)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = teacherNote,
                                    onValueChange = { teacherNote = it },
                                    label = { Text("Private Teacher Note (Optional)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Voice Recording Button (Rule 9)
                                Button(
                                    onClick = { 
                                        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                                        audioRecordLauncher.launch(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                ) {
                                    Icon(Icons.Default.Mic, contentDescription = "Record Voice Note")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (voiceUri != null) "Voice Note Saved!" else "Add Voice Note")
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.recordIncident(
                                            studentId = selectedStudent!!.id,
                                            incidentType = "Escaped during Tiffin",
                                            description = description,
                                            teacherNote = teacherNote,
                                            voiceRecordingRef = voiceUri
                                        )
                                        description = ""
                                        teacherNote = ""
                                        voiceUri = null
                                        selectedStudent = null
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Save Incident Record")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
