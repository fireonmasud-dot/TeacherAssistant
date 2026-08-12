package com.teacherassistant.app.ui.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teacherassistant.app.data.local.entity.TodoItem
import com.teacherassistant.app.domain.repository.UtilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val utilityRepository: UtilityRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    init {
        viewModelScope.launch {
            utilityRepository.getAllTodos().collect {
                _todos.value = it
            }
        }
    }

    fun addTodo(task: String, priority: String) {
        viewModelScope.launch {
            val todo = TodoItem(task = task, priority = priority)
            utilityRepository.insertTodo(todo)
        }
    }

    fun toggleTodoStatus(todo: TodoItem) {
        viewModelScope.launch {
            utilityRepository.updateTodoStatus(todo.id, !todo.isCompleted)
        }
    }

    fun deleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            utilityRepository.deleteTodo(todo)
        }
    }
}
