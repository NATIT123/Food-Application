package com.example.appfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appfood.Models.Meal
import com.example.appfood.databinding.LayoutViewListFoodCategoryBinding

class MealsAdapter(
    private val context: Context,
    private val mOnClickItemListener: onClickItemListener
) :
    RecyclerView.Adapter<MealsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val favoriteFoodBinding: LayoutViewListFoodCategoryBinding) :
        RecyclerView.ViewHolder(favoriteFoodBinding.root)

    interface onClickItemListener {
        fun onClickItem(position: Int)
    }


    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutViewListFoodCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val favoriteFood = differ.currentList[position]
        holder.favoriteFoodBinding.apply {
            tvFoodCategoryNameList.text = favoriteFood.strMeal
            Glide.with(context).load(favoriteFood.strMealThumb).into(imgFoodCategoryList)
            root.setOnClickListener {
                mOnClickItemListener.onClickItem(position)
            }
        }
    }


}