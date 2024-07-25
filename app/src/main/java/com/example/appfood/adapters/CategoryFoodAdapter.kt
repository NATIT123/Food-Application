package com.example.appfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appfood.Models.Category
import com.example.appfood.Models.CategoryMeal
import com.example.appfood.databinding.LayoutViewCategoryFoodBinding

class CategoryFoodAdapter(
    private val context: Context,
    private val listCategoryFood: MutableList<CategoryMeal>,
    private val mOnClickCategoryFood: onClickCategoryFood
) :
    RecyclerView.Adapter<CategoryFoodAdapter.ItemViewHolder>() {


    interface onClickCategoryFood {
        fun clickItemCategory(position: Int)
    }

    inner class ItemViewHolder(val layoutViewCategoryFood: LayoutViewCategoryFoodBinding) :
        RecyclerView.ViewHolder(layoutViewCategoryFood.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutViewCategoryFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategoryFood.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meal = listCategoryFood[position]
        holder.layoutViewCategoryFood.apply {
            Glide.with(context).load(meal.strCategoryThumb).into(imgFoodCategory)
            tvFoodCategoryName.text = meal.strCategory
            root.setOnClickListener {
                mOnClickCategoryFood.clickItemCategory(position)
            }
        }
    }
}