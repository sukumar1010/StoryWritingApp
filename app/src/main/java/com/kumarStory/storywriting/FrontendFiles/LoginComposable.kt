package com.kumarStory.storywriting.FrontendFiles

import android.content.SharedPreferences
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkSaveStateControl
import androidx.navigation.navArgument
import org.koin.androidx.compose.getViewModel
import com.kumarStory.storywriting.BackendFiles.MainViewModel
import com.kumarStory.storywriting.R
import com.kumarStory.storywriting.SetStatusBarColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    saveUsername:SharedPreferences,
    viewModel: MainViewModel = getViewModel(),
    onCreateClick:()->Unit
) {

    val context = LocalContext.current
    val priceFocusRequester = remember { FocusRequester() }
    var errorMessage by remember{
        mutableStateOf("")
    }

    SetStatusBarColor(color = Color.White, darkIcons = true)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Half: Image
        Image(
            painter = painterResource(id = R.drawable.sukumarloginscreen), // Replace with your image resource
            contentDescription = "Create Account Illustration",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ensures it occupies half the screen
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Half: Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Remaining space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Username Input
            var username by remember { mutableStateOf("") }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)), // Rounded corners
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        priceFocusRequester.requestFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(

                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF4CAF50)
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(priceFocusRequester)
                    .clip(RoundedCornerShape(16.dp)), // Rounded corners
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            if(errorMessage.isNotEmpty() ){
                    Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {

                   if(username.isNotEmpty() and password.isNotEmpty()){
                       viewModel.login(username,password){response->
                           if(response.body()?.statusCode==200){
                               val editt = saveUsername.edit()
                               editt.putString("saveUsername",response.body()?.userId.toString())
                               editt.apply()
                               navController.navigate(Screens.MainScreen.route)

                           }
                           else if(response.body()?.statusCode!=200){
                                errorMessage = "Invalid username or password"
                           }
                       }
                   }


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Light Green
                    contentColor = Color.White // Text color inside the button
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Log In",fontWeight = FontWeight.Bold,fontSize = 20.sp)
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Go to Login
            Text(
                text = "Don't have an Account? create",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onCreateClick() }
            )
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountComposable(
    saveUsername:SharedPreferences,
    navController: NavController,
    viewModel: MainViewModel = getViewModel(),
    onLoginClick: () -> Unit,

) {
    val context = LocalContext.current
    var errorMessage by remember{ mutableStateOf("") }
    var proceed by remember {
        mutableStateOf(false)
    }

    val priceFocusRequester = remember { FocusRequester() }

    SetStatusBarColor(color = Color.White, darkIcons = true)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Half: Image
        Image(
            painter = painterResource(id = R.drawable.sukumarregister), // Replace with your image resource
            contentDescription = "Create Account Illustration",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ensures it occupies half the screen
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Half: Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Remaining space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Username Input
            var username by remember { mutableStateOf("") }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused && username.isNotEmpty()) {
                            viewModel.checkUserExist(username) { response ->
                                if (response.body()?.statusCode != 200) {
                                    errorMessage = "This username is already useb by someone else"
                                } else if (response.body()?.statusCode == 200) {
                                    proceed = !proceed
                                }
                            }
                        } else if (focusState.isFocused) {
                            errorMessage = ""
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        priceFocusRequester.requestFocus()
                    }
                ),
                shape = RoundedCornerShape(30.dp)
            )
            if(errorMessage.isNotEmpty() and errorMessage.isNotBlank()){
                Text(text = errorMessage,color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(

                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF4CAF50)
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(priceFocusRequester)
                    .clip(RoundedCornerShape(16.dp)), // Rounded corners
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Button(
                onClick = {
                    if (proceed and username.isNotEmpty() and password.isNotEmpty()){


                        viewModel.registerUser(
                            username = username,
                            password = password,

                            ) { response ->

                            if (response.body()?.statusCode==201) {
                                val editt = saveUsername.edit()
                                editt.putString("saveUsername",response.body()?.userId.toString())
                                editt.apply()


                                navController.navigate(Screens.MainScreen.route)

                            }
                        }
                }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Light Green
                    contentColor = Color.White // Text color inside the button
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up",fontWeight = FontWeight.Bold,fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Go to Login
            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}



