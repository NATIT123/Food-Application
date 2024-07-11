package com.example.appfood.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appfood.Models.Meal
import com.example.appfood.R
import com.example.appfood.ViewModel.HomeViewModel
import com.example.appfood.activities.DetailActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.adapters.MealsAdapter
import com.example.appfood.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), MealsAdapter.onClickItemListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mSearchMealsAdapter: MealsAdapter
    private var listSearchMeals = mutableListOf<Meal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvSearch.apply {
            mSearchMealsAdapter = MealsAdapter(requireActivity(), this@SearchFragment)
            layoutManager =
                GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
            adapter = mSearchMealsAdapter

        }

        binding.imgSearch.setOnClickListener {
            searchMeals()
        }

        observerSearchMealLiveData()

        var searchJob: Job? = null
        binding.edtSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(it.toString())
            }
        }

    }

    private fun searchMeals() {
        val searchQuery = binding.edtSearch.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun observerSearchMealLiveData() {
        viewModel.observerSearchMealLiveData().observe(viewLifecycleOwner) { mealList ->
            listSearchMeals = mealList.toMutableList()
            mSearchMealsAdapter.differ.submitList(mealList)
        }
    }

    override fun onClickItem(position: Int) {
        goToDetailActivity(position)
    }

    private fun goToDetailActivity(position: Int) {
        val meal = listSearchMeals[position]
        val intent = Intent(activity, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(HomeFragment.MEAL_ID, meal.idMeal)
        bundle.putString(HomeFragment.MEAL_NAME, meal.strMeal)
        bundle.putString(HomeFragment.MEAL_THUMB, meal.strMealThumb)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

