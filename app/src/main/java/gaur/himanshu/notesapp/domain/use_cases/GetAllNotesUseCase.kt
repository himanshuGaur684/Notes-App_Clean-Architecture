package gaur.himanshu.notesapp.domain.use_cases

import gaur.himanshu.notesapp.domain.repository.NotesRepository
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(private val notesRepository: NotesRepository) {

    operator fun invoke() = notesRepository.getAllNotes()

}