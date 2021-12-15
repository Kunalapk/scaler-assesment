package com.base_module.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base_module.database.dao.AppDao
import com.base_module.model.VideoModel

@Database(entities = [VideoModel::class], version = 1, exportSchema = false)
abstract class BaseAppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao


    companion object {
        private val TAG = BaseAppDatabase::class.java.simpleName
        private val LOCK = Any()
        private val DATABASE_NAME = "kunal_assesment_scaler_db"
        private var mInstance: BaseAppDatabase? = null

        fun getInstance(context: Context): BaseAppDatabase {
            if (mInstance == null) {
                synchronized(LOCK) {
                    mInstance = Room.databaseBuilder(context.applicationContext, BaseAppDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return mInstance!!
        }
    }

}