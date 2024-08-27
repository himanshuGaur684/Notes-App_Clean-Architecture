package gaur.himanshu.notesapp.domain.use_cases

import gaur.himanshu.notesapp.domain.model.Note
import gaur.himanshu.notesapp.domain.repository.NotesRepository
import javax.inject.Inject

class UpdateUseCase @Inject constructor(private val notesRepository: NotesRepository) {

    suspend operator fun invoke(note:Note) = notesRepository.update(note)

}