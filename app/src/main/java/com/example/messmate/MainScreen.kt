package com.example.messmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    selectedItemIndex = selectedItemIndex,
                    onItemClick = { index ->
                        selectedItemIndex = index
                        navController.navigate(DrawerMenuItems.items[index].route)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onLogout = onLogout
                )
            }
        },
        drawerState = drawerState
    ) {
        MainContent(
            navController = navController,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navController: NavHostController,
    onMenuClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        "mess_menu" -> "Mess Menu"
        "attendance" -> "Attendance"
        "feedback" -> "Feedback"
        "discuss" -> "Discuss"
        else -> "Mess Token System"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "mess_menu",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("mess_menu") {
                MessMenuScreen()
            }
            composable("attendance") {
                AttendanceScreen()
            }
            composable("feedback") {
                FeedbackScreen()
            }
            composable("discuss") {
                DiscussScreen()
            }
        }
    }
}

@Composable
fun DrawerContent(
    selectedItemIndex: Int,
    onItemClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        DrawerHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        DrawerMenuItems.items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(item.title) },
                selected = index == selectedItemIndex,
                onClick = { onItemClick(index) },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = onLogout,
            icon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout"
                )
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = MaterialTheme.colorScheme.error,
                unselectedIconColor = MaterialTheme.colorScheme.error
            )
        )
    }
}

@Composable
fun DrawerHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Profile Picture Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "MessMate User",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "user@example.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
// Add this data class to define drawer menu items
data class DrawerMenuItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// Define the drawer menu items object
object DrawerMenuItems {
    val items = listOf(
        DrawerMenuItem(
            title = "Mess Menu",
            route = "mess_menu",
            selectedIcon = Icons.Filled.Restaurant,
            unselectedIcon = Icons.Outlined.Restaurant
        ),
        DrawerMenuItem(
            title = "Attendance",
            route = "attendance",
            selectedIcon = Icons.Filled.Check,
            unselectedIcon = Icons.Outlined.Check
        ),
        DrawerMenuItem(
            title = "Feedback",
            route = "feedback",
            selectedIcon = Icons.Filled.Comment,
            unselectedIcon = Icons.Outlined.Comment
        ),
        DrawerMenuItem(
            title = "Discuss",
            route = "discuss",
            selectedIcon = Icons.Filled.Forum,
            unselectedIcon = Icons.Outlined.Forum
        )
    )
}
