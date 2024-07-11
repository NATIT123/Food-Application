package com.example.appfood.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.R
import com.example.appfood.ViewModel.HomeViewModel
import com.example.appfood.activities.CategoryMealActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.adapters.CategoryFoodAdapter
import com.example.appfood.adapters.CategoryListFoodAdapter
import com.example.appfood.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment(), CategoryFoodAdapter.onClickCategoryFood {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mCategoryAdapter: CategoryFoodAdapter

    private lateinit var listCategoryMeal: MutableList<CategoryMeal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCategoryList()

    }


    private fun observeCategoryList() {
        viewModel.observerCategoryMealLiveData().observe(viewLifecycleOwner) { it ->
            listCategoryMeal = it
            mCategoryAdapter = CategoryFoodAdapter(requireContext(), it, this)
            binding.rcvFragmentCategory.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                adapter = mCategoryAdapter
            }
        }
    }

    override fun clickItemCategory(position: Int) {
        val intent = Intent(activity, CategoryMealActivity::class.java)
        val foodCategory = listCategoryMeal[position]
        intent.putExtra(HomeFragment.MEAL_CATEGORY, foodCategory.strCategory)
        startActivity(intent)
    }
}