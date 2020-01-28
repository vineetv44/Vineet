package com.appstreettask.ui.model

import android.os.Parcel
import android.os.Parcelable

class DataDetailsItem() : Parcelable {
    lateinit var username: String;
    lateinit var name: String;
    lateinit var type: String;
    lateinit var url: String;
    lateinit var avatar: String;
    lateinit var repo: DataModel;

    constructor(parcel: Parcel) : this() {
        username = parcel.readString()!!
        name = parcel.readString()!!
        type = parcel.readString()!!
        url = parcel.readString()!!
        avatar = parcel.readString()!!
        repo = parcel.readParcelable(DataModel::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeString(avatar)
        parcel.writeParcelable(repo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataDetailsItem> {
        override fun createFromParcel(parcel: Parcel): DataDetailsItem {
            return DataDetailsItem(parcel)
        }

        override fun newArray(size: Int): Array<DataDetailsItem?> {
            return arrayOfNulls(size)
        }
    }
}