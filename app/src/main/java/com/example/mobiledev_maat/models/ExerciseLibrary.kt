package com.example.mobiledev_maat.models

data class LibraryExercise(
    val name: String,
    val muscles: List<String>
)

val EXERCISE_LIBRARY = listOf(
    LibraryExercise("Bench Press", listOf("Chest", "Triceps", "Shoulders")),
    LibraryExercise("Incline Bench Press", listOf("Chest", "Shoulders", "Triceps")),
    LibraryExercise("Decline Bench Press", listOf("Chest", "Triceps")),
    LibraryExercise("Dumbbell Bench Press", listOf("Chest", "Triceps", "Shoulders")),
    LibraryExercise("Incline DB Press", listOf("Chest", "Shoulders", "Triceps")),
    LibraryExercise("Cable Fly", listOf("Chest")),
    LibraryExercise("Pec Deck", listOf("Chest")),
    LibraryExercise("Push-Up", listOf("Chest", "Triceps", "Shoulders")),
    LibraryExercise("Dips", listOf("Chest", "Triceps")),

    LibraryExercise("Deadlift", listOf("Lower Back", "Glutes", "Hamstrings", "Traps", "Lats")),
    LibraryExercise("Barbell Row", listOf("Lats", "Upper Back", "Biceps")),
    LibraryExercise("Dumbbell Row", listOf("Lats", "Upper Back", "Biceps")),
    LibraryExercise("Pull-Up", listOf("Lats", "Biceps", "Upper Back")),
    LibraryExercise("Chin-Up", listOf("Lats", "Biceps")),
    LibraryExercise("Lat Pulldown", listOf("Lats", "Biceps")),
    LibraryExercise("Seated Cable Row", listOf("Lats", "Upper Back", "Biceps")),
    LibraryExercise("T-Bar Row", listOf("Lats", "Upper Back", "Biceps")),
    LibraryExercise("Face Pull", listOf("Traps", "Shoulders", "Upper Back")),
    LibraryExercise("Shrug", listOf("Traps")),
    LibraryExercise("Back Extension", listOf("Lower Back", "Glutes")),

    LibraryExercise("Overhead Press", listOf("Shoulders", "Triceps")),
    LibraryExercise("Dumbbell Shoulder Press", listOf("Shoulders", "Triceps")),
    LibraryExercise("Lateral Raise", listOf("Shoulders")),
    LibraryExercise("Front Raise", listOf("Shoulders")),
    LibraryExercise("Rear Delt Fly", listOf("Shoulders", "Upper Back")),
    LibraryExercise("Arnold Press", listOf("Shoulders", "Triceps")),
    LibraryExercise("Upright Row", listOf("Shoulders", "Traps")),

    LibraryExercise("Barbell Curl", listOf("Biceps")),
    LibraryExercise("Dumbbell Curl", listOf("Biceps")),
    LibraryExercise("Hammer Curl", listOf("Biceps", "Forearms")),
    LibraryExercise("Preacher Curl", listOf("Biceps")),
    LibraryExercise("Concentration Curl", listOf("Biceps")),
    LibraryExercise("Cable Curl", listOf("Biceps")),

    LibraryExercise("Triceps Pushdown", listOf("Triceps")),
    LibraryExercise("Overhead Triceps Extension", listOf("Triceps")),
    LibraryExercise("Skull Crusher", listOf("Triceps")),
    LibraryExercise("Close-Grip Bench Press", listOf("Triceps", "Chest")),
    LibraryExercise("Triceps Kickback", listOf("Triceps")),

    LibraryExercise("Wrist Curl", listOf("Forearms")),
    LibraryExercise("Reverse Wrist Curl", listOf("Forearms")),
    LibraryExercise("Farmer's Walk", listOf("Forearms", "Traps", "Abdominals")),

    LibraryExercise("Back Squat", listOf("Quadriceps", "Glutes", "Hamstrings")),
    LibraryExercise("Front Squat", listOf("Quadriceps", "Glutes")),
    LibraryExercise("Goblet Squat", listOf("Quadriceps", "Glutes")),
    LibraryExercise("Leg Press", listOf("Quadriceps", "Glutes", "Hamstrings")),
    LibraryExercise("Leg Extension", listOf("Quadriceps")),
    LibraryExercise("Leg Curl", listOf("Hamstrings")),
    LibraryExercise("Romanian Deadlift", listOf("Hamstrings", "Glutes", "Lower Back")),
    LibraryExercise("Stiff-Leg Deadlift", listOf("Hamstrings", "Lower Back")),
    LibraryExercise("Bulgarian Split Squat", listOf("Quadriceps", "Glutes")),
    LibraryExercise("Lunge", listOf("Quadriceps", "Glutes", "Hamstrings")),
    LibraryExercise("Hip Thrust", listOf("Glutes", "Hamstrings")),
    LibraryExercise("Glute Bridge", listOf("Glutes")),
    LibraryExercise("Calf Raise", listOf("Calves")),
    LibraryExercise("Seated Calf Raise", listOf("Calves")),
    LibraryExercise("Hip Abduction", listOf("Abductors")),
    LibraryExercise("Hip Adduction", listOf("Adductors")),

    LibraryExercise("Plank", listOf("Abdominals")),
    LibraryExercise("Crunch", listOf("Abdominals")),
    LibraryExercise("Sit-Up", listOf("Abdominals")),
    LibraryExercise("Hanging Leg Raise", listOf("Abdominals")),
    LibraryExercise("Russian Twist", listOf("Abdominals")),
    LibraryExercise("Cable Crunch", listOf("Abdominals")),
    LibraryExercise("Ab Wheel", listOf("Abdominals")),
    LibraryExercise("Side Plank", listOf("Abdominals")),

    LibraryExercise("Running", listOf("Quadriceps", "Hamstrings", "Calves")),
    LibraryExercise("Rowing Machine", listOf("Lats", "Upper Back", "Quadriceps")),
    LibraryExercise("Cycling", listOf("Quadriceps", "Hamstrings", "Calves")),
    LibraryExercise("Jump Rope", listOf("Calves", "Quadriceps")),
    LibraryExercise("Burpee", listOf("Chest", "Quadriceps", "Abdominals"))
)

fun musclesForExercise(name: String): List<String> =
    EXERCISE_LIBRARY
        .firstOrNull { it.name.equals(name, ignoreCase = true) }
        ?.muscles
        ?: emptyList()

fun searchLibrary(query: String): List<LibraryExercise> =
    if (query.isBlank()) EXERCISE_LIBRARY
    else EXERCISE_LIBRARY.filter { it.name.contains(query, ignoreCase = true) }