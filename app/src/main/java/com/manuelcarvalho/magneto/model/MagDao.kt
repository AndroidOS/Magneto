package com.manuelcarvalho.magneto.model

import androidx.room.Dao
import androidx.room.Insert


@Dao
interface MagDao {

    @Insert
    suspend fun insertAll(vararg readings: Mag): List<Long>

    @androidx.room.Query("SELECT * FROM mag")
    suspend fun getAllReadings(): List<Mag>

    @androidx.room.Query("SELECT * FROM mag WHERE uuid = :magId")
    suspend fun getReading(magId: Int): Mag

    @androidx.room.Query("DELETE FROM mag")
    suspend fun deleteAllReadings()
}