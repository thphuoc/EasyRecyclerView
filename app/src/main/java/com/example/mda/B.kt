package com.example.mda

import android.os.Parcel
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class B (@SerializedName("age") var age: Int = 0): A() {
    private companion object : Parceler<B> {
        override fun B.write(parcel: Parcel, flags: Int) {
            parcel.writeString(Gson().toJson(this))
        }

        override fun create(parcel: Parcel): B {
            return Gson().fromJson(parcel.readString(), B::class.java)
        }
    }
}