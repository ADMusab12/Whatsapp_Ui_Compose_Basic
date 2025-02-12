package com.codetech.composebasics.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.codetech.composebasics.R
import com.codetech.composebasics.abstraction.CallData
import com.codetech.composebasics.abstraction.ChatData
import com.codetech.composebasics.abstraction.StatusData
import com.codetech.composebasics.ui.theme.WhatsAppBlueTheme
import com.codetech.composebasics.ui.theme.WhatsAppGreenTheme
import com.codetech.composebasics.ui.theme.WhatsAppOrangeTheme
import com.codetech.composebasics.ui.theme.WhatsAppPinkTheme
import com.codetech.composebasics.ui.theme.WhatsAppPurpleTheme
import com.codetech.composebasics.utils.Extension.changeStatusBarColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        changeStatusBarColor(window = window, color = R.color.white)
        super.onCreate(savedInstanceState)
        setContent {
            Whatsapp()
        }
    }
}

@Composable
fun Whatsapp() {
    val navController = rememberNavController()
    var currentTheme by remember { mutableStateOf(WhatsAppGreenTheme) }

    MaterialTheme(
        colorScheme = currentTheme
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "chats",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("chats") { ChatsScreen() }
                composable("status") { StatusScreen(navController) }
                composable("calls") { CallsScreen(navController) }
                composable("themes") { ThemesScreen ({ theme -> currentTheme = theme },navController) }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val items = listOf("chats", "status", "calls", "themes")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item
                val selectedColor = MaterialTheme.colorScheme.primary
                val unselectedColor = Color.Gray
                val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f, label = "")

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) selectedColor.copy(alpha = 0.1f) else Color.Transparent)
                        .clickable {
                            navController.navigate(item) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = when (item) {
                                "chats" -> R.drawable.ic_chat
                                "status" -> R.drawable.ic_status
                                "calls" -> R.drawable.ic_contact
                                "themes" -> R.drawable.ic_theme
                                else -> R.drawable.ic_chat
                            }
                        ),
                        contentDescription = item,
                        tint = if (isSelected) selectedColor else unselectedColor,
                        modifier = Modifier
                            .size(28.dp)
                            .scale(scale)
                    )
                    Text(
                        text = item.replaceFirstChar { it.uppercaseChar() },
                        fontSize = 12.sp,
                        color = if (isSelected) selectedColor else unselectedColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun ChatsScreen() {
    val chats = listOf(
        ChatData("User One", "User Two: 🎧 Sticker", "9:49", R.drawable.ic_user, 2, isTyping = false),
        ChatData("User Two", "typing...", "9:45", R.drawable.ic_user, 0, isTyping = true),
        ChatData("User Three", "📹 Best breakfast ever", "9:37", R.drawable.ic_user, 0, isTyping = false),
        ChatData("User Four", "User Five: She is so cute 😍", "9:09", R.drawable.ic_user, 5, isTyping = false),
        ChatData("User Five", "✅ It was so great to see you! Let's...", "8:58",
            R.drawable.ic_user, 0, isTyping = false)
    )
    LazyColumn {
        items(chats) { chat ->
            ChatItem(chat)
        }
    }
}

@Composable
fun ChatItem(chat: ChatData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        //use of coil
        Image(
            painter = rememberAsyncImagePainter(chat.avatar),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = if (chat.isTyping) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = if (chat.isTyping) FontWeight.Bold else FontWeight.Normal
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
            if (chat.unreadCount > 0) {
                Surface(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = chat.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusScreen(navController: NavHostController) {

    BackHandler {
        navController.navigate("chats")
    }
    val recentStatuses = listOf(
        StatusData("User One", "Today 4:37 pm", R.drawable.ic_user, isViewed = false),
        StatusData("User Two", "Today 4:27 pm", R.drawable.ic_user, isViewed = false),
        StatusData("User Three", "Today 2:37 pm", R.drawable.ic_user, isViewed = false),
        StatusData("User Four", "Today 2:17 pm", R.drawable.ic_user, isViewed = false),
        StatusData("User Five", "Today 2:07 pm", R.drawable.ic_user, isViewed = false)
    )

    val viewedStatuses = listOf(
        StatusData("User Six", "Today 1:37 pm", R.drawable.ic_user, isViewed = true),
        StatusData("User Seven", "Today 1:07 pm", R.drawable.ic_user, isViewed = true),
        StatusData("User Eight", "Today 12:37 pm", R.drawable.ic_user, isViewed = true)
    )

    BackHandler {
        navController.navigate("chats")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "Recent Updates",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(recentStatuses) { status ->
            StatusItem(status)
        }

        item {
            Text(
                text = "Viewed Updates",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(viewedStatuses) { status ->
            StatusItem(status)
        }
    }
}

@Composable
fun StatusItem(status: StatusData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {}
    ) {
        Box(modifier = Modifier.size(50.dp)) {
            Surface(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (status.isViewed) Color.Gray else MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                color = Color.LightGray
            ) {
                Image(
                    painter = painterResource(id = status.avatar),
                    contentDescription = "Status Picture",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = status.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = status.timestamp, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
@Composable
fun CallsScreen(navController: NavHostController) {
    val favoriteCalls = listOf(
        CallData("User One", R.drawable.ic_user, true),
        CallData("User Three", R.drawable.ic_user, true),
        CallData("User Two", R.drawable.ic_user, true)
    )

    val recentCalls = listOf(
        CallData("User Four", R.drawable.ic_user, false, "Today, 11:39 AM")
    )

    BackHandler {
        navController.navigate("chats")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "Favorites",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(favoriteCalls) { call ->
            CallItem(call, isFavorite = true)
        }

        item {
            Text(
                text = "Recent",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        items(recentCalls) { call ->
            CallItem(call, isFavorite = false)
        }
    }
}

@Composable
fun CallItem(call: CallData, isFavorite: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {}
    ) {
        Image(
            painter = painterResource(id = call.avatar),
            contentDescription = "Caller Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = call.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            call.timestamp?.let {
                Text(text = it, fontSize = 14.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call Icon",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}


@Composable
fun ThemesScreen(onThemeSelected: (ColorScheme) -> Unit,navController: NavHostController) {
    val themes = listOf(
        WhatsAppGreenTheme,
        WhatsAppBlueTheme,
        WhatsAppPinkTheme,
        WhatsAppPurpleTheme,
        WhatsAppOrangeTheme,
    )

    var selectedTheme by remember { mutableStateOf(themes.first()) }

    BackHandler {
        navController.navigate("chats")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Your Theme",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Choose a theme to personalize your experience",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            themes.forEach { theme ->
                val isSelected = theme == selectedTheme
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = tween(durationMillis = 300), label = ""
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedTheme = theme
                        onThemeSelected(theme)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                            .scale(scale),
                        color = theme.primary,
                        shape = RoundedCornerShape(20.dp)
                    ) {}

                }
            }
        }
    }
}

@Composable
fun ChatIcons(id: Int, description: String) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = description,
        modifier = Modifier.size(25.dp)
    )
}
