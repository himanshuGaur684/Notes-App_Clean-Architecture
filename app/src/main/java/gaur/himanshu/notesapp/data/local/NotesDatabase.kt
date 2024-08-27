package gaur.himanshu.notesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gaur.himanshu.notesapp.domain.model.Note

const val NOTES_DATABASE_NAME = "notes_database"

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, NotesDatabase::class.java, NOTES_DATABASE_NAME)
                .build()
    }

    abstract fun getNoteDao(): NotesDao

}