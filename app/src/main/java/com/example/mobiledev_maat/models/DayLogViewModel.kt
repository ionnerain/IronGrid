package com.example.mobiledev_maat.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class DayLogViewModel(private val repository: DayLogRepository) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    val logs: StateFlow<Map<LocalDate, DayLogModel>> = repository.getAllLogs()
        .map { list -> list.associateBy { it.date } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun saveLog(log: DayLogModel) {
        viewModelScope.launch { repository.saveLog(log) }
    }

    fun deleteLog(date: LocalDate) {
        viewModelScope.launch { repository.deleteLog(date) }
    }

    fun getLog(date: LocalDate): DayLogModel? = logs.value[date]

    fun previousMonth() { _currentMonth.value = _currentMonth.value.minusMonths(1) }
    fun nextMonth() { _currentMonth.value = _currentMonth.value.plusMonths(1) }

    fun workoutCountForMonth(): Int {
        val month = _currentMonth.value
        return logs.value.keys.count { YearMonth.from(it) == month }
    }

    fun allLogsSorted(): List<DayLogModel> =
        logs.value.values.sortedByDescending { it.date }

    data class ExerciseSession(
        val date: LocalDate,
        val workoutType: String,
        val sets: List<SetEntry>
    )

    fun exerciseHistory(exercise: String): List<ExerciseSession> {
        if (exercise.isBlank()) return emptyList()
        return logs.value.values.sortedByDescending { it.date }.mapNotNull { log ->
            val entry = log.exercises.firstOrNull {
                it.name.equals(exercise, ignoreCase = true)
            } ?: return@mapNotNull null
            ExerciseSession(log.date, log.workoutType, entry.sets)
        }
    }

    fun allExercises(): List<String> =
        logs.value.values
            .flatMap { it.exercises }
            .map { it.name }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

    fun personalRecord(exercise: String): Double? =
        exerciseHistory(exercise).flatMap { it.sets }.maxOfOrNull { it.weightKg }

    fun isNewPR(exercise: String, weightKg: Double, excludeDate: LocalDate? = null): Boolean {
        if (exercise.isBlank() || weightKg <= 0.0) return false
        val previous = exerciseHistory(exercise)
            .filter { it.date != excludeDate }
            .flatMap { it.sets }
            .maxOfOrNull { it.weightKg } ?: 0.0
        return weightKg > previous
    }

    fun daysSinceTrained(muscle: String): Long? {
        val lastDate = logs.value.values.filter { muscle in it.muscles }
            .maxOfOrNull { it.date } ?: return null
        return ChronoUnit.DAYS.between(lastDate, LocalDate.now())
    }

    fun recoveryProgress(muscle: String): Float {
        val days = daysSinceTrained(muscle) ?: return 1f
        val window = recoveryDaysFor(muscle).toDouble()
        return (days / window).coerceIn(0.0, 1.0).toFloat()
    }

    fun recommendedMuscle(): String? =
        ALL_MUSCLES.maxByOrNull { recoveryProgress(it) }
            ?.takeIf { recoveryProgress(it) >= 0.6f }

    fun recommendedMessage(): String {
        val muscle = recommendedMuscle()
        return if (muscle == null) {
            "Log your first workout to get started"
        } else {
            val days = daysSinceTrained(muscle)
            if (days == null) "$muscle — never trained, good time to start"
            else "$muscle — last trained $days days ago, fully recovered"
        }
    }
}