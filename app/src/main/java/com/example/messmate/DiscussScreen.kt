package com.example.messmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListScope // (Note: You usually don't need this import directly)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussScreen() {
    var selectedCategory by remember { mutableStateOf("all") }
    var showNewPostDialog by remember { mutableStateOf(false) }
    var newPostTitle by remember { mutableStateOf("") }
    var newPostContent by remember { mutableStateOf("") }
    var newPostCategory by remember { mutableStateOf("general") }

    val categories = listOf(
        "all" to "All",
        "general" to "General",
        "complaints" to "Complaints",
        "suggestions" to "Suggestions",
        "announcements" to "Announcements"
    )

    // Sample discussions (in real app, this would come from Firebase)
    val sampleDiscussions = remember {
        listOf(
            Discussion(
                "1", "user1", "user1@example.com", "John Doe",
                "Suggestion for weekend menu", 
                "Can we have some special dishes during weekends? Maybe pizza or biryani?",
                System.currentTimeMillis() - 3600000, emptyList(), 12, "suggestions"
            ),
            Discussion(
                "2", "user2", "user2@example.com", "Jane Smith",
                "Water issue in mess hall",
                "The water cooler near table 5 is not working properly. Please fix it.",
                System.currentTimeMillis() - 7200000, 
                listOf(
                    DiscussionReply("r1", "admin", "admin@messmate.com", "Admin", "Thanks for reporting. We'll fix it by tomorrow.", System.currentTimeMillis() - 3600000)
                ), 
                5, "complaints"
            ),
            Discussion(
                "3", "admin", "admin@messmate.com", "Mess Manager",
                "New timings for dinner",
                "Starting from next week, dinner will be served from 7:30 PM to 9:30 PM instead of 7:00 PM to 9:00 PM.",
                System.currentTimeMillis() - 86400000, emptyList(), 8, "announcements"
            )
        )
    }

    val filteredDiscussions = if (selectedCategory == "all") {
        sampleDiscussions
    } else {
        sampleDiscussions.filter { it.category == selectedCategory }
    }

    if (showNewPostDialog) {
        NewPostDialog(
            title = newPostTitle,
            onTitleChange = { newPostTitle = it },
            content = newPostContent,
            onContentChange = { newPostContent = it },
            selectedCategory = newPostCategory,
            onCategoryChange = { newPostCategory = it },
            categories = categories.drop(1), // Remove "all" from new post categories
            onDismiss = { 
                showNewPostDialog = false
                newPostTitle = ""
                newPostContent = ""
                newPostCategory = "general"
            },
            onPost = {
                // Handle new post submission
                showNewPostDialog = false
                newPostTitle = ""
                newPostContent = ""
                newPostCategory = "general"
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with categories and new post button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
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
                        text = "Community Discussions",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    FloatingActionButton(
                        onClick = { showNewPostDialog = true },
                        modifier = Modifier.size(40.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Post"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category filters
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (categoryId, categoryName) ->
                        FilterChip(
                            onClick = { selectedCategory = categoryId },
                            label = { Text(categoryName) },
                            selected = selectedCategory == categoryId
                        )
                    }
                }
            }
        }

        // Discussions List
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredDiscussions) { discussion ->
                DiscussionCard(discussion = discussion)
            }
        }
    }
}

@Composable
fun DiscussionCard(discussion: Discussion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with user info and category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User Avatar
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = discussion.userName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = formatTimestamp(discussion.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                CategoryChip(category = discussion.category)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title and Content
            Text(
                text = discussion.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = discussion.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer with likes and replies
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* Handle like */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "${discussion.likes}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Comments",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${discussion.replies.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = { /* Handle reply */ }
                ) {
                    Text("Reply")
                }
            }

            // Show replies if any
            if (discussion.replies.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                discussion.replies.forEach { reply ->
                    ReplyCard(reply = reply)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ReplyCard(reply: DiscussionReply) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = reply.userName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = formatTimestamp(reply.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reply.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    val (color, text) = when (category) {
        "general" -> Color(0xFF2196F3) to "General"
        "complaints" -> Color(0xFFF44336) to "Complaints"
        "suggestions" -> Color(0xFF4CAF50) to "Suggestions"
        "announcements" -> Color(0xFFFF9800) to "Announcements"
        else -> MaterialTheme.colorScheme.primary to "Other"
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

@Composable
fun NewPostDialog(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    categories: List<Pair<String, String>>,
    onDismiss: () -> Unit,
    onPost: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Discussion Post") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = onContentChange,
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                categories.forEach { (categoryId, categoryName) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedCategory == categoryId,
                            onClick = { onCategoryChange(categoryId) }
                        )
                        Text(categoryName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onPost,
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LazyRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: LazyListScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
}