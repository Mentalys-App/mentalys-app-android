package com.mentalys.app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.remote.response.article.ArticleListItem

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAllArticles(): LiveData<List<ArticleEntity>>

//    @Query("SELECT * FROM article_table WHERE isFavorite = 1")
//    fun getFavoriteStory(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(story: ArticleEntity)




    @Query("SELECT * FROM article")
    fun getAllListArticle(): LiveData<List<ArticleListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListArticle(story: List<ArticleListItem>)

//
//    @Update
//    fun updateFavoriteStory(story: Article)

//    @Query("SELECT * FROM article_table ORDER BY createdAt DESC")
//    suspend fun getStoriesForWidget(): List<ArticleEntity>

}