package com.example.olsera

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Companies")
@Parcelize
data class Company(
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "companyName")
    val companyName: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "zipCode")
    val zipCode: String
):Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var companyID = 0
}
