package com.teacherassistant.app.domain.repository

import com.teacherassistant.app.data.local.entity.Routine
import com.teacherassistant.app.data.local.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface UtilityRepository {
    fun getRoutineForDay(day: String): Flow<List<Routine>>
    suspend fun insertRoutine(routine: Routine): Long
    suspend fun deleteRoutine(routine: Routine)

    fun getAllTodos(): Flow<List<TodoItem>>
    suspend fun insertTodo(todo: TodoItem): Long
    suspend fun deleteTodo(todo: TodoItem)
    suspend fun updateTodoStatus(id: Long, isCompleted: Boolean)
}
