package com.example.messmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Restaurant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    var showSubmissionForm by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("food_quality") }
    var rating by remember { mutableStateOf(0) }

    val feedbackCategories = listOf(
        FeedbackCategory("food_quality", "Food Quality", Icons.Default.Restaurant),
        FeedbackCategory("service", "Service", Icons.Default.RoomService),
        FeedbackCategory("cleanliness", "Cleanliness", Icons.Default.CleaningServices),
        FeedbackCategory("other", "Other", Icons.Default.Help)
    )

    // Sample previous feedback (in real app, this would come from Firebase)
    val previousFeedback = remember {
        listOf(
            Feedback("1", "user1", "user@example.com", "Great Food Quality", "The food today was excellent!", 5, "food_quality", System.currentTimeMillis() - 86400000, "reviewed"),
            Feedback("2", "user1", "user@example.com", "Service Issue", "Long waiting time during lunch", 2, "service", System.currentTimeMillis() - 172800000, "pending")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toggle between form and history
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { showSubmissionForm = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (showSubmissionForm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Submit Feedback")
            }

            TextButton(
                onClick = { showSubmissionForm = false },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!showSubmissionForm) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("My Feedback")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        if (showSubmissionForm) {
            FeedbackSubmissionForm(
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it },
                selectedCategory = selectedCategory,
                onCategoryChange = { selectedCategory = it },
                rating = rating,
                onRatingChange = { rating = it },
                feedbackCategories = feedbackCategories,
                onSubmit = {
                    // Handle feedback submission
                    title = ""
                    description = ""
                    rating = 0
                    selectedCategory = "food_quality"
                }
            )
        } else {
            FeedbackHistoryList(feedbackList = previousFeedback)
        }
    }
}

@Composable
fun FeedbackSubmissionForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    feedbackCategories: List<FeedbackCategory>,
    onSubmit: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Submit Your Feedback",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Help us improve our services by sharing your feedback",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            // Category Selection
            Text(
                text = "Feedback Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                feedbackCategories.chunked(2).forEach { categoryRow ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categoryRow.forEach { category ->
                            FilterChip(
                                onClick = { onCategoryChange(category.id) },
                                label = { Text(category.name) },
                                selected = selectedCategory == category.id,
                                leadingIcon = {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        item {
            // Rating
            Text(
                text = "Rating",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingBar(
                rating = rating,
                onRatingChange = onRatingChange
            )
        }

        item {
            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Feedback Title") },
                placeholder = { Text("Brief title for your feedback") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        item {
            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                placeholder = { Text("Detailed description of your feedback") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5
            )
        }

        item {
            // Submit Button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && description.isNotBlank() && rating > 0
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Submit Feedback",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun FeedbackHistoryList(feedbackList: List<Feedback>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Your Previous Feedback",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(feedbackList) { feedback ->
            FeedbackHistoryCard(feedback = feedback)
        }
    }
}

@Composable
fun FeedbackHistoryCard(feedback: Feedback) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = feedback.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                StatusChip(status = feedback.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = feedback.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < feedback.rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (index < feedback.rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = feedback.category.replace("_", " ").replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    maxRating: Int = 5
) {
    Row {
        repeat(maxRating) { index ->
            IconButton(
                onClick = { onRatingChange(index + 1) }
            ) {
                Icon(
                    imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Rating ${index + 1}",
                    tint = if (index < rating) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (color, text) = when (status) {
        "pending" -> Color(0xFFFF9800) to "Pending"
        "reviewed" -> Color(0xFF2196F3) to "Reviewed"
        "resolved" -> Color(0xFF4CAF50) to "Resolved"
        else -> MaterialTheme.colorScheme.onSurfaceVariant to "Unknown"
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

data class FeedbackCategory(
    val id: String,
    val name: String,
    val icon: ImageVector
)