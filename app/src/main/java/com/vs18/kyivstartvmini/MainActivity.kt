package com.vs18.kyivstartvmini

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.vs18.kyivstartvmini.data.model.*
import com.vs18.kyivstartvmini.datastore.AuthPreferences
import com.vs18.kyivstartvmini.ui.auth.AuthScreen
import com.vs18.kyivstartvmini.ui.channel.ChannelListScreen
import com.vs18.kyivstartvmini.ui.player.PlayerScreen
import com.vs18.kyivstartvmini.ui.profile.ProfileScreen
import com.vs18.kyivstartvmini.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            var currentScreen by remember { mutableStateOf<Screen>(Screen.ChannelList) }
            var selectedChannel by remember { mutableStateOf<Channel?>(null) }
            var showSplash by remember { mutableStateOf(true) }
            val prefs = remember { AuthPreferences(context) }
            val token by prefs.token.collectAsState(initial = null)
            val authorized = token != null
            val email by prefs.email.collectAsState(initial = null)


            if(showSplash) {
                SplashScreen {
                    showSplash = false
                }
            }

            else {
                if (authorized) {
                    when (currentScreen) {
                        is Screen.ChannelList -> ChannelListScreen(
                            email = email,
                            onChannelClick = {
                                selectedChannel = it
                                currentScreen = Screen.Player
                            },
                            onProfileClick = { currentScreen = Screen.Profile }
                        )

                        is Screen.Player -> selectedChannel?.let {
                            PlayerScreen(
                                channel = it,
                                onBack = {
                                    selectedChannel = null
                                    currentScreen = Screen.ChannelList
                                })
                        }

                        is Screen.Profile -> ProfileScreen(
                            onLogout = {
                                currentScreen = Screen.ChannelList
                            },
                            onBack = {
                                currentScreen = Screen.ChannelList
                            }
                        )
                    }
                } else {
                    AuthScreen(
                        onAuthorized = {}
                    )
                }
            }
        }
    }
}

sealed class Screen {
    object ChannelList : Screen()
    object Player : Screen()
    object Profile : Screen()
}