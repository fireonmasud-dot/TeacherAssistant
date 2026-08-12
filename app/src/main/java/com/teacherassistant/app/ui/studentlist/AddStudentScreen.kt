package com.teacherassistant.app.ui.studentlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddStudentViewModel = hiltViewModel()
) {
    var studentName by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var fatherPhone by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var motherPhone by remember { mutableStateOf("") }
    var studentIdNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Student") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                label = { Text("Student Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    label = { Text("Class") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = section,
                    onValueChange = { section = it },
                    label = { Text("Section") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it },
                    label = { Text("Roll") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = studentIdNumber,
                onValueChange = { studentIdNumber = it },
                label = { Text("Student ID Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fatherName,
                onValueChange = { fatherName = it },
                label = { Text("Father's Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fatherPhone,
                onValueChange = { fatherPhone = it },
                label = { Text("Father's Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motherName,
                onValueChange = { motherName = it },
                label = { Text("Mother's Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motherPhone,
                onValueChange = { motherPhone = it },
                label = { Text("Mother's Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (studentName.isNotBlank() && rollNumber.isNotBlank()) {
                        viewModel.addStudent(
                            studentName, className, section, rollNumber,
                            fatherName, fatherPhone, motherName, motherPhone, studentIdNumber
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Student")
            }
        }
    }
}
