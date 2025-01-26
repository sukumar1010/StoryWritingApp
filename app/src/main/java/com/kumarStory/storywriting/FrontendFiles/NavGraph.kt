package com.kumarStory.storywriting.FrontendFiles

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson

@Composable
fun SetupNavGraph(
    saveUserName:SharedPreferences,
    navController: NavHostController,
    todayStreak:SharedPreferences,
    streak:SharedPreferences

){
    NavHost(
        navController =navController ,
        startDestination =  Screens.LoginOrBoardingScreen.route
    ){
        composable(route = Screens.LoginOrBoardingScreen.route){
            LoginOrBoarding(saveUsername = saveUserName, navController = navController)
        }

            composable(route = Screens.OnBoardingScreen.route){
                OnBoardingCompose {
                    navController.popBackStack()
                    navController.navigate(Screens.CreateAccScreen.route)

                }
            }

        composable(route = Screens.LoginScreen.route){
            LoginScreen(
                navController=navController,
                saveUsername = saveUserName,
                onCreateClick = {
                    navController.popBackStack()
                    navController.navigate(Screens.CreateAccScreen.route)
                }
            )
        }

        composable(route=Screens.CreateAccScreen.route){
            CreateAccountComposable(
                saveUsername = saveUserName,
                navController=navController,
                onLoginClick = {
                    navController.popBackStack()
                    navController.navigate(Screens.LoginScreen.route)
                }
            )
        }



        composable(route=Screens.MainScreen.route){
            MainScreenComposable(
                todayStreak=todayStreak,
                streak=streak,
                navController=navController,
                saveUsername = saveUserName
            )
        }

        composable(route=Screens.ProfileScreen.route){
            ProfileScreenComposable(
                todayStreak=todayStreak,
                streak=streak,
                navController=navController,
                saveUsername = saveUserName
            )
        }


        composable(
            route = "storyDetail/{story}",
            arguments = listOf(navArgument("story") { type = NavType.StringType })
        ) { backStackEntry ->
            val storyJson = backStackEntry.arguments?.getString("story")
            val story = Gson().fromJson(storyJson, ReadStory::class.java)
            StoryDetailScreen(story = story)
        }

//        composable(
//            route = "userStoryDetail/{story}",
//            arguments = listOf(navArgument("story") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val storyJson = backStackEntry.arguments?.getString("story")
//            val story = Gson().fromJson(storyJson, ReadStory::class.java)
//            UserStoryDetailScreen(story = story)
//        }

        composable(route=Screens.UserStoriesScreen.route){
            UserStoriesComposable(
                streak=streak,
                todayStreak=todayStreak,
                saveUsername=saveUserName,
                navController=navController
            )
        }
    }

}