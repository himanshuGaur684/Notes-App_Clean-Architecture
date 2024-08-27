package gaur.himanshu.notesapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val NOTES_TABLE = "notes"

@Entity(NOTES_TABLE)
data class Note(
    @ColumnInfo("title") val title: String,
    @ColumnInfo("desc") val desc: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0
) {
    constructor() : this("", "", 0)

}
