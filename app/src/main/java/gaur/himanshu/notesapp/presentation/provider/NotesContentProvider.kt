package gaur.himanshu.notesapp.presentation.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import gaur.himanshu.notesapp.data.local.NOTES_DATABASE_NAME
import gaur.himanshu.notesapp.data.local.NotesDao
import gaur.himanshu.notesapp.data.local.NotesDatabase
import gaur.himanshu.notesapp.domain.model.NOTES_TABLE
import gaur.himanshu.notesapp.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotesContentProvider : ContentProvider() {

    private lateinit var database: NotesDatabase
    private lateinit var notesDao: NotesDao

    companion object {
        const val AUTHORITY = "gaur.himanshu.notesapp.provider"
        private const val FIRST_PATTERN = 1
        private const val SECOND_PATTERN = 2

        val NOTES_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$NOTES_TABLE")
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, NOTES_TABLE, FIRST_PATTERN)
        addURI(AUTHORITY, NOTES_TABLE.plus("#"), SECOND_PATTERN)
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        database =
            Room.databaseBuilder(context, NotesDatabase::class.java, NOTES_DATABASE_NAME).build()
        notesDao = database.getNoteDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            FIRST_PATTERN -> {
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection(selection, selectionArgs)
                    .orderBy(sortOrder)
                val query = queryBuilder.create()
                runBlocking(Dispatchers.IO) { database.query(query) }
            }

            SECOND_PATTERN -> {
                val noteId = ContentUris.parseId(uri)
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection("${Note::id.name} = ?", arrayOf(noteId.toString()))

                val query = queryBuilder.create()
                runBlocking(Dispatchers.IO) { database.query(query) }
            }

            else -> throw IllegalArgumentException("Unknown URI")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            FIRST_PATTERN -> "vnd.android.cursor.dir/$AUTHORITY.$NOTES_TABLE"
            SECOND_PATTERN -> "vnd.android.cursor.item/$AUTHORITY.$NOTES_TABLE"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val note = Note(
            title = values?.getAsString("title") ?: "",
            desc = values?.getAsString("desc") ?: ""
        )
        val noteId = runBlocking { notesDao.insert(note) }
        context?.contentResolver?.notifyChange(NOTES_CONTENT_URI, null)
        return ContentUris.withAppendedId(NOTES_CONTENT_URI, noteId.toLong())
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        val noteId = ContentUris.parseId(uri)
        val id = runBlocking(Dispatchers.IO) {
            val note = notesDao.getNoteById(noteId.toInt())
            notesDao.delete(note)
        }
        context?.contentResolver?.notifyChange(NOTES_CONTENT_URI, null)
        return id
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val noteId = ContentUris.parseId(uri)
        val note = runBlocking { notesDao.getNoteById(noteId.toInt()) }
        val updateNote = note.copy(
            title = values?.getAsString("title") ?: note.title,
            desc = values?.getAsString("desc") ?: note.desc,
            id = noteId.toInt()
        )
        val id = runBlocking(Dispatchers.IO) { notesDao.update(updateNote) }
        context?.contentResolver?.notifyChange(NOTES_CONTENT_URI, null)
        return id
    }
}