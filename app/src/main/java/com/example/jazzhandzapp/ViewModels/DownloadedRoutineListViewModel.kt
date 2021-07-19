package com.example.jazzhandzapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.jazzhandzapp.Database.*

class DownloadedRoutineListViewModel(application: Application): AndroidViewModel(application) {
    private val repository: DatabaseRepository

    val allCreators: LiveData<List<Creator>>

    val routineCreators: LiveData<List<CreatorRoutines>>
    val allRoutines: LiveData<List<RoutineName>>
    val allDanceMoves: LiveData<List<DanceMove>>


    init {
        val creatorsDAO = AppDatabase.getDatabase(application, viewModelScope).creatorDAO()
        val routineNameDAO = AppDatabase.getDatabase(application, viewModelScope).routineNameDAO()
        val danceMovesDAO = AppDatabase.getDatabase(application, viewModelScope).danceMoveDAO()
        val routineElementDAO = AppDatabase.getDatabase(application, viewModelScope).routineElementDAO()

        repository = DatabaseRepository(creatorsDAO, routineNameDAO, danceMovesDAO, routineElementDAO)

        allCreators = repository.allCreators
        routineCreators = repository.routineCreators
        allRoutines = repository.allRoutineNames
        allDanceMoves = repository.allDanceMoves

    }
}