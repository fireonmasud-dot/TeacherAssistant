package com.teacherassistant.app.ui.academic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teacherassistant.app.data.local.entity.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamRankingScreen(
    viewModel: ExamRankingViewModel = hiltViewModel()
) {
    val exams by viewModel.exams.collectAsState()
    val rankingState by viewModel.rankingState.collectAsState()

    var selectedExam by remember { mutableStateOf<Exam?>(null) }
    var requiredSubjects by remember { mutableStateOf("Bengali, English, Math") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Results & Rank") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
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
            // 1. Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Select Exam", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Simple exam selector for UI
                    if (exams.isNotEmpty()) {
                        selectedExam = exams.first()
                        Text("Selected: ${selectedExam?.examName} (${selectedExam?.year})", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text("No exams found.", color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("2. Required Subjects (Rule 24 Check)", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = requiredSubjects,
                        onValueChange = { requiredSubjects = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Comma separated") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { selectedExam?.let { viewModel.generateRanking(it.id, requiredSubjects) } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedExam != null
                    ) {
                        Text("Generate Fair Rank")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Results / Errors
            when (val state = rankingState) {
                is RankingUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is RankingUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Validation Errors Found (Rule 24)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            state.errors.forEach { error ->
                                Text("• $error", color = MaterialTheme.colorScheme.onErrorContainer)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Please fix these errors in the Result Entry screen before generating final ranks.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
                is RankingUiState.Success -> {
                    Text("Fair Rank Generated Successfully!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    var rollsUpdated by remember { mutableStateOf(false) }

                    Button(
                        onClick = { 
                            viewModel.updateRollsBasedOnRank(state.result.ranking)
                            rollsUpdated = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(if (rollsUpdated) "Rolls Updated Successfully!" else "Update Student Rolls Based on Rank")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.result.ranking) { rankItem ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = "#${rankItem.rank}",
                                                modifier = Modifier.padding(8.dp),
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(rankItem.student.studentName, fontWeight = FontWeight.Bold)
                                            Text("Roll: ${rankItem.student.rollNumber}", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                    Text("Total: ${rankItem.totalMarks}", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
