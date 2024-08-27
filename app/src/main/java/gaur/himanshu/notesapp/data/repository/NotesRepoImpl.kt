package gaur.himanshu.notesapp.data.repository

import gaur.himanshu.notesapp.data.local.NotesDao
import gaur.himanshu.notesapp.data.mappers.toNote
import gaur.himanshu.notesapp.data.mappers.toNoteEntity
import gaur.himanshu.notesapp.domain.model.Note
import gaur.himanshu.notesapp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepoImpl(private val notesDao: NotesDao) : NotesRepository {
    override suspend fun insert(note: Note) {
        notesDao.insert(note.toNoteEntity())
    }

    override suspend fun update(note: Note) {
        notesDao.update(note.toNoteEntity())
    }

    override suspend fun delete(note: Note) {
        notesDao.delete(note.toNoteEntity())
    }

    override fun getAllNotes(): Flow<List<Note>> =
        notesDao.getAllNotes().map { it.map { it.toNote() } }
}