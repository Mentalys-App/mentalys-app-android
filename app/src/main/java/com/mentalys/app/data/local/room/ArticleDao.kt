package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.data.local.entity.SpecialistEntity

@Dao
interface ArticleDao {

    // Single Article
    @Query("SELECT * FROM article WHERE id = :id")
    fun getArticle(id: String): LiveData<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    // List of Articles
    @Query("SELECT * FROM article_list")
    fun getListArticle(): LiveData<List<ArticleListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListArticle(article: List<ArticleListEntity>)

    // Foods
    @Query("SELECT * FROM food")
    fun getFood(): LiveData<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: List<FoodEntity>)

}