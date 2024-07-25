package com.example.appfood.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.appfood.ViewModel.HomeViewModel
import com.example.appfood.activities.DetailActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.adapters.MealsAdapter
import com.example.appfood.databinding.FragmentFavotiteBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [FavotiteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavotiteFragment : Fragment(), MealsAdapter.onClickItemListener {

    private lateinit var binding: FragmentFavotiteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mMealsAdapter: MealsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavotiteBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        mMealsAdapter = MealsAdapter(requireContext(), this)

        binding.rcvFragmentFavorite.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mMealsAdapter
        }

        observerFavoriteMeals()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val food = mMealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(food)

                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    viewModel.insertMeal(food)
                }.show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rcvFragmentFavorite)


    }


    private fun observerFavoriteMeals() {
        viewModel.observerFavoriteMealLiveData().observe(requireActivity()) { it ->
            mMealsAdapter.differ.submitList(it)
        }
    }

    override fun onClickItem(position: Int) {
        goToDetailActivity(position)
    }

    private fun goToDetailActivity(position: Int) {
        val food = mMealsAdapter.differ.currentList[position]
        val intent = Intent(activity, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(HomeFragment.MEAL_ID, food.idMeal)
        bundle.putString(HomeFragment.MEAL_NAME, food.strMeal)
        bundle.putString(HomeFragment.MEAL_THUMB, food.strMealThumb)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}


