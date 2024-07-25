package com.example.appfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appfood.Fragments.HomeFragment
import com.example.appfood.Models.MealPopular
import com.example.appfood.R
import com.example.appfood.ViewModel.CategoryMealViewModel
import com.example.appfood.ViewModel.MealViewModel
import com.example.appfood.adapters.CategoryListFoodAdapter
import com.example.appfood.databinding.ActivityCategoryMealBinding
import com.example.appfood.databinding.ActivityMainBinding

class CategoryMealActivity : AppCompatActivity(), CategoryListFoodAdapter.onClickCategoryItem {

    private lateinit var mCategoryListFoodAdapter: CategoryListFoodAdapter

    private lateinit var binding: ActivityCategoryMealBinding

    private var listCategoryFood = mutableListOf<MealPopular>()

    private lateinit var categoryListMvvm: CategoryMealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryListMvvm = ViewModelProvider(this)[CategoryMealViewModel::class.java]

        initRecyclerView()

        val categoryName = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)
        if (categoryName != null) {
            categoryListMvvm.getCategoryMealList(categoryName)
        }
        observerListCategory()


    }

    @SuppressLint("SetTextI18n")
    private fun observerListCategory() {
        categoryListMvvm.observerCategoryMealListLiveData().observe(this) {
            this.listCategoryFood = it
            mCategoryListFoodAdapter.setData(this.listCategoryFood)
            binding.tvTotal.text = "Total: ${this.listCategoryFood.size}"
        }
    }


    private fun initRecyclerView() {

        binding.rcvCategory.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        mCategoryListFoodAdapter = CategoryListFoodAdapter(this, listCategoryFood, this)
        binding.rcvCategory.adapter = mCategoryListFoodAdapter
    }

    override fun clickItem(position: Int) {
        val intent = Intent(this@CategoryMealActivity, DetailActivity::class.java)
        val bundle = Bundle()
        val categoryFood = listCategoryFood[position]
        bundle.putString(HomeFragment.MEAL_ID, categoryFood.idMeal)
        bundle.putString(HomeFragment.MEAL_NAME, categoryFood.strMeal)
        bundle.putString(HomeFragment.MEAL_THUMB, categoryFood.strMealThumb)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}