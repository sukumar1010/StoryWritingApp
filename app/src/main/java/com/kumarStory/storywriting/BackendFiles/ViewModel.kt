package com.kumarStory.storywriting.BackendFiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val apiService: ApiService) : ViewModel() {

    fun registerUser(username: String, password: String, onResult: (Response<RegisterResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.register(RegisterRequest(username, password))
            delay(1000)
            onResult(response)
        }
    }

    fun checkUserExist(username: String,onResult: (Response<CheckUserExistenceResponse>) -> Unit)   {
        viewModelScope.launch {
            val response = apiService.checkuserExistence(CheckUserExistence(username))
            delay(1000)
            onResult(response)

        }
    }

    fun login(username: String, password: String, onResult: (Response<LoginResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.login(LoginRequest(username, password))
            onResult(response)
        }
    }

    fun createStory(userId: Int, title: String, content: String, onResult: (Response<StoryPostingResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.postingStory(StoryPostingRequest(userId, title, content))
            onResult(response)
        }
    }

    fun getUserStories(userId: Int, onResult: (Response<UserStoriesResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.getStoriesOfUser(userId)
            onResult(response)
        }
    }

    fun getAllStories( onResult: (Response<UserStoriesResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.getAllStories()
            onResult(response)
        }
    }

    fun getUserById(userId:Int, onResult: (Response<GetUserResponse>) -> Unit) {
        viewModelScope.launch {
            val response = apiService.getUserById(userId)
            onResult(response)
        }
    }

    fun updatePassword(userId:Int,password:String,onResult:(Response<UpdatePasswordResponse>)->Unit){
        viewModelScope.launch {
            val response = apiService.updatePassword(userId,UpdatePasswordRequest(password))
            onResult(response)
        }
    }

    fun updateMaxStreak(userId:Int,streak:Int,onResult:(Response<UpdatePasswordResponse>)->Unit){
        viewModelScope.launch {
            val response = apiService.updateMaxStreak(userId,UpdateMaxStreak(streak))
            onResult(response)
        }
    }

    fun DeleteStory( userId: Int,storyId:Int,onResult:(Response<DeleteResponse>)->Unit){
        viewModelScope.launch {
            val response = apiService.deleteStory(userId,storyId)
            onResult(response)
        }
    }

    fun DeleteUser(userId: Int,onResult:(Response<DeleteResponse>)->Unit){
        viewModelScope.launch {
            val response = apiService.deleteUser(userId)
            onResult(response)
        }
    }
}
