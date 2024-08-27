package gaur.himanshu.notesapp.data.repository

import gaur.himanshu.notesapp.data.local.NotesDao
import gaur.himanshu.notesapp.domain.model.Note
import gaur.himanshu.notesapp.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(private val notesDao: NotesDao) : NotesRepository {
    override suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun update(note: Note) {
        notesDao.update(note)
    }

    override suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes()
    }
}