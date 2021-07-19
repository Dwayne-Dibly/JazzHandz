package com.example.jazzhandzapp.ViewModels

import android.app.Application
import android.util.Log.d
import androidx.lifecycle.*
import com.example.jazzhandzapp.Activities.DanceActivity
import com.example.jazzhandzapp.Database.*
import kotlinx.coroutines.launch

class DanceActivityViewModel(application: Application): AndroidViewModel(application) {

    val repository: DatabaseRepository

    val allRoutineElements: LiveData<List<RoutineElement>>

    val allCreators: LiveData<List<Creator>>

    val routineCreators: LiveData<List<CreatorRoutines>>
    val allRoutines: LiveData<List<RoutineName>>
    val allDanceMoves: LiveData<List<DanceMove>>

    init {
        val creatorsDAO = AppDatabase.getDatabase(application, viewModelScope).creatorDAO()
        val routineNamesDAO = AppDatabase.getDatabase(application, viewModelScope).routineNameDAO()
        val danceMovesDAO = AppDatabase.getDatabase(application, viewModelScope).danceMoveDAO()
        val routineElementDAO = AppDatabase.getDatabase(application, viewModelScope).routineElementDAO()

        repository = DatabaseRepository(creatorsDAO, routineNamesDAO, danceMovesDAO, routineElementDAO)

        allRoutineElements = repository.allroutimeElements
        allCreators = repository.allCreators
        routineCreators = repository.routineCreators
        allRoutines = repository.allRoutineNames
        allDanceMoves = repository.allDanceMoves
    }

    fun insertElement(danceelement: RoutineElement) {

        viewModelScope.launch {repository.insertRoutineElement(danceelement) }

//        d("Paul", "last element ${danceelement.routinedancemoveId}")
//
//        if (danceelement.routinedancemoveId == 5) {
//           allRoutineElements.observe(DanceActivity(), Observer { element_id -> element_id?.let { deleteLastElement(element_id, danceelement) } })
//
//            d("Paul", "Here")
//
//        } else
//        if (danceelement.routinedancemoveId == 6) {
//
//        }
    }

    fun deleteLastElement(elements: List<RoutineElement>, danceelement: RoutineElement) {

//        val lastelementindex = elements[elements.size-1].elementId
//        d("Paul", "last element to delete ${danceelement.routinedancemoveId} ")
//        viewModelScope.launch {repository.deleteLastElement(lastelementindex) }
    }

    fun deleteRoutineElements(routineid: Int) {

        viewModelScope.launch {repository.deleteRoutineElements(routineid) }
    }

    fun setDatabaseTempo(thisroutine: RoutineName) {

        viewModelScope.launch { repository.updateRoutineName(thisroutine) }
    }
}