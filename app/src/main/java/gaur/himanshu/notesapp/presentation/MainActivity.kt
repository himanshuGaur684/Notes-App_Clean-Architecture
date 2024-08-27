package gaur.himanshu.notesapp.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.notesapp.domain.model.Note
import gaur.himanshu.notesapp.presentation.provider.NotesContentProvider.Companion.NOTES_CONTENT_URI
import gaur.himanshu.notesapp.presentation.ui.theme.NotesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(context = this, viewModel = viewModel)
                }
            }
        }
    }
}


fun getNotes(context: Context): List<Note> {
    return runBlocking(Dispatchers.IO) {
        val cursor = context.contentResolver.query(
            NOTES_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val notesList = mutableListOf<Note>()
        cursor?.let {
            while (it.moveToNext()) {
                val title = it.getString(it.getColumnIndexOrThrow("title"))
                val desc = it.getString(it.getColumnIndexOrThrow("desc"))
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                notesList.add(
                    Note(
                        title = title, desc = desc, id = id
                    )
                )
            }
            it.close()
        }
        notesList
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context, viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetState = sheetState,
        sheetContent = {
            NoteForm { title, desc ->
                val note = Note(
                    title = title,
                    desc = desc
                )
                viewModel.insert(note)
                scope.launch {
                    sheetState.hide()
                }
            }
        }) {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Notes App") }, actions = {
                IconButton(onClick = {
                    scope.launch { sheetState.show() }
                    val notes = getNotes(context)
                    println(notes.toString())
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            })
        }) {

            if (uiState.data.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = "No notes found")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    items(uiState.data) {
                        Card(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = it.desc,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                IconButton(onClick = { viewModel.delete(it) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                            }

                        }
                    }

                }
            }
        }
    }
}


@Composable
fun NoteForm(onSave: (String, String) -> Unit) {
    val title = rememberSaveable { mutableStateOf("") }
    val desc = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp)
            .fillMaxWidth()
    ) {

        OutlinedTextField(value = title.value, onValueChange = {
            title.value = it
        }, modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Title", color = Color.Gray)
            })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = desc.value, onValueChange = {
            desc.value = it
        }, modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Description", color = Color.Gray)
            })
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSave.invoke(title.value, desc.value) }) {
            Text(text = "Save")
        }
    }
}

