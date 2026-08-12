package com.teacherassistant.app.ui.academic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.data.local.entity.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultEntryScreen(
    viewModel: ResultEntryViewModel = hiltViewModel()
) {
    val exams by viewModel.exams.collectAsState()
    val students by viewModel.students.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    var selectedExam by remember { mutableStateOf<Exam?>(null) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var subjectName by remember { mutableStateOf("") }
    var marksObtained by remember { mutableStateOf("") }
    var fullMarks by remember { mutableStateOf("100") }
    var passMarks by remember { mutableStateOf("33") }

    var showNewExamDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter Results") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
            // Exam Selection
            Text("1. Select Exam", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = { showNewExamDialog = true }) {
                    Text("Create New Exam")
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (selectedExam != null) {
                    Text("Selected: ${selectedExam?.examName} (${selectedExam?.year})", color = MaterialTheme.colorScheme.primary)
                } else if (exams.isNotEmpty()) {
                    // Quick select first exam for simplicity in this UI
                    selectedExam = exams.first()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Student Selection
            Text("2. Select Student", fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(students) { student ->
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
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Marks Entry
            if (selectedExam != null && selectedStudent != null) {
                Text("3. Enter Marks for ${selectedStudent?.studentName}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Subject Name (e.g., Bengali, Math)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = marksObtained,
                        onValueChange = { marksObtained = it },
                        label = { Text("Obtained") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fullMarks,
                        onValueChange = { fullMarks = it },
                        label = { Text("Full") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = passMarks,
                        onValueChange = { passMarks = it },
                        label = { Text("Pass") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.saveResult(
                            studentId = selectedStudent!!.id,
                            examId = selectedExam!!.id,
                            subjectName = subjectName,
                            marksObtained = marksObtained,
                            fullMarks = fullMarks,
                            passMarks = passMarks
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Result")
                }

                when (saveState) {
                    is SaveState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    is SaveState.Success -> {
                        Text("Saved successfully!", color = MaterialTheme.colorScheme.primary)
                        LaunchedEffect(Unit) {
                            marksObtained = "" // Clear for next entry
                            viewModel.resetSaveState()
                        }
                    }
                    is SaveState.Error -> {
                        Text("Error: ${(saveState as SaveState.Error).message}", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }

    if (showNewExamDialog) {
        var newExamName by remember { mutableStateOf("") }
        var newExamYear by remember { mutableStateOf("2024") }
        AlertDialog(
            onDismissRequest = { showNewExamDialog = false },
            title = { Text("New Exam") },
            text = {
                Column {
                    OutlinedTextField(value = newExamName, onValueChange = { newExamName = it }, label = { Text("Exam Name") })
                    OutlinedTextField(value = newExamYear, onValueChange = { newExamYear = it }, label = { Text("Year") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.createExam(newExamName, newExamYear)
                    showNewExamDialog = false
                }) { Text("Create") }
            }
        )
    }
}
