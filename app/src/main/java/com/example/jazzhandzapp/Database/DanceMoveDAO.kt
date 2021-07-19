package com.example.jazzhandzapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DanceMoveDAO {
    @Query("SELECT * from move_table ORDER BY dancemove ASC")
    fun getAlphabetizedMoves(): LiveData<List<DanceMove>>

    @Query("SELECT * from move_table")
    fun getAllMoves(): LiveData<List<DanceMove>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dancemove: DanceMove)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dancemove: DanceMove)

    @Transaction
    @Query("SELECT * FROM move_table WHERE dancemoveId = :id ")
    fun getDanceMove(id: Int): DanceMove

    @Query("DELETE FROM move_table")
    suspend fun deleteall()
}