package com.example.jazzhandzapp.Database

import androidx.lifecycle.LiveData
import io.reactivex.Single
import java.util.concurrent.Callable


class DatabaseRepository(private val creatorDAO: CreatorDAO, private val routineNameDAO: RoutineNameDAO, private val danceMoveDAO: DanceMoveDAO, private val routineElementDAO: RoutineElementDAO)  {

    //    val allOwners: LiveData<List<Owner>> = ownerDAO.loadAllOwners()
    val allCreators: LiveData<List<Creator>> = creatorDAO.getAlphabetizedCreators()
    val allRoutineNames: LiveData<List<RoutineName>> = routineNameDAO.getRoutineNames()
    val routineCreators: LiveData<List<CreatorRoutines>> = routineNameDAO.getCreatorsandRoutines()
    val allDanceMoves: LiveData<List<DanceMove>> = danceMoveDAO.getAllMoves()

    val allroutimeElements: LiveData<List<RoutineElement>> = routineElementDAO.getallRoutineElements()

    suspend fun insertCreator(creator: Creator) {
        creatorDAO.insert(creator)
    }
    suspend fun insertRoutineName(routinename: RoutineName) {
        routineNameDAO.insert(routinename)
    }

    suspend fun updateRoutineName(routinename: RoutineName){
        routineNameDAO.update(routinename)
    }
    suspend fun insertDanceMove(dancemove: DanceMove) {
        danceMoveDAO.insert(dancemove)
    }
    suspend fun insertRoutineElement(routineelement: RoutineElement) {
//    suspend fun insertRoutineElement(routineelement: RoutineElement): Single<Long> {
        routineElementDAO.insert(routineelement)
    }

    suspend fun deleteRoutineElements(routineid:Int) {
        routineElementDAO.deleteroutine(routineid)
    }

    suspend fun deleteLastElement(elementid:Int) {
        routineElementDAO.deleteLastElement(elementid)
    }

}