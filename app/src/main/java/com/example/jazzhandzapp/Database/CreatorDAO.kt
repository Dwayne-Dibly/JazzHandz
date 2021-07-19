package com.example.jazzhandzapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CreatorDAO {
    @Query("SELECT * from creator_table ORDER BY creator ASC")
    fun getAlphabetizedCreators(): LiveData<List<Creator>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(creator: Creator)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(creator: Creator)

    @Query("DELETE FROM creator_table")
    suspend fun deleteall()
}