package com.pramudiaputr.mynoteapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.pramudiaputr.mynoteapp.db.Note

class NoteDiffCallback(
    private val mOldNoteList: List<Note>,
    private val mNewNoteList: List<Note>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldNoteList.size
    }

    override fun getNewListSize(): Int {
        return mNewNoteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList[oldItemPosition].id == mNewNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContent = mOldNoteList[oldItemPosition]
        val newContent = mNewNoteList[newItemPosition]
        return oldContent.title == newContent.title && oldContent.description == newContent.description
    }
}