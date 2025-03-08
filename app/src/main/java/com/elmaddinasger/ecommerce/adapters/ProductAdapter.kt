package com.elmaddinasger.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmaddinasger.ecommerce.databinding.ItemProductBinding
import com.elmaddinasger.ecommerce.models.LocalProductModel

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<LocalProductModel>() {
        override fun areItemsTheSame(
            oldItem: LocalProductModel,
            newItem: LocalProductModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LocalProductModel,
            newItem: LocalProductModel
        ): Boolean {
           return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = asyncListDiffer.currentList[position]
        holder.bind(currentProduct)
    }

    fun setList (currentProducts: List<LocalProductModel>) {
        asyncListDiffer.submitList(currentProducts)
    }
    inner class ProductViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentProduct: LocalProductModel) {
            binding.apply {
                tvProductPrice.text = currentProduct.price.toString()
                tvProductName.text = currentProduct.title
                tvProductCategory.text = currentProduct.category
            }
            Glide.with(binding.root.context)
                .load(currentProduct.image)
                .into(binding.imgProductImage)
        }
    }
}