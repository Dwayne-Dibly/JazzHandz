package com.example.jazzhandzapp.DataClassses

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.jazzhandzapp.Database.DanceMove

//


data class Moves(val dancemoveid: Int, val dancemove: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dancemoveid)
        parcel.writeString(dancemove)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Moves> {
        override fun createFromParcel(parcel: Parcel): Moves {
            return Moves(parcel)
        }

        override fun newArray(size: Int): Array<Moves?> {
            return arrayOfNulls(size)
        }

//        fun getItem(position: Int): Moves? {
//            return listOf<Moves>()[position]
//        }


        fun setDanceMoves(dancemoves: List<DanceMove>) {

            var list: ArrayList<Moves> = ArrayList<Moves>()

            for (i in dancemoves.indices) {
                var dancemoveid = dancemoves[i].dancemoveId

                var dancemove = dancemoves[i].dancemove

                list.add(Moves(dancemoveid, dancemove))

            }
            Log.d("Paul", "dancemoves: $list")
        }
    }
}

