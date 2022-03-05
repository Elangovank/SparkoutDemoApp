package com.elango.demoapp.localstorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel

@Database(
    entities = [MapModel::class], version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getMapModelDao(): MapDataDAO
}


class DatabaseClient(var mContext: Context) {
    fun getAppDatabase(): AppDataBase {
        return Room.databaseBuilder(
            mContext,
            AppDataBase::class.java,
            "demo"
        )
            .allowMainThreadQueries()
            .build()
    }
}