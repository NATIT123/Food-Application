package com.example.appfood.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfood.Models.Meal
import com.example.appfood.Models.MealList
import com.example.appfood.Retrofit.ApiService
import com.example.appfood.database.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
    private var mealLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id: String) {
        ApiService.apiService.getMealDetail(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        mealLiveData.value = body.meals[0]

                    }
                } else {
                    Log.d("MyApp", "ERROR")
                    return
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
            }

        })
    }

    fun observeMealDetailLiveData(): MutableLiveData<Meal> {
        return mealLiveData
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }


}