package com.example.mobiledev_maat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiledev_maat.models.DayLogViewModel
import com.example.mobiledev_maat.models.MUSCLE_REGIONS
import com.example.mobiledev_maat.ui.theme.*

@Composable
fun RecoveryScreen(viewModel: DayLogViewModel, onBack: () -> Unit) {
    val logs by viewModel.logs.collectAsState()
    val exercises = remember<List<String>>(logs) { viewModel.allExercises() }

    Box(Modifier.fillMaxSize().background(Background)) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Spacer(Modifier.width(4.dp))
                Column {
                    Text("Recovery", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("What's ready to train", fontSize = 13.sp, color = TextSecondary)
                }
            }

            Spacer(Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                MUSCLE_REGIONS.forEach { region ->
                    item {
                        Text(
                            region.name.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                            color = Accent, letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                        )
                    }
                    items(region.muscles) { muscle ->
                        val progress = viewModel.recoveryProgress(muscle)
                        val days = viewModel.daysSinceTrained(muscle)
                        val color = when {
                            progress < 0.5f -> lerp(Danger, Warn, progress * 2f)
                            else -> lerp(Warn, Accent, (progress - 0.5f) * 2f)
                        }

                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Surface).padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(color))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(muscle, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Text(
                                    when (days) {
                                        null -> "Never trained"
                                        0L -> "Trained today"
                                        1L -> "Trained 1 day ago"
                                        else -> "Trained $days days ago"
                                    },
                                    color = TextSecondary, fontSize = 12.sp
                                )
                            }
                            Text(
                                when {
                                    progress >= 1f -> "READY"
                                    progress >= 0.6f -> "ALMOST"
                                    else -> "RECOVERING"
                                },
                                color = color, fontWeight = FontWeight.SemiBold, fontSize = 10.sp
                            )
                        }
                    }
                }

                if (exercises.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "PERSONAL RECORDS", fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                            color = TextSecondary, letterSpacing = 1.5.sp
                        )
                    }
                    items(exercises) { exercise ->
                        val pr = viewModel.personalRecord(exercise) ?: 0.0
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Surface).padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(exercise, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Text("Personal best", color = TextSecondary, fontSize = 12.sp)
                            }
                            Text("${pr}kg", color = Accent, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}