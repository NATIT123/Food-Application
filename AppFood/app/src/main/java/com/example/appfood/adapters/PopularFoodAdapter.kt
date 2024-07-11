package com.example.appfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appfood.Models.MealPopular
import com.example.appfood.databinding.LayoutViewPopularFoodBinding

class PopularFoodAdapter(
    private val context: Context,
    private val listPopularFood: MutableList<MealPopular>,
    private val mOnClickPopularFood: iClickItemPopularFood
) :
    RecyclerView.Adapter<PopularFoodAdapter.ItemViewHolder>() {


    interface iClickItemPopularFood {
        fun clickItem(position: Int)
    }

    inner class ItemViewHolder(val layoutViewPopularFoodBinding: LayoutViewPopularFoodBinding) :
        RecyclerView.ViewHolder(layoutViewPopularFoodBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutViewPopularFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPopularFood.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val food = listPopularFood[position]
        holder.layoutViewPopularFoodBinding.apply {
            Glide.with(context).load(food.strMealThumb).into(imgFoodPopular)
            root.setOnLongClickListener {
                mOnClickPopularFood.clickItem(position)
                true
            }
        }
    }
}