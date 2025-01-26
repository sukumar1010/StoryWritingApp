package com.kumarStory.storywriting.FrontendFiles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Screens(val route:String) {
    object OnBoardingScreen : Screens(route = "onBoardingScreen")
    object CreateAccScreen : Screens(route = "createAccScreen")
    object LoginScreen : Screens(route = "loginScreen")
    object MainScreen : Screens(route = "mainScreen")
    object LoginOrBoardingScreen : Screens(route = "loginOrBoardingScreen")
    object ProfileScreen : Screens(route = "profileScreen")
    object UserStoriesScreen : Screens(route = "userStoriesScreen")



}

@Parcelize
data class ReadStory(
    val storyId: Int,
    val title: String,
    val content: String
) : Parcelable
