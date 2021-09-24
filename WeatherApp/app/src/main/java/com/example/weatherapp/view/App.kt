package com.example.weatherapp.view

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.model.database.HistoryDAO
import com.example.weatherapp.model.database.HistoryDataBase
import java.lang.IllegalStateException

class App:Application(){

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object{
        private var  appInstance:App? = null
        private var db:HistoryDataBase? = null

        private const val DB_NAME = "History.db"

        fun getHistoryDao():HistoryDAO{
            if(db ==null){
                if(appInstance==null)throw IllegalStateException("wtf")
                db = Room.databaseBuilder(
                    appInstance!!,
                    HistoryDataBase::class.java,
                    DB_NAME
                ).allowMainThreadQueries()
                    .build()
            }
            return db!!.historyDao()
        }
    }
}