package com.example.mobiledev_maat.models

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class DayLogRepository(private val dao: DayLogDao) {

    private val db = FirebaseFirestore.getInstance()

    fun getAllLogs(): Flow<List<DayLogModel>> =
        dao.getAllDays().map { days ->
            days.map { day ->
                val exercises = dao.getExercisesForDay(day.date)
                val setsByExercise = exercises.associate { ex ->
                    ex.id to dao.getSetsForExercise(ex.id)
                }
                day.toDomain(exercises, setsByExercise)
            }
        }

    suspend fun getLog(date: LocalDate): DayLogModel? {
        val day = dao.getDay(date) ?: return null
        val exercises = dao.getExercisesForDay(date)
        val setsByExercise = exercises.associate { ex ->
            ex.id to dao.getSetsForExercise(ex.id)
        }
        return day.toDomain(exercises, setsByExercise)
    }

    suspend fun saveLog(log: DayLogModel) {
        dao.deleteExercisesForDay(log.date)
        dao.upsertDay(DayLogEntity(log.date, log.workoutType))
        log.exercises.forEachIndexed { exIdx, ex ->
            val exId = dao.insertExercise(
                ExerciseEntity(day_date = log.date, name = ex.name, order_index = exIdx)
            )
            ex.sets.forEachIndexed { setIdx, set ->
                dao.insertSet(
                    SetEntity(
                        exercise_id = exId,
                        weight_kg = set.weightKg,
                        reps = set.reps,
                        order_index = setIdx
                    )
                )
            }
        }
    }

    suspend fun deleteLog(date: LocalDate) {
        dao.deleteDay(date)
    }

    // ─── Firebase (test functions, not yet called) ──────────
    fun addWorkoutToFirebase() {
        val workout = hashMapOf(
            "date" to "2026-01-15",
            "workoutType" to "Push Day",
            "muscles" to listOf("Chest", "Triceps", "Shoulders"),
            "exercise" to "Bench Press",
            "weightKg" to 82.5,
            "reps" to 6
        )

        db.collection("col_workouts")
            .add(workout)
            .addOnSuccessListener { println("Workout has been saved") }
            .addOnFailureListener { println("Error encountered while saving") }
    }
}

private fun DayLogEntity.toDomain(
    exercises: List<ExerciseEntity>,
    setsByExercise: Map<Long, List<SetEntity>>
): DayLogModel = DayLogModel(
    date = date,
    workoutType = workoutType,
    exercises = exercises.map { ex ->
        ExerciseEntry(
            name = ex.name,
            sets = setsByExercise[ex.id]?.map {
                SetEntry(weightKg = it.weight_kg, reps = it.reps)
            } ?: emptyList()
        )
    }
)