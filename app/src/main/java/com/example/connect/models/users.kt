package com.example.connect.models

import android.os.Parcel
import android.os.Parcelable

class users(
    private val uid: String?,
    val profilePhoto: String?,
    val email: String?,
    val username: String?,
    val number: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this("","","","","");
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(profilePhoto)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<users> {
        override fun createFromParcel(parcel: Parcel): users {
            return users(parcel)
        }

        override fun newArray(size: Int): Array<users?> {
            return arrayOfNulls(size)
        }
    }
}