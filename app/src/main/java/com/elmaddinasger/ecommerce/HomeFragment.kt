package com.elmaddinasger.ecommerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmaddinasger.ecommerce.adapters.CategoryAdapter
import com.elmaddinasger.ecommerce.adapters.ProductAdapter
import com.elmaddinasger.ecommerce.databinding.FragmentHomeBinding
import com.elmaddinasger.ecommerce.models.CategoryModel
import com.elmaddinasger.ecommerce.viewModels.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
  private lateinit var binding: FragmentHomeBinding
  private lateinit var productViewModel : ProductViewModel
  private lateinit var productAdapter: ProductAdapter
  private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        productViewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]
        productViewModel.fetchProducts()
        productAdapter = ProductAdapter()
        categoryAdapter = CategoryAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProducts()
        getCategory()
    }

    private fun getProducts() {
        lifecycleScope.launch {
            productViewModel.productList.collectLatest { nullableProducts ->
                nullableProducts?.let { products ->
                    productAdapter.setList(products)
                    binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.rvProducts.adapter = productAdapter
                }
            }
        }
    }

    val categoryList = listOf(CategoryModel(1,"List1"),CategoryModel(2,"Listr2"))

    private fun getCategory () {
        lifecycleScope.launch {
            productViewModel.categories.collectLatest { categories ->
               categoryAdapter.setList(categories)
                binding.rvCategory.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvCategory.adapter = categoryAdapter
            }
        }
    }


}