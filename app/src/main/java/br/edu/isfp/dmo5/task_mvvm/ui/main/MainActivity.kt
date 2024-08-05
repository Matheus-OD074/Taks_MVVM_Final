package br.edu.isfp.dmo5.task_mvvm.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.isfp.dmo5.task_mvvm.R
import br.edu.isfp.dmo5.task_mvvm.databinding.ActivityMainBinding
import br.edu.isfp.dmo5.task_mvvm.databinding.DialogTaskBinding
import br.edu.isfp.dmo5.task_mvvm.ui.adapter.TaskAdapter
import br.edu.isfp.dmo5.task_mvvm.ui.listener.TaskClickListener

class MainActivity
    :AppCompatActivity(),
    View.OnClickListener,
    TaskClickListener
{

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = TaskAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setUpRecyclerView()
        setUpObservers()
        setUpListeners()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_add){
            newTask()
        }
    }

    override fun clickDone(position: Int) {
        viewModel.handleDone(position)
    }

    private fun setUpRecyclerView() {
        binding.recyclerTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTasks.adapter = adapter
    }

    private fun setUpObservers() {
        viewModel.tasks.observe(this, Observer {
            adapter.submitDataset(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.insertedTask.observe(this, Observer{
            val str: String = if (it){
                "Tarefa inserida com sucesso."
            }else {
                "Erro ao inserir a tarefa"
            }

            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        })

        viewModel.updatedTask.observe(this, Observer{
            val str: String = if (it){
                "Estado da tarefa alterado com sucesso"
            }else {
                "Estado da tarefa não foi alterado"
            }


            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        })
    }

    private fun setUpListeners() {
        binding.buttonAdd.setOnClickListener(this)
    }

    private fun newTask() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_task, null)

        val bindingDialog: DialogTaskBinding = DialogTaskBinding.bind(dialogView)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Adicionar nova tarefa")
            .setPositiveButton("Salvar", DialogInterface.OnClickListener{
                    dialog, witch ->
                val str = bindingDialog.editDescription.text.toString()
                viewModel.addTask(str)
                dialog.dismiss()
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                    dialog, witch ->
                dialog.dismiss()
            })

        val dialog = builder.create()
        dialog.show()

    }
}