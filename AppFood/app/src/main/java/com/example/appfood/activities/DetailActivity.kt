package com.example.appfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.appfood.Fragments.HomeFragment
import com.example.appfood.Models.Meal
import com.example.appfood.R
import com.example.appfood.ViewModel.MealViewModel
import com.example.appfood.ViewModel.MealViewModelFactory
import com.example.appfood.database.MealDatabase
import com.example.appfood.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var id: String

    private lateinit var detailMvvm: MealViewModel

    private lateinit var mMeal: Meal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        detailMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]


//        detailMvvm = ViewModelProvider(this)[MealViewModel::class.java]

        loading()
        setData()
        detailMvvm.getMealDetail(id)
        observerMealDetail()

        binding.btnClickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mMeal.strYoutube))
            startActivity(intent)
        }

        binding.floatingButtonFavorite.setOnClickListener {
            onClickFavoriteMeal(mMeal)
        }

    }

    private fun onClickFavoriteMeal(meal: Meal) {
        meal?.let {
            detailMvvm.insertMeal(meal)
            Toast.makeText(this@DetailActivity, "Meal Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData() {
        val bundle = intent.extras
        if (bundle != null) {
            val name = bundle.getString(HomeFragment.MEAL_NAME)
            val thumbImage = bundle.getString(HomeFragment.MEAL_THUMB)
            val id = bundle.getString(HomeFragment.MEAL_ID)
            this.id = id!!
            binding.collapsingLayout.title = name
            Glide.with(this).load(thumbImage).into(binding.imgMealDetail)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerMealDetail() {
        detailMvvm.observeMealDetailLiveData().observe(
            this
        ) {
            complete()
            mMeal = it
            Log.d("MyApp", mMeal.strYoutube!!)
            binding.tvCategory.text = "Category: ${mMeal.strCategory}"
            binding.tvArea.text = "Area: ${mMeal.strArea}"
            binding.tvInstruction.text = mMeal.strInstructions
        }
    }

    private fun loading() {
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvInstruction.visibility = View.INVISIBLE
        binding.btnClickVideo.visibility = View.INVISIBLE
        binding.floatingButtonFavorite.visibility = View.INVISIBLE
        binding.progressLinear.visibility = View.VISIBLE
    }

    private fun complete() {
        binding.tvArea.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvInstruction.visibility = View.VISIBLE
        binding.btnClickVideo.visibility = View.VISIBLE
        binding.floatingButtonFavorite.visibility = View.VISIBLE
        binding.progressLinear.visibility = View.GONE
    }
}