package com.example.jazzhandzapp.Database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

//// https://medium.com/mindorks/room-kotlin-android-architecture-components-71cad5a1bb35  used this - didn't quite work?

//https://developer.android.com/training/data-storage/room/relationships

@Entity(tableName = "creator_table")
class Creator(
    @PrimaryKey(autoGenerate = true) val creatorId: Int,
    @ColumnInfo(name = "creator") val creator: String)  {
}



//@Entity(tableName = "routinename_table",
//    foreignKeys = arrayOf(
//        ForeignKey(entity = Creator::class, parentColumns = arrayOf("creatorId"), childColumns = arrayOf("routineCreatorId"),  onUpdate = CASCADE,onDelete = CASCADE))
//
//
//)
//class RoutineName(
//    @PrimaryKey (autoGenerate = true) val routineId: Int,
//    @ColumnInfo(name = "routinename") val routinename: String,
//    @ColumnInfo(name = "routineCreatorId") val routinecreatorId: Int,
//    @ColumnInfo(name = "tempo") val tempo: Int,
//    @ColumnInfo(name = "track") val track: String) {
//}

class CreatorRoutines(
    @Embedded
    var creator: Creator? = null,

    @Relation(
        parentColumn = "creatorId",
        entityColumn = "creator"
    )
    var routine: List<Creator> = ArrayList()){
}

@Entity (tableName = "move_table")
data class DanceMove (
    @PrimaryKey(autoGenerate = true) val dancemoveId: Int,
    @ColumnInfo(name = "dancemove") val dancemove: String){

}

@Entity(tableName = "routine_element_table",
//    foreignKeys = arrayOf(
//        ForeignKey(entity = RoutineName::class, parentColumns = arrayOf("routineId"), childColumns = arrayOf("routineElementId"),  onUpdate = CASCADE,onDelete = CASCADE))

            foreignKeys = [
            ForeignKey(entity = RoutineName::class, parentColumns = arrayOf("routineId"), childColumns = arrayOf("routineElementId"),  onUpdate = CASCADE,onDelete = CASCADE),
            ForeignKey(entity = DanceMove::class, parentColumns = arrayOf("dancemoveId"), childColumns = arrayOf("routineDancemoveId"),  onUpdate = CASCADE,onDelete = CASCADE)]


)
class RoutineElement(
    @PrimaryKey (autoGenerate = true) val elementId: Int,
    @ColumnInfo(name = "routineElementId") val routineelementId: Int,
    @ColumnInfo(name = "routineDancemoveId") val routinedancemoveId: Int,

    @ColumnInfo(name = "elementtime") val elementtime: Int,   // time of event from start
    @ColumnInfo(name = "elementbeat") val elementbeat: Int,    // beat - convert into 1-8
    @ColumnInfo(name = "routineelementtime") val routineelementtime: Int,    // routine time for display - seconds
    @ColumnInfo(name = "comment") val comment: String,    // text comment e.g. hand guesture
    @ColumnInfo(name = "weighton") val weighton: String    // text comment e.g. hand guesture

    ) {
}


