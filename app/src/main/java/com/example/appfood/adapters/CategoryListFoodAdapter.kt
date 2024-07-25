package com.example.appfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appfood.Models.MealPopular
import com.example.appfood.databinding.LayoutViewListFoodCategoryBinding

class CategoryListFoodAdapter(
    private val context: Context,
    private var listCategory: MutableList<MealPopular>,
    private val mOnClickCategoryItem: onClickCategoryItem
) : RecyclerView.Adapter<CategoryListFoodAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val layoutViewListFoodCategory: LayoutViewListFoodCategoryBinding) :
        RecyclerView.ViewHolder(layoutViewListFoodCategory.root)

    interface onClickCategoryItem {
        fun clickItem(position: Int)
    }


    fun setData(listData: MutableList<MealPopular>) {
        this.listCategory = listData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListFoodAdapter.ItemViewHolder {
        val binding = LayoutViewListFoodCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: CategoryListFoodAdapter.ItemViewHolder, position: Int) {
        val food = listCategory[position]
        holder.layoutViewListFoodCategory.apply {
            tvFoodCategoryNameList.text = food.strMeal
            Glide.with(context).load(food.strMealThumb).into(imgFoodCategoryList)
            root.setOnClickListener {
                mOnClickCategoryItem.clickItem(position)
            }
        }
    }


}