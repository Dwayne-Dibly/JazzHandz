package com.example.jazzhandzapp.DataClassses

import android.os.Parcel
import android.os.Parcelable

data class DownloadedRoutines(val useruid: String?, val downloadedroutinename: String?, val downloadedroutinecreator: String?, val downloadedtempo: String?, val downloadedtrack: String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(useruid)
        parcel.writeString(downloadedroutinename)
        parcel.writeString(downloadedroutinecreator)
        parcel.writeString(downloadedtempo)
        parcel.writeString(downloadedtrack)
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