package com.example.weatherapp.model.database

import androidx.room.*


@Dao
interface HistoryDAO {

    @Query("SELECT*FROM HistoryEntity")
    fun all():List<HistoryEntity>

    @Query("SELECT*FROM HistoryEntity WHERE city LIKE:city")
    fun getDataByWorld(city:String):List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}