package com.example.mobiledev_maat.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

data class DayLogModel(
    val date: LocalDate,
    val workoutType: String,
    val exercises: List<ExerciseEntry> = emptyList()
) {
    val muscles: List<String>
        get() = exercises
            .flatMap { musclesForExercise(it.name) }
            .distinct()
            .sorted()

    val totalVolume: Double get() = exercises.sumOf { it.totalVolume }
    val exerciseCount: Int get() = exercises.size
    val setCount: Int get() = exercises.sumOf { it.sets.size }
}

data class ExerciseEntry(
    val name: String,
    val sets: List<SetEntry> = emptyList()
) {
    val totalVolume: Double get() = sets.sumOf { it.weightKg * it.reps }
    val bestWeight: Double get() = sets.maxOfOrNull { it.weightKg } ?: 0.0
}

data class SetEntry(val weightKg: Double, val reps: Int)

@Entity(tableName = "day_logs")
data class DayLogEntity(
    @PrimaryKey val date: LocalDate,
    val workoutType: String
)

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = DayLogEntity::class,
            parentColumns = ["date"],
            childColumns = ["day_date"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("day_date")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val day_date: LocalDate,
    val name: String,
    val order_index: Int
)

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exercise_id")]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exercise_id: Long,
    val weight_kg: Double,
    val reps: Int,
    val order_index: Int
)