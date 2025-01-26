package com.kumarStory.storywriting.BackendFiles

data class RegisterRequest(val username:String, val password:String)
data class RegisterResponse(val statusCode : Int,val userId: Int)

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val statusCode : Int,val userId: Int)

data class StoryRequest(val user: Int, val title: String, val content: String)
data class StoryResponse(val storyId: Int, val title: String, val content: String)

data class StoryPostingRequest(val user: Int, val title: String, val content: String)
data class StoryPostingResponse(val statusCode: Int)

data class UserStoriesResponse(val stories: List<StoryResponse>)

data class CheckUserExistence(val username:String)
data class CheckUserExistenceResponse(val statusCode:Int)

data class GetUserResponse(
    val statusCode: Int,
    val user: User?
)

data class User(
    val username: String?,
    val password: String?,
    val maxStreak: Int?
)

data class UpdatePasswordRequest(
    val password: String
)

data class UpdateMaxStreak(
    val maxStreak:Int
)
data class UpdatePasswordResponse(
    val statusCode: Int,
    val message: String
)

data class DeleteResponse(val statusCode: Int)