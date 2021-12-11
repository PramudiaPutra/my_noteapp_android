package com.pramudiaputr.mynoteapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pramudiaputr.mynoteapp.databinding.ActivityMainBinding
import com.pramudiaputr.mynoteapp.ui.ViewModelFactory
import com.pramudiaputr.mynoteapp.ui.insert.NoteActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = obtainViewModel(this@MainActivity)
        viewModel.getAllNotes().observe(this, { noteList ->
            if (noteList != null) {
                adapter.setListNotes(noteList)
            } else {
                Log.d("LISTT", "empty")
            }
        })

        adapter = MainAdapter()

//        binding.rvNotes.layoutManager = LinearLayoutManager(this@MainActivity)
//        binding.rvNotes.setHasFixedSize(true)
//        binding.rvNotes.adapter = adapter

        with(binding) {
            rvNotes.layoutManager = LinearLayoutManager(this@MainActivity)
            rvNotes.setHasFixedSize(true)
            rvNotes.adapter = adapter
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}