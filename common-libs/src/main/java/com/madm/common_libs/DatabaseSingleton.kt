package com.madm.common_libs

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [RandomEntity::class], version = 1)
abstract class DatabaseSingleton : RoomDatabase() {
    abstract fun entityDAO() : DAORandomEntity

    companion object{
        private var db : DatabaseSingleton? = null

        public fun getInstance (context : Context) : DatabaseSingleton{
            if(db == null){
                db = databaseBuilder(
                    context,
                    DatabaseSingleton::class.java,
                    "some-database.db"
                ).createFromAsset("some-database.db").build()
            }
            return db as DatabaseSingleton
        }
    }
}