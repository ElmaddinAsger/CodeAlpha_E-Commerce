package com.elmaddinasger.ecommerce.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmaddinasger.ecommerce.dto.toLocalProductModel
import com.elmaddinasger.ecommerce.models.CategoryModel
import com.elmaddinasger.ecommerce.models.LocalProductModel
import com.elmaddinasger.ecommerce.retrofit.AppRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel() {
    private val _productList = MutableStateFlow<List<LocalProductModel>?>(null)
    val productList: StateFlow<List<LocalProductModel>?> = _productList

    private val _categories = MutableStateFlow<MutableList<CategoryModel>>(mutableListOf(CategoryModel(1,"All Categories")))
    val categories: StateFlow<List<CategoryModel>> = _categories

    fun fetchProducts () {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = AppRetrofit.productServices.getProduct()
                    if (response.isSuccessful) {
                        Log.e("Retrofit", response.body().toString())
                        val mutableProductList = mutableListOf<LocalProductModel>()
                        response.body()?.forEach { productItem ->
                            mutableProductList.add(productItem.toLocalProductModel())
                            addCategory(productItem.category)
                        }
                        _productList.value = mutableProductList
                    }
                }catch (e:Exception){
                    Log.e("RetrofitException", e.message.toString())
                }
            }
        }
    }
    private val categorySet = mutableSetOf<String>()
    private fun addCategory (categoryName: String) {
        if (categorySet.add(categoryName)){
            _categories.value.add(CategoryModel(_categories.value.size+1, categoryName))
        }
    }
}