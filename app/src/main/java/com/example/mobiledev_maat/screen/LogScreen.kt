package com.example.mobiledev_maat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiledev_maat.models.*
import com.example.mobiledev_maat.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    viewModel: DayLogViewModel,
    date: String,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val parsedDate = LocalDate.parse(date)
    val existing = viewModel.getLog(parsedDate)

    var workoutType by remember { mutableStateOf(existing?.workoutType ?: "") }
    var exercises by remember { mutableStateOf(existing?.exercises ?: emptyList()) }
    var pickingForIndex by remember { mutableStateOf<Int?>(null) }

    val newPRs = remember(exercises) {
        exercises.map { entry ->
            val maxWeight = entry.sets.maxOfOrNull { it.weightKg } ?: 0.0
            entry.name.isNotBlank() && viewModel.isNewPR(entry.name, maxWeight, excludeDate = parsedDate)
        }
    }

    if (pickingForIndex != null) {
        ExercisePickerDialog(
            onDismiss = { pickingForIndex = null },
            onPick = { name ->
                val idx = pickingForIndex
                if (idx != null && idx in exercises.indices) {
                    exercises = exercises.toMutableList().also {
                        it[idx] = it[idx].copy(name = name)
                    }
                }
                pickingForIndex = null
            }
        )
    }

    Box(Modifier.fillMaxSize().background(Background)) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Spacer(Modifier.width(4.dp))
                Column {
                    Text(
                        if (existing != null) "Edit Workout" else "New Workout",
                        fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                    )
                    Text(
                        parsedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                        fontSize = 13.sp, color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Label("WORKOUT TYPE")
            OutlinedTextField(
                value = workoutType, onValueChange = { workoutType = it },
                placeholder = { Text("e.g., Push Day", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), singleLine = true,
                colors = fieldColors()
            )

            Spacer(Modifier.height(20.dp))

            Label("EXERCISES")

            exercises.forEachIndexed { exIndex, entry ->
                ExerciseCard(
                    entry = entry,
                    isNewPR = newPRs.getOrNull(exIndex) == true,
                    onPickExercise = { pickingForIndex = exIndex },
                    onAddSet = {
                        exercises = exercises.toMutableList().also {
                            it[exIndex] = entry.copy(sets = entry.sets + SetEntry(0.0, 0))
                        }
                    },
                    onUpdateSet = { setIndex, newSet ->
                        exercises = exercises.toMutableList().also {
                            val sets = entry.sets.toMutableList()
                            sets[setIndex] = newSet
                            it[exIndex] = entry.copy(sets = sets)
                        }
                    },
                    onDeleteSet = { setIndex ->
                        exercises = exercises.toMutableList().also {
                            it[exIndex] = entry.copy(sets = entry.sets.filterIndexed { i, _ -> i != setIndex })
                        }
                    },
                    onDeleteExercise = {
                        exercises = exercises.filterIndexed { i, _ -> i != exIndex }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = {
                    exercises = exercises + ExerciseEntry(name = "", sets = listOf(SetEntry(0.0, 0)))
                    pickingForIndex = exercises.lastIndex
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Accent)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Accent)
                Spacer(Modifier.width(8.dp))
                Text("Add Exercise", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    if (workoutType.isNotBlank()) {
                        viewModel.saveLog(
                            DayLogModel(
                                date = parsedDate,
                                workoutType = workoutType,
                                exercises = exercises.filter { it.name.isNotBlank() }
                            )
                        )
                        onSave()
                    }
                },
                enabled = workoutType.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent, contentColor = Background,
                    disabledContainerColor = SurfaceV2, disabledContentColor = TextSecondary
                )
            ) { Text("Save Workout", fontWeight = FontWeight.Bold, fontSize = 16.sp) }

            if (existing != null) {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.deleteLog(parsedDate); onSave() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
                ) { Text("Delete", fontWeight = FontWeight.SemiBold, fontSize = 15.sp) }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary, letterSpacing = 1.sp)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
    focusedBorderColor = Accent, unfocusedBorderColor = SurfaceV2,
    cursorColor = Accent
)

@Composable
private fun ExerciseCard(
    entry: ExerciseEntry,
    isNewPR: Boolean,
    onPickExercise: () -> Unit,
    onAddSet: () -> Unit,
    onUpdateSet: (Int, SetEntry) -> Unit,
    onDeleteSet: (Int) -> Unit,
    onDeleteExercise: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Surface).padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f)) {
                OutlinedTextField(
                    value = entry.name,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Tap to pick exercise", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = fieldColors()
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onPickExercise() }
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onDeleteExercise) {
                Icon(Icons.Default.Close, contentDescription = "Delete", tint = TextSecondary)
            }
        }

        val muscles = remember(entry.name) { musclesForExercise(entry.name) }
        if (muscles.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                muscles.joinToString(" · "),
                color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium
            )
        }

        if (isNewPR) {
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.clip(RoundedCornerShape(8.dp)).background(Accent)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("NEW PR", color = Background, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Text("SET", modifier = Modifier.width(40.dp), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text("KG", modifier = Modifier.weight(1f), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text("REPS", modifier = Modifier.weight(1f), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(36.dp))
        }
        Spacer(Modifier.height(6.dp))

        entry.sets.forEachIndexed { i, set ->
            SetRow(i + 1, set, { newSet -> onUpdateSet(i, newSet) }, { onDeleteSet(i) })
            Spacer(Modifier.height(6.dp))
        }

        TextButton(onClick = onAddSet, modifier = Modifier.height(36.dp)) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Accent, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add Set", color = Accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SetRow(index: Int, set: SetEntry, onUpdate: (SetEntry) -> Unit, onDelete: () -> Unit) {
    var weightText by remember(set) { mutableStateOf(if (set.weightKg > 0) set.weightKg.toString() else "") }
    var repsText by remember(set) { mutableStateOf(if (set.reps > 0) set.reps.toString() else "") }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(index.toString(), modifier = Modifier.width(40.dp), color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

        OutlinedTextField(
            value = weightText,
            onValueChange = { input ->
                weightText = input.filter { it.isDigit() || it == '.' }
                onUpdate(set.copy(weightKg = weightText.toDoubleOrNull() ?: 0.0))
            },
            placeholder = { Text("0", color = TextSecondary) },
            modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = fieldColors()
        )

        Spacer(Modifier.width(8.dp))

        OutlinedTextField(
            value = repsText,
            onValueChange = { input ->
                repsText = input.filter { it.isDigit() }
                onUpdate(set.copy(reps = repsText.toIntOrNull() ?: 0))
            },
            placeholder = { Text("0", color = TextSecondary) },
            modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = fieldColors()
        )

        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Delete set", tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}