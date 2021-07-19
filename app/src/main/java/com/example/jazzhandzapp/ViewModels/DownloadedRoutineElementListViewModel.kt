package com.example.jazzhandzapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.jazzhandzapp.DataClassses.Elements
import com.example.jazzhandzapp.Database.*
import kotlinx.coroutines.launch
import java.lang.Exception

class DownloadedRoutineElementListViewModel(application: Application): AndroidViewModel(application) {

    private val repository: DatabaseRepository

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

        allCreators = repository.allCreators
        routineCreators = repository.routineCreators
        allRoutines = repository.allRoutineNames
        allDanceMoves = repository.allDanceMoves

    }

    fun saveRoutine(routinename: RoutineName) {

        try {
            viewModelScope.launch { repository.insertRoutineName(routinename) }
        } catch (exception: Exception) {

        }
    }

    fun newRoutineCreator(newroutinecreator: String, newroutinecreator_id: Int) {

        val newcreator = Creator(newroutinecreator_id, newroutinecreator)

        viewModelScope.launch {repository.insertCreator(newcreator)}
    }

    fun insertElements(routinelistsize: Int, routineelements: ArrayList<Elements>) {

        for (i in routineelements.indices) {
            val danceelement: RoutineElement

            danceelement = RoutineElement(0,routinelistsize, routineelements[i].elementmove!!.toInt(), routineelements[i].elementtime!!.toInt()
                ,routineelements[i].elementbeat!!.toInt(),routineelements[i].routineelementtime!!.toInt(), routineelements[i].comments!!, routineelements[i].weighton!!)

            viewModelScope.launch {repository.insertRoutineElement(danceelement) }

        }

    }

    fun deleteRoutineElements(routineid: Int) {

        viewModelScope.launch {repository.deleteRoutineElements(routineid) }
    }

}