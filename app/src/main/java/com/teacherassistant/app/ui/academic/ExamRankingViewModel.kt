package com.teacherassistant.app.ui.academic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.Exam
import com.teacherassistant.app.domain.repository.AcademicRepository
import com.teacherassistant.app.domain.repository.StudentRepository
import com.teacherassistant.app.domain.usecase.ExamRankingResult
import com.teacherassistant.app.domain.usecase.GenerateExamRankingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamRankingViewModel @Inject constructor(
    private val generateExamRankingUseCase: GenerateExamRankingUseCase,
    private val academicRepository: AcademicRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _exams = MutableStateFlow<List<Exam>>(emptyList())
    val exams = _exams.asStateFlow()

    private val _rankingState = MutableStateFlow<RankingUiState>(RankingUiState.Idle)
    val rankingState: StateFlow<RankingUiState> = _rankingState.asStateFlow()

    init {
        viewModelScope.launch {
            academicRepository.getAllExams().collect { _exams.value = it }
        }
    }

    fun generateRanking(examId: Long, requiredSubjects: String) {
        val subjectsList = requiredSubjects.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (subjectsList.isEmpty()) {
            _rankingState.value = RankingUiState.Error(listOf("Please enter required subjects (comma separated)"))
            return
        }

        _rankingState.value = RankingUiState.Loading
        viewModelScope.launch {
            try {
                when (val result = generateExamRankingUseCase.invoke(examId, subjectsList)) {
                    is ExamRankingResult.Success -> {
                        _rankingState.value = RankingUiState.Success(result)
                    }
                    is ExamRankingResult.Error -> {
                        _rankingState.value = RankingUiState.Error(result.errors)
                    }
                }
            } catch (e: Exception) {
                _rankingState.value = RankingUiState.Error(listOf(e.message ?: "Unknown Error occurred"))
            }
        }
    }

    fun updateRollsBasedOnRank(ranking: List<com.teacherassistant.app.domain.usecase.StudentRank>) {
        viewModelScope.launch {
            try {
                ranking.forEach { rankItem ->
                    studentRepository.updateStudentRoll(rankItem.student.id, rankItem.rank.toString())
                }
                // Optional: You could update UI state to show a success message here
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}

sealed class RankingUiState {
    object Idle : RankingUiState()
    object Loading : RankingUiState()
    data class Success(val result: ExamRankingResult.Success) : RankingUiState()
    data class Error(val errors: List<String>) : RankingUiState()
}
