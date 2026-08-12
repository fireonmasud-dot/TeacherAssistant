package com.teacherassistant.app.domain.ai

object AiPromptManager {
    
    // Modular Prompts (Rule 33)
    
    fun getStudentSummaryPrompt(studentName: String, attendanceCount: Int, tiffinEscapes: Int): String {
        return """
            You are a helpful Teacher Assistant AI.
            Analyze the following student data and generate a short, encouraging 2-sentence summary for the parents.
            Student Name: $studentName
            Attendance this month: $attendanceCount days
            Tiffin Escapes this month: $tiffinEscapes days
            
            Keep the tone professional yet caring.
        """.trimIndent()
    }

    fun getGeneralAdvicePrompt(query: String): String {
        return """
            You are an expert pedagogical AI assistant for a teacher.
            Answer the following query clearly and concisely:
            Query: $query
        """.trimIndent()
    }
}
