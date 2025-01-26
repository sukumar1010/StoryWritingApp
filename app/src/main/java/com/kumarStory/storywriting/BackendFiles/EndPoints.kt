package com.kumarStory.storywriting.BackendFiles

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.PUT


interface ApiService {

    @POST("createAcc/")
    suspend fun register(@Body request:RegisterRequest):Response<RegisterResponse>

    @POST("login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("checkuserExistence/")
    suspend fun checkuserExistence(@Body request:CheckUserExistence):Response<CheckUserExistenceResponse>

    @POST("uploadStory/")
    suspend fun postingStory(
        @Body request:StoryPostingRequest
    ):Response<StoryPostingResponse>



    @GET("stories/{userId}/")
    suspend fun getStoriesOfUser(@Path("userId") userId: Int): Response<UserStoriesResponse>

    @DELETE("delete-story/{user_id}/{story_id}/")
    suspend fun deleteStory(
        @Path("user_id") userId: Int,
        @Path("story_id") storyId: Int,

    ):Response<DeleteResponse>

    @DELETE("delete-user/{user_id}/")
    suspend fun deleteUser(
        @Path("user_id") userId: Int
    ):Response<DeleteResponse>


    @GET("getAllStories/")
    suspend fun getAllStories(): Response<UserStoriesResponse>



    @GET("user/{user_id}/")
    suspend fun getUserById(@Path("user_id") userId: Int): Response<GetUserResponse>

    @PUT("update-password/{user_id}/")
    suspend fun updatePassword(
        @Path("user_id") userId: Int,
        @Body request: UpdatePasswordRequest
    ): Response<UpdatePasswordResponse>

    @PUT("update-maxStreak/{user_id}/")
    suspend fun updateMaxStreak(
        @Path("user_id") userId: Int,
        @Body request:UpdateMaxStreak
    ):Response<UpdatePasswordResponse>
}
