package com.pramudiaputr.mynoteapp.ui.insert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.pramudiaputr.mynoteapp.R
import com.pramudiaputr.mynoteapp.databinding.ActivityNoteBinding
import com.pramudiaputr.mynoteapp.db.Note
import com.pramudiaputr.mynoteapp.helper.DateHelper
import com.pramudiaputr.mynoteapp.ui.ViewModelFactory

class NoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var isEdit = false
    private var note: Note? = null

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: ActivityNoteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        noteViewModel = obtainViewModel(this@NoteActivity)
        note = intent.getParcelableExtra(EXTRA_NOTE)

        if (note != null) isEdit = true else note = Note()

        var actionBarTitle: String
        var btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)

            if (note != null) {
                note?.let {
                    binding?.edtTitle?.setText(it.title)
                    binding?.edtDescription?.setText(it.description)
                }
            } else {
                actionBarTitle = getString(R.string.add)
                btnTitle = getString(R.string.save)
            }

            supportActionBar?.title = actionBarTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.btnSubmit?.text = btnTitle
        }

        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.edtTitle?.text.toString().trim()
            val description = binding?.edtDescription?.text.toString().trim()

            when {
                title.isEmpty() -> {
                    binding?.edtTitle?.error = getString(R.string.empty)
                }
                description.isEmpty() -> {
                    binding?.edtDescription?.error = getString(R.string.empty)
                }
                else -> {
                    note.let { note ->
                        note?.title = title
                        note?.description = description
                    }
                    if (isEdit) {
                        noteViewModel.update(note as Note)
                        showToast(getString(R.string.changed))
                    } else {
                        note.let {
                            note?.date = DateHelper.getCurrentDate()
                        }
                        noteViewModel.insert(note as Note)
                        Log.d("USER_TAG", "insert: $note")
                        showToast(getString(R.string.added))
                    }
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClosed = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClosed) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClosed) {
                    noteViewModel.delete(note as Note)
                    Log.d("USER_TAG", "delete: $note")
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NoteViewModel::class.java]
    }
}