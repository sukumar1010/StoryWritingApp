package com.kumarStory.storywriting.FrontendFiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DrawerDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.gson.Gson
import com.kumarStory.storywriting.BackendFiles.MainViewModel
import com.kumarStory.storywriting.BackendFiles.StoryResponse
import com.kumarStory.storywriting.BackendFiles.UserStoriesResponse
import com.kumarStory.storywriting.R
import com.kumarStory.storywriting.SetStatusBarColor
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
@Composable
fun LoginOrBoarding(
    saveUsername: SharedPreferences,
    navController: NavController
) {
    val context = LocalContext.current
    var isInternetAvailable by remember { mutableStateOf(true) }

    // Check internet connection
    LaunchedEffect(key1 = true) {
        isInternetAvailable = isInternetConnected(context)
    }

    // Display screen based on internet availability
    if (isInternetAvailable) {
        LaunchedEffect(key1 = true) {
            delay(1000L)
            if (saveUsername.getString("saveUsername", "")?.isNotEmpty() == true) {
                navController.popBackStack()
                navController.navigate(Screens.MainScreen.route)
            } else {
                navController.popBackStack()
                navController.navigate(Screens.OnBoardingScreen.route)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        if (isInternetAvailable) {
            // Main logo while navigating
            Image(
                painter = painterResource(id = R.drawable.mainlogo),
                contentScale = ContentScale.Fit,
                contentDescription = "main logo",
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(60.dp))
            )
        } else {
            // Show Lottie animation for internet lost
            LottieAnimations(
                resId = R.raw.networklost, // Replace with your Lottie JSON resource
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(60.dp))
            )
        }
    }
}

@Composable
fun LottieAnimations(resId: Int, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier

    )
}

fun isInternetConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

//@Composable
//fun LoginOrBoarding(
//    saveUsername:SharedPreferences,
//    navController: NavController
//){
//    SetStatusBarColor(color = Color.White, darkIcons = true)
//    LaunchedEffect(key1 = true) {
//
//        delay(1000L)
//        if (saveUsername.getString("saveUsername", "")?.isNotEmpty() == true) {
//            navController.popBackStack()
//            navController.navigate(Screens.MainScreen.route)
//
//        }
//        else{
//            navController.popBackStack()
//            navController.navigate(Screens.OnBoardingScreen.route)
//        }
//    }
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Transparent)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.mainlogo),
//            contentScale = ContentScale.Fit, // Ensures the image covers the entire screen
//            contentDescription = "main logo",
//            modifier = Modifier
//                .size(300.dp) // Set desired size (increase as needed)
//                .clip(RoundedCornerShape(60.dp))
//        )
//    }
//
//}



