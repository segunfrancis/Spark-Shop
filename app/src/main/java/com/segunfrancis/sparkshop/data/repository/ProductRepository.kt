package com.segunfrancis.sparkshop.data.repository

import android.util.Log
import com.segunfrancis.sparkshop.data.local.DimensionsEntity
import com.segunfrancis.sparkshop.data.local.MetaEntity
import com.segunfrancis.sparkshop.data.local.ProductDao
import com.segunfrancis.sparkshop.data.local.ProductEntity
import com.segunfrancis.sparkshop.data.local.ProductWithDetails
import com.segunfrancis.sparkshop.data.local.ReviewEntity
import com.segunfrancis.sparkshop.data.remote.Dimensions
import com.segunfrancis.sparkshop.data.remote.Meta
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.remote.Review
import com.segunfrancis.sparkshop.data.remote.SparkShopApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val dispatcher: CoroutineDispatcher,
    private val api: SparkShopApi
) : ProductRepository {
    override suspend fun addProduct(
        product: ProductEntity,
        dimensions: DimensionsEntity,
        meta: MetaEntity,
        review: ReviewEntity
    ) {
        dao.insertProduct(product)
        dao.insertDimensions(dimensions)
        dao.insertMeta(meta)
        dao.insertReview(review)
    }

    override suspend fun addProductsWithRelations(products: List<ProductWithRelations>) {
        withContext(dispatcher) {
            products.forEach { item ->
                dao.insertProductWithRelations(
                    item.product,
                    item.dimensions,
                    item.meta,
                    item.reviews
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllProducts(): Flow<List<ProductWithDetails>> {
        Log.d("getAllProducts", "Called")
        return dao.getProductsWithDetails().flatMapLatest { productsWithDetails ->
            flow {
                emit(productsWithDetails)
                if (productsWithDetails.isEmpty()) {
                    val products = api.getAllProducts().products
                    val productsWithRelation = products.map { product ->
                        ProductWithRelations(
                            product = product.toProductEntity(),
                            dimensions = product.dimensions.toDimensionEntity(product.id),
                            meta = product.meta.toMetaEntity(product.id),
                            reviews = product.reviews.map { review -> review.toReviewEntity(product.id) }
                        )
                    }
                    productsWithRelation.forEach { product ->
                        dao.insertProductWithRelations(
                            product.product,
                            product.dimensions,
                            product.meta,
                            product.reviews
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
        dimensions: DimensionsEntity,
        meta: MetaEntity,
        review: ReviewEntity
    )

    suspend fun addProductsWithRelations(products: List<ProductWithRelations>)
    suspend fun getAllProducts(): Flow<List<ProductWithDetails>>
    suspend fun clearProducts()
}

data class ProductWithRelations(
    val product: ProductEntity,
    val dimensions: DimensionsEntity,
    val meta: MetaEntity,
    val reviews: List<ReviewEntity>
)

/*fun ProductEntity.toProduct(): Product {
    return with(this) {
        Product(
            id = id,
            availabilityStatus = availabilityStatus,
            category = category,
            description = description,
            price = price,
            discountPercentage = discountPercentage,
            images = images,
            tags = tags,
            minimumOrderQuantity = minimumOrderQuantity,
            rating = rating,
            brand = brand,
            stock = stock,
            sku = sku,
            shippingInformation = shippingInformation,
            title = title,
            weight = weight,
            thumbnail = thumbnail,
            returnPolicy = returnPolicy,
            dimensions =
        )
    }
}*/

fun DimensionsEntity?.toDimension(): Dimensions {
    return with(this) {
        Dimensions(
            depth = this?.depth ?: 0.0,
            width = this?.width ?: 0.0,
            height = this?.height ?: 0.0
        )
    }
}

fun MetaEntity?.toMeta(): Meta {
    return with(this) {
        Meta(
            barcode = this?.barcode,
            createdAt = this?.createdAt,
            updatedAt = this?.updatedAt,
            qrCode = this?.qrCode
        )
    }
}

fun ReviewEntity?.toReview(): Review {
    return with(this) {
        Review(
            comment = this?.comment,
            date = this?.date,
            reviewerName = this?.reviewerName,
            reviewerEmail = this?.reviewerEmail,
            rating = this?.rating ?: 0
        )
    }
}

fun Dimensions.toDimensionEntity(productId: Int): DimensionsEntity {
    return with(this) {
        DimensionsEntity(depth = depth, width = width, height = height, productId = productId)
    }
}

fun Meta.toMetaEntity(productId: Int): MetaEntity {
    return with(this) {
        MetaEntity(
            barcode = barcode,
            createdAt = createdAt,
            updatedAt = updatedAt,
            qrCode = qrCode,
            productId = productId
        )
    }
}

fun Review.toReviewEntity(productId: Int): ReviewEntity {
    return with(this) {
        ReviewEntity(
            comment = comment,
            date = date,
            reviewerName = reviewerName,
            reviewerEmail = reviewerEmail,
            rating = rating,
            productId = productId
        )
    }
}

fun Product.toProductEntity(): ProductEntity {
    return with(this) {
        ProductEntity(
            id = id,
            price = price,
            thumbnail = thumbnail,
            title = title,
            weight = weight,
            stock = stock,
            rating = rating,
            category = category,
            description = description,
            tags = tags,
            images = images,
            minimumOrderQuantity = minimumOrderQuantity,
            discountPercentage = discountPercentage,
            availabilityStatus = availabilityStatus,
            sku = sku,
            brand = brand,
            returnPolicy = returnPolicy,
            shippingInformation = shippingInformation
        )
    }
}
