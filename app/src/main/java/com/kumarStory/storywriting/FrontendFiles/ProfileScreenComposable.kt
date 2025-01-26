package com.kumarStory.storywriting.FrontendFiles

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kumarStory.storywriting.BackendFiles.MainViewModel
import com.kumarStory.storywriting.R
import com.kumarStory.storywriting.SetStatusBarColor
import org.koin.androidx.compose.getViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreenComposable(
    todayStreak:SharedPreferences,
    streak:SharedPreferences,
    navController: NavController,
    viewModel: MainViewModel = getViewModel(),
    saveUsername: SharedPreferences
) {
    val todayStreakValue = todayStreak.getInt("todayStreak",0)
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var maxStreak by remember { mutableStateOf(0) }
    var isPasswordUpdate by remember { mutableStateOf(false) }
    var passwordUpdate by remember { mutableStateOf("") }
    val userIdd = saveUsername.getString("saveUsername", "")
    val keyboardController = LocalSoftwareKeyboardController.current
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var passwordUpdateMessage by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit){
        viewModel.getUserById(userIdd!!.toInt()) { response ->
            if (response.isSuccessful) {
                userName = response.body()?.user?.username!!
                password = response.body()?.user?.password!!
                maxStreak = response.body()?.user?.maxStreak!!

                val tmp = maxStreak
                if(todayStreakValue > tmp){
                    viewModel.updateMaxStreak(userIdd.toInt(),todayStreakValue){res->
                        if(res.body()?.statusCode==200){
                            maxStreak= maxStreak
                        }

                    }

                }
            }
        }
    }


    SetStatusBarColor(color = Color.LightGray.copy(alpha = 0.7f), darkIcons = true)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(color = Color.LightGray.copy(alpha = 0.7f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userName,
                color = Color.Black,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(top = 17.dp)
                    .wrapContentSize(),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
                .background(color = Color.LightGray.copy(alpha = 0.7f)),
        ) {
            Spacer(modifier = Modifier.width(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 30.dp),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 17.dp)
                        .wrapContentSize(),
                )
                Spacer(modifier = Modifier.width(20.dp))

                Spacer(modifier = Modifier.width(150.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            isPasswordUpdate = !isPasswordUpdate

                        }
                        .padding(top = 20.dp)
                )
            }
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                if (isPasswordUpdate) {


                    OutlinedTextField(
                        value = passwordUpdate,
                        onValueChange = { passwordUpdate = it },
                        label = { Text("Update Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (passwordUpdate.isNotEmpty() && passwordUpdate.isNotBlank()) {
                                    viewModel.updatePassword(userIdd!!.toInt(), passwordUpdate) { response ->
                                        if (response.body()?.statusCode == 200) {
                                            passwordUpdateMessage = response.body()?.message!!

                                        } else if (response.body()?.statusCode != 200) {
                                            passwordUpdateMessage = response.body()?.message!!
                                        }



                                    }

                                    passwordUpdate=""
                                    keyboardController?.hide()

                                }
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()

                    )


                }

            }
            if (passwordUpdateMessage.isNotEmpty() && passwordUpdateMessage.isNotBlank()) {
                Toast.makeText(context, passwordUpdateMessage, Toast.LENGTH_LONG).show()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.LightGray.copy(alpha = 0.7f))
            ) {
                // Top Content (Centered Items)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Use weight to allow the bottom section to appear properly
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation2))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 8.dp, top = 10.dp)
                    )

                    Text(
                        text = "$maxStreak",
                        color = Color.Black,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 17.dp)
                            .wrapContentSize()
                    )
                }

                // Bottom Content (Logout and Delete Buttons)
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(bottom = 5.dp), // Add padding to avoid touching the screen's edge
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
                ) {
                    Button(onClick = {
                        val editt = saveUsername.edit()
                        editt.putString("saveUsername", "")
                        editt.apply()
                        navController.navigate(Screens.LoginScreen.route)
//                        navController.popBackStack()



                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.Red
                        )
                    ) {
                        Text(
                            text = "LogOut",
                            color = Color.Red
                        )
                    }

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray, // Background color of the button
                            contentColor = Color.Red  // Text (or content) color
                        )
                    ) {
                        Text(text = "Delete Account", color = Color.Red)
                    }

                    DeleteAccountDialog(
                        showDialog = showDialog,
                        onConfirm = {
                            showDialog = false
                            viewModel.DeleteUser(userIdd!!.toInt()) { res ->
                                if (res.body()?.statusCode == 200) {
                                    val editt = saveUsername.edit()
                                    editt.putString("saveUsername", "")
                                    editt.apply()
                                    Toast.makeText(
                                        context,
                                        "Account deleted successfully.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // Navigate to login or home screen after deletion
                                    navController.navigate(Screens.CreateAccScreen.route)
//                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to delete account.${res.body()?.statusCode}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        onDismiss = { showDialog = false }
                    )

                }
            }



        }



    }
}


@Composable
fun DeleteAccountDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Account",color = Color.Red)
            },
            text = {
                Text(text = "Are you sure you want to delete your account? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "OK", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}