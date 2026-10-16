package com.example.mobiledev_maat.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DayLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDay(day: DayLogEntity)

    @Query("SELECT * FROM day_logs ORDER BY date DESC")
    fun getAllDays(): Flow<List<DayLogEntity>>

    @Query("SELECT * FROM day_logs WHERE date = :date LIMIT 1")
    suspend fun getDay(date: LocalDate): DayLogEntity?

    @Query("DELETE FROM day_logs WHERE date = :date")
    suspend fun deleteDay(date: LocalDate)

    @Insert
    suspend fun insertExercise(ex: ExerciseEntity): Long

    @Query("SELECT * FROM exercises WHERE day_date = :date ORDER BY order_index ASC")
    suspend fun getExercisesForDay(date: LocalDate): List<ExerciseEntity>

    @Query("DELETE FROM exercises WHERE day_date = :date")
    suspend fun deleteExercisesForDay(date: LocalDate)

    @Insert
    suspend fun insertSet(set: SetEntity): Long

    @Query("SELECT * FROM sets WHERE exercise_id = :exId ORDER BY order_index ASC")
    suspend fun getSetsForExercise(exId: Long): List<SetEntity>

    @Query("DELETE FROM sets WHERE exercise_id = :exId")
    suspend fun deleteSetsForExercise(exId: Long)
}