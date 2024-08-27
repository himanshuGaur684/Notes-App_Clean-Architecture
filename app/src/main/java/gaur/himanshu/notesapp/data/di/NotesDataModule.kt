package gaur.himanshu.notesapp.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.notesapp.data.local.NotesDao
import gaur.himanshu.notesapp.data.local.NotesDatabase
import gaur.himanshu.notesapp.data.repository.NotesRepoImpl
import gaur.himanshu.notesapp.domain.repository.NotesRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NotesDataModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotesDatabase {
        return NotesDatabase.getInstance(context)
    }

    @Provides
    fun provideNotesDao(notesDatabase: NotesDatabase): NotesDao {
        return notesDatabase.getNotesDao()
    }

    @Provides
    fun provideRepository(notesDao: NotesDao): NotesRepository {
        return NotesRepoImpl(notesDao)
    }

}