package com.example.messmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Add this data class for menu items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessMenuScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedMealType by remember { mutableStateOf("All") }

    // Sample menu items
    val sampleMenuItems = remember {
        listOf(
            MenuItem("1", "Aloo Paratha", "breakfast", 25.0, "Delicious stuffed paratha with potato", true, selectedDate.toString()),
            MenuItem("2", "Chole Bhature", "breakfast", 35.0, "Spicy chickpeas with fried bread", true, selectedDate.toString()),
            MenuItem("3", "Dal Rice", "lunch", 40.0, "Traditional dal with steamed rice", true, selectedDate.toString()),
            MenuItem("4", "Chicken Curry", "lunch", 80.0, "Spicy chicken curry with rice", true, selectedDate.toString()),
            MenuItem("5", "Roti Sabzi", "dinner", 30.0, "Fresh roti with seasonal vegetables", true, selectedDate.toString()),
            MenuItem("6", "Biryani", "dinner", 90.0, "Aromatic basmati rice with spices", true, selectedDate.toString())
        )
    }

    val mealTypes = listOf("All", "Breakfast", "Lunch", "Dinner")
    val filteredItems = if (selectedMealType == "All") sampleMenuItems
    else sampleMenuItems.filter { it.category.equals(selectedMealType, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Date and Filter Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Today's Menu",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Meal Type Filter
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mealTypes) { mealType ->
                        FilterChip(
                            onClick = { selectedMealType = mealType },
                            label = { Text(mealType) },
                            selected = selectedMealType == mealType
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { menuItem ->
                MenuItemCard(menuItem = menuItem)
            }
        }
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem) {
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
            // Meal Type Icon
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (menuItem.category.lowercase()) {
                        "breakfast" -> Color(0xFFFFE0B2)
                        "lunch" -> Color(0xFFE8F5E8)
                        "dinner" -> Color(0xFFE3F2FD)
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    }
                )
            ) {
                Icon(
                    imageVector = when (menuItem.category.lowercase()) {
                        "breakfast" -> Icons.Filled.LightMode
                        "lunch" -> Icons.Filled.WbSunny
                        "dinner" -> Icons.Filled.NightlightRound
                        else -> Icons.Filled.Restaurant
                    },
                    contentDescription = menuItem.category,
                    modifier = Modifier.padding(8.dp),
                    tint = when (menuItem.category.lowercase()) {
                        "breakfast" -> Color(0xFFFF8F00)
                        "lunch" -> Color(0xFF4CAF50)
                        "dinner" -> Color(0xFF2196F3)
                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Menu Item Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = menuItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = menuItem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = menuItem.category.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Price
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${menuItem.price.toInt()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (menuItem.isAvailable) {
                    Text(
                        text = "Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50)
                    )
                } else {
                    Text(
                        text = "Not Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
