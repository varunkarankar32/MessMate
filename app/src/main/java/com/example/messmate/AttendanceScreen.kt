package com.example.messmate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.libraries.intelligence.acceleration.Analytics
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Sample attendance data (in real app, this would come from Firebase)
    val sampleAttendanceData = remember {
        mutableStateMapOf(
            "breakfast" to false,
            "lunch" to true,
            "dinner" to false
        )
    }

    val mealTimes = listOf(
        Triple("Breakfast", "08:00 AM - 10:00 AM", "breakfast"),
        Triple("Lunch", "12:00 PM - 02:00 PM", "lunch"),
        Triple("Dinner", "07:00 PM - 09:00 PM", "dinner")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Date Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Mark Your Attendance",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mark your attendance for meals to help reduce food wastage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Status
        AttendanceStatsCard(attendanceData = sampleAttendanceData)

        Spacer(modifier = Modifier.height(16.dp))

        // Meal Attendance List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mealTimes) { (mealName, timings, mealKey) ->
                AttendanceMealCard(
                    mealName = mealName,
                    timings = timings,
                    isAttending = sampleAttendanceData[mealKey] ?: false,
                    onAttendanceChange = { isAttending ->
                        sampleAttendanceData[mealKey] = isAttending
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                // Handle attendance submission
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submit Attendance",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AttendanceStatsCard(attendanceData: Map<String, Boolean>) {
    val attendingCount = attendanceData.values.count { it }
    val totalMeals = attendanceData.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // In your navigation drawer item


            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Today's Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "$attendingCount out of $totalMeals meals",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${(attendingCount.toFloat() / totalMeals * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun AttendanceMealCard(
    mealName: String,
    timings: String,
    isAttending: Boolean,
    onAttendanceChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Meal Icon
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (mealName.lowercase()) {
                        "breakfast" -> Color(0xFFFFE0B2)
                        "lunch" -> Color(0xFFE8F5E8)
                        "dinner" -> Color(0xFFE3F2FD)
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    }
                )
            ) {
                Icon(
                    imageVector = when (mealName.lowercase()) {
                        "breakfast" -> Icons.Default.Face
                        "lunch" -> Icons.Default.Face
                        "dinner" -> Icons.Default.Face
                        else -> Icons.Default.Face
                    },
                    contentDescription = mealName,
                    modifier = Modifier.padding(12.dp),
                    tint = when (mealName.lowercase()) {
                        "breakfast" -> Color(0xFFFF8F00)
                        "lunch" -> Color(0xFF4CAF50)
                        "dinner" -> Color(0xFF2196F3)
                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Meal Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mealName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = timings,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Attendance Switch
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    checked = isAttending,
                    onCheckedChange = onAttendanceChange
                )

                Text(
                    text = if (isAttending) "Attending" else "Not Attending",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isAttending) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}