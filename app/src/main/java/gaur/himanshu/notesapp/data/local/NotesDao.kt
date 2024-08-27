package gaur.himanshu.notesapp.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import gaur.himanshu.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note): Int

    @Delete
    suspend fun delete(note: Note): Int

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes")
    fun getAllNotesCursor(): Cursor

    @Query("SELECT * FROM notes WHERE id=:id")
    fun getNoteByIdCursor(id: Int): Cursor

    @Query("SELECT * FROM notes WHERE id=:id")
    suspend fun getNoteById(id: Int): Note


}