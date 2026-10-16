package com.example.mobiledev_maat.models

import android.app.Application
import androidx.room.Room

class IronGridApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "irongrid.db"
        ).build()
    }

    val repository: DayLogRepository by lazy {
        DayLogRepository(database.dayLogDao())
    }
}