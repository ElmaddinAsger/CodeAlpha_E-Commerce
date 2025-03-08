package com.elmaddinasger.ecommerce.dto

import com.elmaddinasger.ecommerce.models.LocalProductModel
import com.elmaddinasger.ecommerce.models.ProductItem

fun ProductItem.toLocalProductModel (): LocalProductModel {
    return LocalProductModel(
        category = category,
        description = description,
        id = id,
        image = image,
        price = price,
        title = title
    )
}
