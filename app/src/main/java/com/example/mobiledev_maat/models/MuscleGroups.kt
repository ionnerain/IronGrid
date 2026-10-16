package com.example.mobiledev_maat.models

data class MuscleRegion(val name: String, val muscles: List<String>)

val MUSCLE_REGIONS = listOf(
    MuscleRegion(
        "Upper Body",
        listOf(
            "Abdominals", "Biceps", "Chest", "Forearms",
            "Lats", "Lower Back", "Shoulders",
            "Traps", "Triceps", "Upper Back"
        )
    ),
    MuscleRegion(
        "Lower Body",
        listOf(
            "Abductors", "Adductors", "Calves",
            "Glutes", "Hamstrings", "Quadriceps"
        )
    )
)

val ALL_MUSCLES: List<String> = MUSCLE_REGIONS.flatMap { it.muscles }

val MUSCLE_RECOVERY_DAYS: Map<String, Int> = mapOf(
    "Quadriceps" to 3,
    "Hamstrings" to 3,
    "Glutes" to 3,
    "Lats" to 3,
    "Upper Back" to 3,
    "Lower Back" to 3,
    "Chest" to 3,
    "Traps" to 3,
    "Shoulders" to 2,
    "Triceps" to 2,
    "Calves" to 2,
    "Biceps" to 1,
    "Forearms" to 1,
    "Abdominals" to 1,
    "Abductors" to 1,
    "Adductors" to 1
)

fun recoveryDaysFor(muscle: String): Int = MUSCLE_RECOVERY_DAYS[muscle] ?: 2