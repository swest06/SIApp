package com.project.siapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String,
           val name: String,
           var photo: String = "",
           var age: String = "",
           var gender: String = "",
           var about: String = "",
           val location: String = "London, UK") : Parcelable {


//    val uid: String = _uid
//    val name: String = _name
//    var photo = ""
//    var age = ""
//    var gender = ""
//    var about = ""
//    val location = "London, UK"

    constructor() : this("", "")
}
