package com.example.jazzhandzapp.DataClassses

import android.os.Parcel
import android.os.Parcelable

data class Routines(val routineid: String?, val routinename: String?, val routinecreatorid: String?, val tempo: String?, val track: String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(routineid)
        parcel.writeString(routinename)
        parcel.writeString(routinecreatorid)
        parcel.writeString(tempo)
        parcel.writeString(track)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Routines> {
        override fun createFromParcel(parcel: Parcel): Routines {
            return Routines(parcel)
        }

        override fun newArray(size: Int): Array<Routines?> {
            return arrayOfNulls(size)
        }
    }
}