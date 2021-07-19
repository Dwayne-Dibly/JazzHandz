package com.example.jazzhandzapp.DataClassses

import android.os.Parcel
import android.os.Parcelable

data class Creators(val creatorid: String?, val creatorname: String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(creatorid)
        parcel.writeString(creatorname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Elements> {
        override fun createFromParcel(parcel: Parcel): Elements {
            return Elements(parcel)
        }

        override fun newArray(size: Int): Array<Elements?> {
            return arrayOfNulls(size)
        }
    }
}
