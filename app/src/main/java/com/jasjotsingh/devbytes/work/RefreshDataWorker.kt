package com.jasjotsingh.devbytes.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jasjotsingh.devbytes.database.getDatabase
import com.jasjotsingh.devbytes.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)
        try {
            repository.refreshVideos( )
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
    companion object {
        const val WORK_NAME = "com.jasjotsingh.devbytes.work.RefreshDataWorker"
    }
}