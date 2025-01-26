package com.kumarStory.storywriting.FrontendFiles


import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kumarStory.storywriting.BackendFiles.MainViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.*
import com.google.gson.Gson
import com.kumarStory.storywriting.BackendFiles.StoryResponse
import com.kumarStory.storywriting.BackendFiles.UserStoriesResponse
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.kumarStory.storywriting.SetStatusBarColor


@Composable
fun UserStoriesComposable(
    streak:SharedPreferences,
    todayStreak:SharedPreferences,
    saveUsername: SharedPreferences,
    navController: NavController,
    viewModel: MainViewModel = getViewModel(),
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    SetStatusBarColor(color = Color.LightGray.copy(alpha = 0.7f), darkIcons = true)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Adjusts for bottom bar padding
        ) {
            // Tab Indicator
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .height(48.dp)
                    ,

                contentColor = Color.White      ,
                backgroundColor = Color.LightGray.copy(alpha = 0.7f),


                ) {
                listOf("Your Stories", "Write Story").forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(text = title, fontSize = 20.sp,color=Color.Black)
                        }
                    )
                }
            }

            // Pager for swipeable screens
            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> UserStoriesScreen(
                        navController = navController,
                        saveUsername = saveUsername,
                        viewModel = viewModel
                    ) // Screen 1: Display User Stories
                    1 -> WriteStoryScreen(
                        todayStreak=todayStreak,
                        streak=streak,
                        viewModel = viewModel,
                        saveUsername = saveUsername
                    ) // Screen 2: Write a New Story
                }
            }
        }
    }
}
@Composable
fun UserStoriesScreen(
    navController: NavController,
    saveUsername: SharedPreferences,
    viewModel: MainViewModel
) {
    var stories by remember { mutableStateOf<UserStoriesResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) } // Track loading status
    val context = LocalContext.current
    val userIdd = saveUsername.getString("saveUsername", "")!!.toInt()

    LaunchedEffect(key1 = stories?.stories?.size) {
        viewModel.getUserStories(userIdd) { response ->
            if (response.isSuccessful) {
                stories = response.body()
            } else {
                Toast.makeText(context, "Failed to fetch stories", Toast.LENGTH_SHORT).show()
            }
            isLoading = false // Hide loading bar after fetching stories
        }
    }

    if (isLoading) {
        // Show a loading indicator while fetching stories
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.7f))
        ) {
            stories?.stories?.let { storyList -> // Check if stories is not null
                items(storyList) { story ->
                    UserStoryCard(
                        story = story,
                        onClick = {
                            navController.navigate("storyDetail/${Uri.encode(Gson().toJson(story))}")
                        },
                        onTapDelete = {
                            viewModel.DeleteStory(userIdd, story.storyId) { res ->
                                if (res.body()?.statusCode == 200) {
                                    stories = stories?.copy(
                                        stories = stories!!.stories.filter { it.storyId != story.storyId }
                                    )
                                    Toast.makeText(context, "Story deleted", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserStoryCard(story: StoryResponse, onClick: () -> Unit,onTapDelete:()->Unit) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onTapDelete()
                    },
                    onLongPress = {
                        clipboardManager.setText(AnnotatedString(story.content))
                        Toast
                            .makeText(context, "Story copied to clipboard!", Toast.LENGTH_SHORT)
                            .show()

                    },
                    onTap = {
                        onClick()
                    }
                )

            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
//            .clickable { onClick() },

        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title in bold
            androidx.compose.material3.Text(
                text = story.title,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Truncated content
            androidx.compose.material3.Text(
                text = story.content,
                maxLines = 3, // Limit to 3 lines
                overflow = TextOverflow.Ellipsis, // Add "..." if the text is truncated
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )
        }
    }
}

//@Composable
//fun UserStoryDetailScreen(story: ReadStory) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Title
//        androidx.compose.material3.Text(
//            text = story.title,
//            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Full content
//        androidx.compose.material3.Text(
//            text = story.content,
//            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
//        )
//    }
//}

@Composable
fun WriteStoryScreen(
    todayStreak:SharedPreferences,
    streak:SharedPreferences,
    viewModel: MainViewModel,
    saveUsername: SharedPreferences
) {
    val checkTodayStreak = todayStreak.getInt("todayStreak",0)
    val checkStreak = streak.getBoolean("streak",false)

    val userId = saveUsername.getString("saveUsername", "")!!.toInt()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    // This keeps track of whether the content field is focused
    var isContentFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // Adjust for the keyboard
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Title Input Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif) },

                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,


                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Story Content Input Field
            BasicTextField(
                value = content,
                onValueChange = { newValue ->
                    content = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isContentFocused = focusState.isFocused
                    },
                textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = "Write your story here...",
                            style = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }

        // Scroll to the bottom when content field gains focus
        if (isContentFocused) {
            LaunchedEffect(isContentFocused) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }

        // Submit Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp)
                .size(50.dp)

                .background(color = Color.Green, shape = RoundedCornerShape(50.dp))
                .clickable {
                    keyboardController?.hide() // Hide keyboard on submit
                    if (content.isNotBlank()) {
                        viewModel.createStory(userId, title, content) { response ->
                            if (response.body()?.statusCode == 201) {
                                Toast
                                    .makeText(
                                        context,
                                        "Story added successfully!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                title = ""
                                content = ""
                            } else {
                                Toast
                                    .makeText(context, "Failed to add story", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast
                            .makeText(context, "Story cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (checkStreak == false) {
                        val ed = streak.edit()
                        ed.putBoolean("streak", true)
                        ed.apply()
                        val edt = todayStreak.edit()
                        edt.putInt("todayStreak", checkTodayStreak + 1)
                        edt.apply()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
        }
    }
}
