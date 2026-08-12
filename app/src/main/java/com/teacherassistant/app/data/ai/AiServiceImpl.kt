package com.teacherassistant.app.data.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.teacherassistant.app.domain.ai.AiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AiServiceImpl @Inject constructor() : AiService {

    // Initialize the Gemini model with the provided API key
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AQ.Ab8RN6JS9nOtq7ekq0co2z3logf-f8xHVdHYeeGT7p6_1XTAaQ"
    )

    override fun generateResponse(prompt: String): Flow<String> = flow {
        try {
            val response = generativeModel.generateContent(prompt)
            emit(response.text ?: "I'm sorry, I couldn't generate a response.")
        } catch (e: Exception) {
            emit("Error: ${e.message}")
        }
    }
}
