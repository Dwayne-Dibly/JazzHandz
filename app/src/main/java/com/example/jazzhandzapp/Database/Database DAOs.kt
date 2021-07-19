package com.example.jazzhandzapp.Database

//@Dao
////abstract class OwnerDAO : OwnerDao
//abstract class  OwnerDAO {
//
//    abstract interface OwnerDao {
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
////    fun insertOwner(owner: Owner)
//        fun insertOwner(vararg owners: Owner)
//
//        @Update
//        fun updateOwner(vararg owners: Owner)
//
//        @Delete
//        fun deleteOwner(vararg owners: Owner)
//
//        @Query("SELECT * FROM Owner")
//        fun loadAllOwners(): LiveData<List<Owner>>
//    }
//}

//@Dao
//abstract class RoutineNameDAO {
//
//    interface RoutineNameDao {
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        fun insertRoutineName(vararg routinename: RoutineName)
//
//        @Update
//        fun updateRoutineName(vararg routinename: RoutineName)
//
//        @Delete
//        fun deleteRoutineName(vararg routinename: RoutineName)
//
//        @Query("SELECT * FROM RoutineName")
//        fun loadAllRoutines(): Array<RoutineName>
//
////        @Transaction
////        @Query("SELECT * FROM Owner")
////        fun getOwnersandRoutines(): List<OwnerRoutines>
//
//    }
//}
//@Dao
//abstract class DanceMovesDAO
//
//interface DanceMovesDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertDanceMoves(danceMoves: DanceMoves)
//
//    @Update
//    fun updateDanceMoves(danceMoves: DanceMoves)
//
//    @Delete
//    fun deleteDanceMoves(danceMoves: DanceMoves)
//
////    @Query("SELECT * FROM danceMoves")
//    @Query("SELECT * FROM Moves")
//    fun getDanceMoves(): List<DanceMoves>
//}
//
//@Dao
//abstract class DanceEventsDAO
//
//interface DanceEventsDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertDanceEvents(danceEvents: DanceEvents)
//
//    @Update
//    fun updateDanceEvents(danceEvents: DanceEvents)
//
//    @Delete
//    fun deleteDanceEvents(danceEvents: DanceEvents)
//
//    //    @Query("SELECT * FROM danceMoves")
//    @Query("SELECT * FROM `Routine Element List`")
//    fun getDanceEvents(): List<DanceEvents>
//}