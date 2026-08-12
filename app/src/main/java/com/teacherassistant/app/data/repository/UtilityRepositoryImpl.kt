package com.teacherassistant.app.data.repository

import com.teacherassistant.app.data.local.dao.UtilityDao
import com.teacherassistant.app.data.local.entity.Routine
import com.teacherassistant.app.data.local.entity.TodoItem
import com.teacherassistant.app.domain.repository.UtilityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UtilityRepositoryImpl @Inject constructor(
    private val utilityDao: UtilityDao
) : UtilityRepository {

    override fun getRoutineForDay(day: String): Flow<List<Routine>> {
        return utilityDao.getRoutineForDay(day)
    }

    override suspend fun insertRoutine(routine: Routine): Long {
        return utilityDao.insertRoutine(routine)
    }

    override suspend fun deleteRoutine(routine: Routine) {
        utilityDao.deleteRoutine(routine)
    }

    override fun getAllTodos(): Flow<List<TodoItem>> {
        return utilityDao.getAllTodos()
    }

    override suspend fun insertTodo(todo: TodoItem): Long {
        return utilityDao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: TodoItem) {
        utilityDao.deleteTodo(todo)
    }

    override suspend fun updateTodoStatus(id: Long, isCompleted: Boolean) {
        utilityDao.updateTodoStatus(id, isCompleted)
    }
}
