package com.example.jazzhandzapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutineNameDAO {
    @Query("SELECT * from routinename_table")
    fun getRoutineNames(): LiveData<List<RoutineName>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(routineName: RoutineName)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(routineName: RoutineName)

    @Query("DELETE FROM routinename_table")
    suspend fun deleteall()

    @Transaction
    @Query("DELETE FROM routinename_table WHERE routineId = :id ")
    suspend fun deleteRoutine(id: Int)

    @Transaction
//    @Query("SELECT * FROM creator_table")  //    INNER JOIN creator_table on creatorId = creatorId")  ///  runs but doesn't import the data
    @Query("SELECT * FROM creator_table INNER JOIN routinename_table WHERE routineCreatorId = creatorId")   //  this works!! Have to have * and not selected columns
    fun getCreatorsandRoutines(): LiveData<List<CreatorRoutines>>

}