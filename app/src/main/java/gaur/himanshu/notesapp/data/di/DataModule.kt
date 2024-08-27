package gaur.himanshu.notesapp.data.di

import android.content.Context
import androidx.room.Insert
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.notesapp.data.local.NotesDao
import gaur.himanshu.notesapp.data.local.NotesDatabase
import gaur.himanshu.notesapp.data.repository.NotesRepositoryImpl
import gaur.himanshu.notesapp.domain.repository.NotesRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotesDatabase{
        return NotesDatabase.getInstance(context)
    }

    @Provides
    fun provideNotesDao(notesDatabase: NotesDatabase): NotesDao{
        return notesDatabase.getNoteDao()
    }

    @Provides
    fun provideRepositoryImpl(notesDao: NotesDao):NotesRepository{
        return NotesRepositoryImpl(notesDao)
    }

}