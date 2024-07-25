package com.example.appfood.Fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appfood.Models.Meal
import com.example.appfood.R
import com.example.appfood.ViewModel.HomeViewModel
import com.example.appfood.activities.DetailActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.databinding.FragmentMealBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "com.example.appfood.Fragments.idMeal"
private const val MEAL_NAME = "com.example.appfood.Fragments.nameMeal"
private const val MEAL_THUMB = "com.example.appfood.Fragments.thumbMeal"


class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var mMeal: Meal

    // TODO: Rename and change types of parameters
    private var idMeal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            idMeal = it.getString(MEAL_ID)
            viewModel.getMealById(idMeal!!)
        }

        observerMealBottomSheetFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)


        binding.bottomSheet.setOnClickListener {
            clickItem()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun clickItem() {
        val intent = Intent(activity, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(MEAL_ID, mMeal.idMeal)
        bundle.putString(MEAL_NAME, mMeal.strMeal)
        bundle.putString(MEAL_THUMB, mMeal.strMealThumb)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun observerMealBottomSheetFragment() {
        viewModel.observerBottomSheetMealLiveData().observe(viewLifecycleOwner) { meal ->
            mMeal = meal
            binding.apply {
                Glide.with(this@MealBottomSheetFragment).load(meal.strMealThumb).into(imgMeal)
                tvMealCategory.text = meal.strCategory
                tvMealLocation.text = meal.strArea
                tvMealName.text = meal.strMeal
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }

}