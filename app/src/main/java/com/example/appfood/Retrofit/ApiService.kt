package com.example.appfood.Retrofit

import com.example.appfood.Models.Category
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.Models.Popular
import com.example.appfood.Models.MealList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val apiService: ApiService =
            Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                .create(ApiService::class.java)
    }


    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularMeal(@Query("c") category: String): Call<Popular>

    @GET("categories.php")
    fun getCategoryMeal(): Call<Category>

    @GET("filter.php")
    fun getCategoryMealList(@Query("c") category: String): Call<Popular>

    @GET("search.php")
    fun searchMeals(@Query("s") key: String): Call<MealList>


}