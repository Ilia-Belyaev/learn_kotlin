package com.example.weatherapp.model.database

import android.database.Cursor
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

    @Query("SELECT id,city, temperature FROM HistoryEntity ORDER BY timestamp DESC")
    fun getHistoryCursor(): Cursor

    @Query("SELECT id,city, temperature FROM HistoryEntity WHERE id=:id")
    fun getHistoryCursor(id:Long): Cursor

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)
}