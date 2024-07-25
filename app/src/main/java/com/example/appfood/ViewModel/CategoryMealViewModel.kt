package com.example.appfood.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.Models.Meal
import com.example.appfood.Models.MealPopular
import com.example.appfood.Models.Popular
import com.example.appfood.Retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel : ViewModel() {

    private var categoryMealListLiveData = MutableLiveData<MutableList<MealPopular>>()

    fun getCategoryMealList(name: String) {
        ApiService.apiService.getCategoryMealList(name).enqueue(object : Callback<Popular> {
            override fun onResponse(call: Call<Popular>, response: Response<Popular>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        categoryMealListLiveData.postValue(body.meals.toMutableList())
                    }
                } else {
                    return
                }
            }

            override fun onFailure(p0: Call<Popular>, p1: Throwable) {

            }

        })
    }

    fun observerCategoryMealListLiveData(): MutableLiveData<MutableList<MealPopular>> {
        return categoryMealListLiveData
    }
}