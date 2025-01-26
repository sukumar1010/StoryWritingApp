package com.kumarStory.storywriting

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.kumarStory.storywriting.FrontendFiles.SetupNavGraph
import com.kumarStory.storywriting.ui.theme.StoryWritingTheme
import androidx.work.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val saveUserName = getSharedPreferences("saveUsername", MODE_PRIVATE)
        val todayStreak = getSharedPreferences("todayStreak", MODE_PRIVATE)
        val streak = getSharedPreferences("streak", MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContent {

            StoryWritingTheme {
                // A surface container using the 'background' color from the theme

                MaterialTheme(
                    colorScheme = lightColorScheme(), // Explicitly use light colors
//                    typography = Typography.,
//                    shapes = Shapes,
                    content = {
                        // Your app content
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val navController = rememberNavController()
                            SetupNavGraph(
                                navController=navController,
                                saveUserName = saveUserName,
                                todayStreak=todayStreak,
                                streak=streak
                            )
                        }
                    }
                )
            }
        }
    }
}



fun calculateTimeUntilMidnight(): Long {
    val now = Calendar.getInstance()
    val midnight = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_MONTH, 1) // Next day
    }
    return midnight.timeInMillis - now.timeInMillis
}

@SuppressLint("ScheduleExactAlarm")
fun scheduleMidnightBroadcastReceiver(context: Context) {
    val intent = Intent(context, ResetBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Calculate delay until midnight
    val delayUntilMidnight = calculateTimeUntilMidnight()

    // Set an exact alarm for midnight
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + delayUntilMidnight,
        pendingIntent
    )
}
//class ResetBroadcastReceiver : BroadcastReceiver() {
//    @SuppressLint("UnsafeProtectedBroadcastReceiver")
//    override fun onReceive(context: Context, intent: Intent?) {
//        // Perform the task here
//        val sharedPreferences = context.getSharedPreferences("testPrefs", Context.MODE_PRIVATE)
//        val currentValue = sharedPreferences.getInt("testValue", 0)
//        sharedPreferences.edit().putInt("testValue", currentValue + 1).apply()
//
//        Log.d("ResetBroadcastReceiver", "Broadcast received. Updated Value: ${currentValue + 1}")
//
//        // Schedule for the next midnight
//        scheduleMidnightBroadcastReceiver(context)
//    }
//}

//
class ResetBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {
        // Perform the task here
        val sharedPreferencesStreak: SharedPreferences = context.getSharedPreferences("streak", Context.MODE_PRIVATE)
        val sharedPreferencesTodayStreak: SharedPreferences = context.getSharedPreferences("todayStreak", Context.MODE_PRIVATE)

        val valueOfStreak = sharedPreferencesStreak.getBoolean("streak", false)

        if (!valueOfStreak) {
            // Reset today's streak only if streak is false
            sharedPreferencesTodayStreak.edit().putInt("todayStreak", 0).apply()
        }
        Toast.makeText(context,"newday",Toast.LENGTH_LONG).show()
        // Always set `streak` to false to prepare for the next day
         sharedPreferencesStreak.edit().putBoolean("streak", false).apply()
        val updated =sharedPreferencesStreak.getBoolean("streak", false)

        Log.d("ResetBroadcastReceiver", "Broadcast received. Updated Value: ${valueOfStreak },${updated }")
    }
}
//
//@SuppressLint("ServiceCast")
//fun scheduleBroadcastReceiver(context: MyApp) {
//    val intent = Intent(context, ResetBroadcastReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    alarmManager.setRepeating(
//        AlarmManager.RTC_WAKEUP,
//        System.currentTimeMillis() + 30000, // Initial delay (30 seconds)
//        30000, // Repeat interval (30 seconds)
//        pendingIntent
//    )
//}




@Composable
fun SetStatusBarColor(color: Color, darkIcons: Boolean = false) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = color,
        darkIcons = darkIcons // Set true for dark icons, false for light icons
    )
}