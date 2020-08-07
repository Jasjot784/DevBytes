package com.jasjotsingh.devbytes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jasjotsingh.devbytes.database.VideosDatabase
import com.jasjotsingh.devbytes.database.asDomainModel
import com.jasjotsingh.devbytes.domain.DevByteVideo
import com.jasjotsingh.devbytes.network.DevByteNetwork
import com.jasjotsingh.devbytes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class VideosRepository(private val database: VideosDatabase) {
    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()}
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh videos is called");
            val playlist = DevByteNetwork.devbytes.getPlaylist().await()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }
}

