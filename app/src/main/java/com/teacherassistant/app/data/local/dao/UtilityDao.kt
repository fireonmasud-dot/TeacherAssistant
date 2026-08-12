package com.teacherassistant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teacherassistant.app.data.local.entity.Routine
import com.teacherassistant.app.data.local.entity.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface UtilityDao {
    // --- Routine ---
    @Query("SELECT * FROM routines WHERE dayOfWeek = :day ORDER BY timeSlot ASC")
    fun getRoutineForDay(day: String): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine): Long

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    // --- To-Do ---
    @Query("SELECT * FROM todo_items ORDER BY isCompleted ASC, CASE priority WHEN 'High' THEN 1 WHEN 'Medium' THEN 2 ELSE 3 END ASC")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItem): Long

    @Delete
    suspend fun deleteTodo(todo: TodoItem)
    
    @Query("UPDATE todo_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTodoStatus(id: Long, isCompleted: Boolean)
}
