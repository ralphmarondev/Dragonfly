package com.ralphmarondev.dragonfly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import com.ralphmarondev.dragonfly.core.common.NotificationHelper
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sendGreetings()
        setContent {
            Scaffold { innerPadding ->
                WebViewScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    url = "https://ralphmarondev.github.io/"
                )
            }
        }
    }

    private fun sendGreetings() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Good morning!"
            in 12..17 -> "Good afternoon!"
            in 18..21 -> "Good evening!"
            else -> "Still up?"
        }
        val shortMessage = "I'm here if you need anything!"
        val expandedMessage = when (hour) {
            in 5..11 -> "Let’s start the day gently. You can check your apps or just take things slow."
            in 12..17 -> "Hope your day’s going well so far. Don’t forget to take a break too."
            in 18..21 -> "You made it through the day. Want to check in on things?"
            else -> "It’s quiet right now… perfect time to rest or reflect."
        }
        NotificationHelper.sendNotification(
            context = this,
            id = 2,
            title = greeting,
            content = shortMessage,
            style = NotificationCompat.BigTextStyle().bigText(expandedMessage)
        )
    }
}