package com.teacherassistant.app.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.domain.ai.AiPromptManager
import com.teacherassistant.app.domain.ai.AiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    private val aiService: AiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    fun askGeneralAdvice(query: String) {
        if (query.isBlank()) return
        
        _uiState.value = AiUiState.Loading
        val prompt = AiPromptManager.getGeneralAdvicePrompt(query)
        
        viewModelScope.launch {
            aiService.generateResponse(prompt)
                .catch { e -> _uiState.value = AiUiState.Error(e.message ?: "AI Error") }
                .collect { response ->
                    _uiState.value = AiUiState.Success(response)
                }
        }
    }
}

sealed class AiUiState {
    object Idle : AiUiState()
    object Loading : AiUiState()
    data class Success(val response: String) : AiUiState()
    data class Error(val message: String) : AiUiState()
}
