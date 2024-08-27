package gaur.himanshu.notesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.notesapp.domain.model.Note
import gaur.himanshu.notesapp.domain.use_cases.DeleteUseCase
import gaur.himanshu.notesapp.domain.use_cases.GetAllNotesUseCase
import gaur.himanshu.notesapp.domain.use_cases.InsertUseCase
import gaur.himanshu.notesapp.domain.use_cases.UpdateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertUseCase: InsertUseCase,
    private val updateUseCase: UpdateUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase
) : ViewModel() {

    val uiState = getAllNotesUseCase.invoke()
        .map { UiState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState())

    fun insert(note: Note) = viewModelScope.launch {
        insertUseCase.invoke(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        updateUseCase(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        deleteUseCase(note)
    }

}

data class UiState(
    val data: List<Note> = emptyList()
)
