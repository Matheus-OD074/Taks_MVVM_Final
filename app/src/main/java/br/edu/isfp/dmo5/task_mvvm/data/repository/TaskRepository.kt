package br.edu.isfp.dmo5.task_mvvm.data.repository

import br.edu.isfp.dmo5.task_mvvm.data.dao.TaskDao
import br.edu.isfp.dmo5.task_mvvm.data.model.Task

class TaskRepository {

    private val dao = TaskDao

    fun findAll(): List<Task>{
        return dao.getAllTasks()
    }

    fun findById(id: Long): Task{
        return dao.getTask(id)
    }

    fun insert(task: Task){
        dao.addTask(task)
    }
}