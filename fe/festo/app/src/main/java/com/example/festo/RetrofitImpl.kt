package com.example.festo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitImpl {
//    private const val URL = "주소" // 예시 : "https://0.0.0.1:8080"
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val service: RetrofitService = retrofit.create(RetrofitService::class.java)

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitService::class.java)


}