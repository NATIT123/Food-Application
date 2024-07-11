package com.example.appfood.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfood.Models.Category
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.Models.Popular
import com.example.appfood.Models.Meal
import com.example.appfood.Models.MealPopular
import com.example.appfood.Models.MealList
import com.example.appfood.Retrofit.ApiService
import com.example.appfood.database.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()

    private var popularMealLiveData = MutableLiveData<MutableList<MealPopular>>()

    private var categoryMealLiveData = MutableLiveData<MutableList<CategoryMeal>>()

    private var favoriteMealLiveData = mealDatabase.mealDao().getListMeal()

    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    private var searchMealLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandom: Meal? = null

    fun getApiRandom() {

        saveStateRandom?.let {
            randomMealLiveData.postValue(saveStateRandom)
            return
        }

        ApiService.apiService.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val meal: Meal = body.meals[0]
                        randomMealLiveData.value = meal
                        saveStateRandom = meal
                    }
                } else {
                    return
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
            }

        })
    }

    fun getMealById(id: String) {
        ApiService.apiService.getMealDetail(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val meal: Meal = body.meals.first()
                        bottomSheetMealLiveData.postValue(meal)
                    }
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {

            }

        })
    }

    fun searchMeal(key: String) {
        ApiService.apiService.searchMeals(key).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val meal: List<Meal> = body.meals
                        searchMealLiveData.postValue(meal)
                    }
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {

            }

        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }

    fun getPopularFood(category: String) {
        ApiService.apiService.getPopularMeal(category).enqueue(object : Callback<Popular> {
            override fun onResponse(call: Call<Popular>, response: Response<Popular>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        popularMealLiveData.value = body.meals.toMutableList()
                    }
                } else {
                    return
                }
            }

            override fun onFailure(p0: Call<Popular>, p1: Throwable) {
            }

        })
    }

    fun getCategoryFood() {
        ApiService.apiService.getCategoryMeal().enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        categoryMealLiveData.value = body.categories.toMutableList()
                    }
                } else {
                    return
                }
            }

            override fun onFailure(p0: Call<Category>, p1: Throwable) {
            }

        })
    }

    fun observeRandomMealLiveData(): MutableLiveData<Meal> {
        return randomMealLiveData
    }

    fun observerPopularMealLiveData(): MutableLiveData<MutableList<MealPopular>> {
        return popularMealLiveData
    }

    fun observerCategoryMealLiveData(): MutableLiveData<MutableList<CategoryMeal>> {
        return categoryMealLiveData
    }

    fun observerFavoriteMealLiveData(): LiveData<List<Meal>> {
        return favoriteMealLiveData
    }

    fun observerBottomSheetMealLiveData(): MutableLiveData<Meal> {
        return bottomSheetMealLiveData
    }

    fun observerSearchMealLiveData(): MutableLiveData<List<Meal>> {
        return searchMealLiveData
    }

}