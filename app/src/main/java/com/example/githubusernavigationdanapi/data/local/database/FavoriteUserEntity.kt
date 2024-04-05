package com.example.githubusernavigationdanapi.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
//    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = null,

) : Parcelable
