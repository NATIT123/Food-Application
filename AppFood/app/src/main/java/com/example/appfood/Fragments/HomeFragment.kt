package com.example.appfood.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appfood.Fragments.bottomsheet.MealBottomSheetFragment
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.Models.Meal
import com.example.appfood.Models.MealPopular
import com.example.appfood.R
import com.example.appfood.ViewModel.HomeViewModel
import com.example.appfood.activities.CategoryMealActivity
import com.example.appfood.activities.DetailActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.adapters.CategoryFoodAdapter
import com.example.appfood.adapters.PopularFoodAdapter
import com.example.appfood.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), PopularFoodAdapter.iClickItemPopularFood,
    CategoryFoodAdapter.onClickCategoryFood {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel


    private lateinit var meal: Meal


    private var listPopularMeal = mutableListOf<MealPopular>()

    private var listCategoryMeal = mutableListOf<CategoryMeal>()

    private lateinit var mPopularFoodAdapter: PopularFoodAdapter

    private lateinit var mCategoryFoodAdapter: CategoryFoodAdapter

    companion object {
        val MEAL_ID = "com.example.appfood.Fragments.idMeal"
        val MEAL_NAME = "com.example.appfood.Fragments.nameMeal"
        val MEAL_THUMB = "com.example.appfood.Fragments.thumbMeal"
        val MEAL_CATEGORY = "com.example.appfood.Fragments.categoryMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        goToDetailActivity()

        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getApiRandom()
        observerRandomMeal()
        viewModel.getPopularFood("Seafood")
        observerPopularMeal()

        viewModel.getCategoryFood()
        observerCategoryMeal()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun goToDetailActivity() {
        binding.imgMeal.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString(MEAL_ID, meal.idMeal)
            bundle.putString(MEAL_NAME, meal.strMeal)
            bundle.putString(MEAL_THUMB, meal.strMealThumb)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) {
            this.meal = it
            Glide.with(this@HomeFragment).load(it.strMealThumb).into(binding.imgMeal)
        }
    }

    private fun observerPopularMeal() {
        viewModel.observerPopularMealLiveData().observe(viewLifecycleOwner) {
            this.listPopularMeal = it

            mPopularFoodAdapter =
                PopularFoodAdapter(requireActivity(), listPopularMeal, this)

            binding.rcvPopular.adapter = mPopularFoodAdapter

            binding.rcvPopular.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun observerCategoryMeal() {
        viewModel.observerCategoryMealLiveData().observe(viewLifecycleOwner) {
            this.listCategoryMeal = it

            mCategoryFoodAdapter = CategoryFoodAdapter(requireActivity(), listCategoryMeal, this)

            binding.rcvCategories.adapter = mCategoryFoodAdapter

            binding.rcvCategories.layoutManager =
                GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)

        }
    }

    override fun clickItem(position: Int) {
        val mMealBottomSheetFragment =
            MealBottomSheetFragment.newInstance(listPopularMeal[position].idMeal)
        mMealBottomSheetFragment.show(childFragmentManager, "Meal Info")
    }

    override fun clickItemCategory(position: Int) {
        val intent = Intent(activity, CategoryMealActivity::class.java)
        val foodCategory = listCategoryMeal[position]
        intent.putExtra(MEAL_CATEGORY, foodCategory.strCategory)
        startActivity(intent)
    }


}