package com.example.githubusernavigationdanapi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubusernavigationdanapi.database.FavoriteUser
import com.example.githubusernavigationdanapi.database.FavoriteUserDao
import com.example.githubusernavigationdanapi.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }
    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllNotes()
    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.update(favoriteUser) }
    }
}