package com.elmaddinasger.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaddinasger.ecommerce.databinding.ItemCategoryBinding
import com.elmaddinasger.ecommerce.models.CategoryModel

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(
            oldItem: CategoryModel,
            newItem: CategoryModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryModel,
            newItem: CategoryModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = asyncListDiffer.currentList[position]
        holder.bind(currentCategory)
    }
    fun setList (currentList: List<CategoryModel>){
        asyncListDiffer.submitList(currentList)
    }
    inner class CategoryViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentCategory: CategoryModel) {
            binding.tvCategory.text = currentCategory.categoryName
        }
    }
}