@Composable
fun MainScreenComposable(
    todayStreak:SharedPreferences,
    streak : SharedPreferences,
    navController: NavController,
    saveUsername: SharedPreferences
) {
    SetStatusBarColor(color = Color.LightGray.copy(alpha = 0.7f),darkIcons = true)
    val todayStreakValue = todayStreak.getInt("todayStreak",0)
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") } // State to hold the search query

    Scaffold(
        topBar = {
            TopAppBar(

                todayStreakValue=todayStreakValue,
                isSearchVisible = isSearchVisible,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchIconClick = { isSearchVisible = !isSearchVisible }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        StoryList(
            streak=streak,
            navController = navController,
            saveUsername = saveUsername,
            searchQuery = searchQuery, // Pass the search query to StoryList
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun TopAppBar(

    todayStreakValue:Int,
    isSearchVisible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchIconClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.7f))
            .padding(start = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title
        Image(
            painter = painterResource(id = R.drawable.mainlogo),
            contentDescription = "main logo",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 8.dp)
        )

        if (isSearchVisible) {
            // Display the search bar when the state is true
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        } else {
            // Placeholder to keep layout alignment consistent
            Spacer(modifier = Modifier.weight(1f))
        }

        // Streak Icon and Search Icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Search Icon
            IconButton(onClick = onSearchIconClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }

            // Streak Icon (Flame)
            Text(text = "$todayStreakValue")
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = "Streak Icon",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
@Composable
fun StoryList(
    streak: SharedPreferences,
    navController: NavController,
    saveUsername: SharedPreferences,
    searchQuery: String,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = getViewModel(),
) {
    val userIdd = saveUsername.getString("saveUsername", "")!!.toInt()
    var stories by remember { mutableStateOf<UserStoriesResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) } // Track loading state
    val context = LocalContext.current
    val st = streak.getBoolean("streak", false)

    // Fetch stories when the composable first renders
    LaunchedEffect(Unit) {
        viewModel.getAllStories { res ->
            if (res.isSuccessful) {
                stories = res.body()
                Toast.makeText(context, "stories ${stories?.stories?.size}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to fetch stories", Toast.LENGTH_SHORT).show()
            }
            isLoading = false // Hide loading bar after fetching stories
        }
    }

    if (isLoading) {
        // Show a circular progress indicator while loading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center // Center the loading indicator
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        val filteredStories = stories?.stories?.filter { story ->
            story.title.contains(searchQuery, ignoreCase = true)
        } ?: emptyList()

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.7f))
        ) {
            itemsIndexed(filteredStories) { index, story ->
                val offsetX = remember { Animatable(initialValue = 1000f) }

                // Start the animation for sliding in
                LaunchedEffect(Unit) {
                    offsetX.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 100,
                            delayMillis = index * 100 // Sequential animation
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(offsetX.value.toInt(), 0) } // Apply horizontal offset animation
                ) {
                    StoryCard(
                        story = story,
                        onClick = {
                            navController.navigate("storyDetail/${Uri.encode(Gson().toJson(story))}")
                        }
                    )
                }
            }
        }
    }
}



@SuppressLint("InvalidColorHexValue")
@Composable
fun StoryCard(story: StoryResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),// Color(0xFFBBB09B).copy(alpha = 0.6f)),

    ) {
        Column(
            modifier = Modifier.padding(16.dp),

        ) {
            // Title in bold
            Text(
                text = story.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Truncated content
            Text(
                text = story.content,
                maxLines = 3, // Limit to 3 lines
                overflow = TextOverflow.Ellipsis, // Add "..." if the text is truncated
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun StoryDetailScreen(story: ReadStory) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title - Centered horizontally
        Text(
            text = story.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally) // Center title explicitly
        )

        // Full content with indentation for the first line
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = ParagraphStyle(
                        textIndent = TextIndent(firstLine = 16.sp) // Indent only the first line
                    )
                ) {
                    append(story.content)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp) // Space between title and content
        )
    }
}



@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.White,
        modifier = Modifier.border(1.dp,Color.Black)
    ) {

        // Home Tab
        BottomNavigationItem(
            selected = true,
            onClick = {
                navController.popBackStack()
                navController.navigate(Screens.MainScreen.route)
                      },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
            label = { Text("Home") },
            selectedContentColor = Color(0xFF74C69D),
            unselectedContentColor = Color.Gray
        )

        // Stories Tab
        BottomNavigationItem(
            selected = false,
            onClick = { navController.popBackStack()
                        navController.navigate(Screens.UserStoriesScreen.route)
                      },
            icon = {
                Icon(Icons.Default.Book,
                    contentDescription = "Stories Icon")
                   },
            label = { Text("Stories") },
            selectedContentColor = Color(0xFF74C69D),
            unselectedContentColor = Color.Gray
        )



        // Profile Tab
        BottomNavigationItem(
            selected = false,
            onClick = { navController.popBackStack()
                        navController.navigate(Screens.ProfileScreen.route)
                      },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile Icon") },
            label = { Text("Profile") },
            selectedContentColor = Color(0xFF74C69D),
            unselectedContentColor = Color.Gray
        )

    }
}