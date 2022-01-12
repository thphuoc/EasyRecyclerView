package com.example.mda

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class A(@SerializedName("name") open var name: String = "") : Parcelable