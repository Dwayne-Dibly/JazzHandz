package com.example.jazzhandzapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [(Creator::class), RoutineName::class, DanceMove::class, RoutineElement::class], version = 1)  //  , CreatorRoutines::class
//@Database(entities = arrayOf(Creator::class), version = 1)

abstract class AppDatabase:  RoomDatabase() {

    abstract fun creatorDAO(): CreatorDAO
    abstract fun routineNameDAO(): RoutineNameDAO
    abstract fun danceMoveDAO(): DanceMoveDAO
    abstract fun routineElementDAO(): RoutineElementDAO

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> scope.launch {
                    populateDatabase(database.creatorDAO(), database.routineNameDAO(), database.danceMoveDAO(), database.routineElementDAO())
                }
            }
        }

        suspend fun populateDatabase(creatorDAO: CreatorDAO, routineNameDAO: RoutineNameDAO, danceMoveDAO: DanceMoveDAO, routineElementDAO: RoutineElementDAO) {    //  need to do this in onCreate and not onOpen
//        fun populateDatabase(creatorDAO: CreatorDAO , routineNameDAO: RoutineNameDAO) {    //  need to do this in onCreate and not onOpen
//            var creatorDAO = database.creatorDAO()
//                    var routineNameDAO = database.routineNameDAO()

            var creator = Creator(1, "You")
//            d("Paul", "${creator.creator}")
            creatorDAO.insert(creator)

            creator = Creator(2, "JazzHandz")
//            d("Paul", "${creator.creator}")
            creatorDAO.insert(creator)

            creator = Creator(3, "You3")
//            d("Paul", "${creator.creator}")
            creatorDAO.insert(creator)

            creator = Creator(4, "You4")
            creatorDAO.insert(creator)
            creator = Creator(5, "You5")
            creatorDAO.insert(creator)
            creator = Creator(6, "You6")
            creatorDAO.insert(creator)
            creator = Creator(7, "You7")
            creatorDAO.insert(creator)

            var routinename = RoutineName(1, "Last Routine", 1, 105, "Unknown")
            routineNameDAO.insert(routinename)
            routinename = RoutineName(2, "Steps Tutorial", 2, 120, "Peggy Lee - Fever")
            routineNameDAO.insert(routinename)
            routinename = RoutineName(3, "Routine2", 3, 300, "Sing Sing Sing")
            routineNameDAO.insert(routinename)
            routinename = RoutineName(4, "Routine 1", 1, 110, "Count Basie - Splanky")
            routineNameDAO.insert(routinename)
            routinename = RoutineName(5, "Routine4", 4, 120, "Baked a cake")
            routineNameDAO.insert(routinename)

            var dancemove = DanceMove(1, "Left Step")
//            d("Paul", "${dancemove.dancemove}")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(2, "Right Step")
//            d("Paul", "${dancemove.dancemove}")
            danceMoveDAO.insert(dancemove)
//            d("Paul", "${dancemove.dancemove}")
            dancemove = DanceMove(3, "Left Hop")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(4, "Right Hop")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(5, "Left Tap")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(6, "Right Tap")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(7, "Left Toe-tap")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(8, "Right Toe-tap")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(9, "Ball-change (to Left)")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(10, "Ball-change (to Right)")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(11, "Left Kick")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(12, "Right Kick")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(13, "Left Kick-replace")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(14, "Right Kick-replace")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(15, "Left Suzie Q")
            danceMoveDAO.insert(dancemove)
            dancemove = DanceMove(16, "Right Suzie Q")
            danceMoveDAO.insert(dancemove)

            var danceelement = RoutineElement(1, 2, 1, 0, 1, 0, "None", "Left")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(2, 2, 2, 500, 2, 0, "None", "Right")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(3, 2, 1, 1000, 3, 1, "None", "Left")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(4, 2, 2, 1500, 4, 1, "None", "Right")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(5, 2, 1, 2000, 5, 2, "None", "Left")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(6, 2, 2, 2500, 6, 2, "None", "Right")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(7, 2, 1, 3000, 7, 3, "None", "Left")
            routineElementDAO.insert(danceelement)
            danceelement = RoutineElement(8, 2, 2, 3500, 8, 3, "None", "Right")
            routineElementDAO.insert(danceelement)

            danceelement = RoutineElement(9, 3, 1, 200, 1, 0, "None", "Left")
            routineElementDAO.insert(danceelement)

            danceelement = RoutineElement(10, 3, 12, 400, 1, 0, "None", "Left")
            routineElementDAO.insert(danceelement)

            danceelement = RoutineElement(11, 2, 10, 4000, 9, 4, "None", "Right")
            routineElementDAO.insert(danceelement)

            //            var routines = listOf(RoutineName(0,"Last Routine"), RoutineName(0,"Routine1"))
//            var creatorroutines = CreatorRoutines(creator, routines)
//            routineNameDAO.getCreatorsandRoutines()
//
//            var creatorroutines = CreatorRoutines(creator, List(2, 0, "Last Routine")

        }

    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Routine Database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance.query("SELECT 1", null) // dummy query to force pre-populated data to import - works!
//                d("Paul","${CreatorRoutines()}" )
                return instance
            }

        }
    }

}




//abstract class AppDatabase:  RoomDatabase() {
//    abstract fun creatorDAO(): OwnerDAO
//    abstract fun ownerRoutinesDAO(): RoutineNameDAO
//    abstract fun routineNameDAO(): RoutineNameDAO
//    abstract fun danceMovesDAO(): DanceMovesDAO
//    abstract fun danceEventsDAO(): DanceEventsDAO
//
//    companion object {
//        var INSTANCE: AppDatabase? = null
//
//        fun getAppDatabase(context: Context): AppDatabase? {
//            if (INSTANCE == null){
//                synchronized(AppDatabase::class) {
//                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "JazzHandzDB").build()
//                }
//            }
//        return INSTANCE
//
//    }
//        fun destroyDataBase(){
//        INSTANCE = null
//        }
//    }
//}
