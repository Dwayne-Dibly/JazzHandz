package com.example.jazzhandzapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutineElementDAO {
        @Query("SELECT * from routine_element_table")
        fun getRoutineElementNames(): LiveData<List<RoutineElement>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
//        suspend fun insert(routineElementName: RoutineElement)
        suspend fun insert(routineElementName: RoutineElement): Long  // try to get row id back


        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun update(routineElementName: RoutineElement)

        @Query("DELETE FROM routine_element_table")
        suspend fun deleteall()

        @Transaction
//        @Query("DELETE FROM routine_element_table WHERE elementId = :id")
        @Query("DELETE FROM routine_element_table WHERE elementId = :id")
        suspend fun deleteLastElement(id: Int)

        @Transaction
        @Query("DELETE FROM routine_element_table WHERE routineElementId = :id")
        suspend fun deleteroutine(id: Int)

        @Transaction
//        @Query("SELECT * FROM routinename_table, move_table INNER JOIN routine_element_table WHERE routineElementId = routineId AND routineDancemoveId = dancemoveId ORDER BY elementtime")   //  this works!! Have to have * and not selected columns
        @Query("SELECT * FROM routinename_table, move_table INNER JOIN routine_element_table WHERE routineElementId = routineId AND routineDancemoveId = dancemoveId")   //  this works!! Have to have * and not selected columns
        fun getallRoutineElements(): LiveData<List<RoutineElement>>

        @Transaction
        @Query("SELECT * FROM routinename_table, move_table INNER JOIN routine_element_table WHERE routineElementId = routineId AND routineDancemoveId = dancemoveId AND routineId = :id ")
        fun getRoutineElements(id: Int): LiveData<List<RoutineElement>>

        // copy Routine and save as a new one
//        @Transaction
//        @Query( )


}