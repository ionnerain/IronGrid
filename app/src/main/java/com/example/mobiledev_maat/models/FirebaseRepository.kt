package com.example.mobiledev_maat.models

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addWorkout() {
        val workout = hashMapOf(
            "workoutType" to "Push Day",
            "date" to "2026-01-15"
        )

        db.collection("col_workouts")
            .add(workout)
            .addOnSuccessListener { println("Workout has been saved") }
            .addOnFailureListener { println("Error encountered while saving") }
    }
}