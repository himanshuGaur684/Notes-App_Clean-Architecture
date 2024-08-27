package gaur.himanshu.notesapp.domain.repository

import gaur.himanshu.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)

    fun getAllNotes():Flow<List<Note>>

}