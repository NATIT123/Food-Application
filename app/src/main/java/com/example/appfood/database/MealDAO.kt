package com.example.appfood.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appfood.Models.Meal

@Dao
interface MealDAO {
    @Insert
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)


    @Query("SELECT * FROM mealInformation")
    fun getListMeal(): LiveData<List<Meal>>
}