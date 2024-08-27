package gaur.himanshu.notesapp.data.mappers

import gaur.himanshu.notesapp.data.local.NoteEntity
import gaur.himanshu.notesapp.domain.model.Note

fun Note.toNoteEntity():NoteEntity{
    return NoteEntity(id,title,desc)
}
fun NoteEntity.toNote():Note{
    return Note (id,title,desc)
}