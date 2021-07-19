package com.example.jazzhandzapp.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "routinename_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = Creator::class, parentColumns = arrayOf("creatorId"), childColumns = arrayOf("routineCreatorId"),  onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.CASCADE))
)
data class RoutineName(
    @PrimaryKey (autoGenerate = true) val routineId: Int,
    @ColumnInfo(name = "routinename") val routinename: String,
    @ColumnInfo(name = "routineCreatorId") val routinecreatorId: Int,
    @ColumnInfo(name = "tempo") val tempo: Int,
    @ColumnInfo(name = "track") val track: String) {
}