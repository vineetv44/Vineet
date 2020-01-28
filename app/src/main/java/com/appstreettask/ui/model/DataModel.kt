package com.appstreettask.ui.model

import android.os.Parcel
import android.os.Parcelable

class DataModel() : Parcelable {

    lateinit var name: String;
    lateinit var description: String;
    lateinit var url: String;

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        description = parcel.readString()!!
        url = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }

}