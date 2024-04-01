package com.example.githubusernavigationdanapi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.githubusernavigationdanapi.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.database.FavoriteUserDao
import com.example.githubusernavigationdanapi.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val result = MediatorLiveData<Result<List<FavoriteUserDao>>>()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUserEntity>> = mFavoriteUserDao.getAllFavoriteUsers()
    fun insert(favoriteUserEntity: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUserEntity) }
    }

    fun getUserFavorite(username: String): LiveData<FavoriteUserEntity> {
        return mFavoriteUserDao.getFavoriteUserByUsername(username)
    }

    fun delete(favoriteUserEntity: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUserEntity) }
    }
//
//    fun update(favoriteUserEntity: FavoriteUserEntity) {
//        executorService.execute { mFavoriteUserDao.update(favoriteUserEntity) }
//    }
}