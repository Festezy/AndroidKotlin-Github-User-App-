package com.example.githubusernavigationdanapi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUserEntity: FavoriteUserEntity)
    @Update
    fun update(favoriteUserEntity: FavoriteUserEntity)
    @Delete
    fun delete(favoriteUserEntity: FavoriteUserEntity)
    @Query("SELECT * from FavoriteUserEntity")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM FavoriteUserEntity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUserEntity>

}