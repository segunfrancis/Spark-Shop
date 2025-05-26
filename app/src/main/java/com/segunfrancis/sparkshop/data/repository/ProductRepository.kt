package com.segunfrancis.sparkshop.data.repository

import com.segunfrancis.sparkshop.data.local.DimensionsEntity
import com.segunfrancis.sparkshop.data.local.ProductDao
import com.segunfrancis.sparkshop.data.local.ProductEntity
import com.segunfrancis.sparkshop.data.local.ProductWithDetails
import com.segunfrancis.sparkshop.data.local.RatingEntity
import com.segunfrancis.sparkshop.data.remote.Dimensions
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.remote.Rating
import com.segunfrancis.sparkshop.data.remote.SparkShopApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val dispatcher: CoroutineDispatcher,
    private val api: SparkShopApi
) : ProductRepository {
    override suspend fun addProduct(
        product: ProductEntity,
        rating: RatingEntity,
        dimensionsEntity: DimensionsEntity
    ) {
        dao.insertProduct(product)
        dao.insertRating(rating)
        dao.insertDimensions(dimensionsEntity)
    }

    override suspend fun addProductsWithRelations(products: List<ProductWithRelations>) {
        withContext(dispatcher) {
            products.forEach { item ->
                dao.insertProductWithRelations(
                    item.product,
                    item.rating,
                    item.dimensions
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllProducts(): Flow<List<ProductWithDetails>> {
        return dao.getProductsWithDetails().flatMapConcat { productsWithDetails ->
            flow {
                emit(productsWithDetails)
                if (productsWithDetails.isEmpty()) {
                    val products = api.getAllProducts()
                    val productsWithRelation = products.map { product ->
                        ProductWithRelations(
                            product = product.toProductEntity(),
                            rating = product.rating.toRatingEntity(product.id),
                            dimensions = product.dimensions.toDimensionsEntity(product.id)
                        )
                    }
                    productsWithRelation.forEach { product ->
                        dao.insertProductWithRelations(
                            product.product,
                            product.rating,
                            product.dimensions
                        )
                    }
                }
            }.flowOn(dispatcher)
        }
    }

    override suspend fun clearProducts() = dao.deleteAllProducts()
}

interface ProductRepository {
    suspend fun addProduct(
        product: ProductEntity,
        rating: RatingEntity,
        dimensions: DimensionsEntity
    )

    suspend fun addProductsWithRelations(products: List<ProductWithRelations>)
    suspend fun getAllProducts(): Flow<List<ProductWithDetails>>
    suspend fun clearProducts()
}

data class ProductWithRelations(
    val product: ProductEntity,
    val rating: RatingEntity,
    val dimensions: DimensionsEntity
)

fun RatingEntity.toRating(): Rating {
    return with(this) {
        Rating(
            rate = this.rate,
            count = count
        )
    }
}

fun Rating.toRatingEntity(productId: Int): RatingEntity {
    return with(this) {
        RatingEntity(rate = rate, count = count, productId = productId)
    }
}

fun DimensionsEntity.toDimensions(): Dimensions {
    return with(this) {
        Dimensions(width = width, height = height, length = length)
    }
}

fun Dimensions.toDimensionsEntity(productId: Int): DimensionsEntity {
    return with(this) {
        DimensionsEntity(width = width, height = height, length = length, productId = productId)
    }
}

fun Product.toProductEntity(): ProductEntity {
    return with(this) {
        ProductEntity(
            id = id,
            price = price,
            title = title,
            category = category,
            description = description,
            image = image,
            stock = stock,
            shippingInformation = shippingInformation,
            percentageOff = percentageOff,
            returnPolicy = returnPolicy
        )
    }
}
