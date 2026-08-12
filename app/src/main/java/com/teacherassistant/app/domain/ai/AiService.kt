package com.teacherassistant.app.domain.ai

import kotlinx.coroutines.flow.Flow

interface AiService {
    /**
     * Sends a prompt to the AI model and returns the response.
     * In a production environment, this would call the Gemini API securely.
     */
    fun generateResponse(prompt: String): Flow<String>
}
