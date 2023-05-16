package com.madm.common_libs.persistency

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RandomEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val ID : Int,
    val text : String,
    @ColumnInfo(defaultValue = "IT")
    val lang : String,
    val category : Int,
    @ColumnInfo(name = "favorite")
    var favorite : Int
) : Parcelable