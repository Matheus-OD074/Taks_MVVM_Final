package br.edu.isfp.dmo5.task_mvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.isfp.dmo5.task_mvvm.data.model.Task
import br.edu.isfp.dmo5.task_mvvm.data.repository.TaskRepository
import br.edu.isfp.dmo5.task_mvvm.dto.TaskDto

class MainViewModel: ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableLiveData<List<TaskDto>>()
    val tasks: LiveData<List<TaskDto>> = _tasks

    private val _insertedTask = MutableLiveData<Boolean>()
    val insertedTask: LiveData<Boolean> = _insertedTask

    private val _updatedTask = MutableLiveData<Boolean>()
    val updatedTask: LiveData<Boolean> = _updatedTask

    init {
        loadData()
    }

    fun handleDone(position: Int){
        val dto = _tasks.value?.get(position)
        if (dto != null){
            val task = repository.findById(dto.id)

            task.isCompleted = !task.isCompleted
            _updatedTask.value = true
            loadData()
        }
    }

    fun addTask(str: String){
        val task = Task(str, false)
        repository.insert(task)
        _insertedTask.value = true

        loadData()
    }

    private fun loadData(){
        val taskList = repository.findAll()
        val taskDtoList = taskList
            .map {
                    task: Task -> TaskDto(task.id, task.description, task.isCompleted)
            }

        _tasks.value = taskDtoList
    }


}