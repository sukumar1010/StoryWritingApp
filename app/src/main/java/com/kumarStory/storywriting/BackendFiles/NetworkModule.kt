package com.kumarStory.storywriting.BackendFiles

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val NetworkModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
//            .baseUrl("http://192.168.177.52:8000/")
            .baseUrl("https://storywritingserver.pythonanywhere.com/")
//            .baseUrl("http://192.168.142.52:8000/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}


val ViewModelModule = module {
    viewModel { MainViewModel(get()) }
}
