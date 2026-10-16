package com.example.mobiledev_maat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiledev_maat.models.DayLogViewModel
import com.example.mobiledev_maat.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun GridScreen(
    viewModel: DayLogViewModel,
    onDayClick: (LocalDate) -> Unit,
    onRecoveryClick: () -> Unit
) {
    val logs by viewModel.logs.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val today = LocalDate.now()
    val days = remember(currentMonth) { buildMonthDays(currentMonth) }

    Box(Modifier.fillMaxSize().background(Background)) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 24.dp)
        ) {
            Column(Modifier.padding(bottom = 20.dp)) {
                Text("IronGrid", fontSize = 30.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                Text(
                    "Track your lifts. Know what to train.",
                    fontSize = 12.sp, color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Accent)
                    .clickable { onRecoveryClick() }
                    .padding(16.dp)
            ) {
                Text(
                    "TODAY'S RECOMMENDATION",
                    fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                    color = Background.copy(alpha = 0.65f), letterSpacing = 1.2.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    viewModel.recommendedMessage(),
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Background
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface)
                    .clickable { onRecoveryClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(AccentDim),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Accent)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Recovery Dashboard", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text("See which muscles are ready", color = TextSecondary, fontSize = 12.sp)
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.previousMonth() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev", tint = TextPrimary)
                }
                Text(
                    currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f), textAlign = TextAlign.Center
                )
                IconButton(onClick = { viewModel.nextMonth() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", tint = TextPrimary)
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                    Text(
                        it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                        color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(days) { date ->
                    if (date == null) Box(Modifier.aspectRatio(1f))
                    else {
                        val log = logs[date]
                        val isToday = date == today
                        val bgColor = when {
                            log != null -> Accent
                            date.isBefore(today) -> Missed
                            else -> SurfaceV2
                        }
                        val textColor = when {
                            log != null -> Background
                            isToday -> Accent
                            else -> TextSecondary
                        }
                        Box(
                            Modifier.aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .clickable { onDayClick(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    date.dayOfMonth.toString(),
                                    color = textColor, fontSize = 14.sp,
                                    fontWeight = if (log != null || isToday) FontWeight.Bold else FontWeight.Normal
                                )
                                if (log != null) {
                                    Spacer(Modifier.height(2.dp))
                                    Box(Modifier.size(4.dp).clip(CircleShape).background(Background))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }

        FloatingActionButton(
            onClick = { onDayClick(today) },
            containerColor = Accent, contentColor = Background,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
        ) { Icon(Icons.Default.Add, contentDescription = "Log today") }
    }
}

private fun buildMonthDays(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val offset = firstDay.dayOfWeek.value % 7
    val list = mutableListOf<LocalDate?>()
    repeat(offset) { list.add(null) }
    for (day in 1..daysInMonth) list.add(month.atDay(day))
    return list
}