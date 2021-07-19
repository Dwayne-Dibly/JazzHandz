package com.example.jazzhandzapp.DataClassses

import android.os.Parcel
import android.os.Parcelable
//


data class Elements(val routineid: String?, val elementtime: String?, val elementbeat: String?, val elementmove: String?, val routineelementtime: String?, val comments: String?, val weighton: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(routineid)
        parcel.writeString(elementtime)
        parcel.writeString(elementbeat)
        parcel.writeString(elementmove)
        parcel.writeString(routineelementtime)
        parcel.writeString(comments)
        parcel.writeString(weighton)
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







//   Below works with setDummyData set!
//
//
//data class User(val name: String?, val resId: Int) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readInt()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeInt(resId)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<User> {
//        override fun createFromParcel(parcel: Parcel): User {
//            return User(parcel)
//        }
//
//        override fun newArray(size: Int): Array<User?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
