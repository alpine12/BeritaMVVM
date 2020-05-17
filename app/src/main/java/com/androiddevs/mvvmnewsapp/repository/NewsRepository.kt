package id.alpine.beritamvvm.repository

import id.alpine.beritamvvm.api.RetrofitInstance
import id.alpine.beritamvvm.db.ArticleDatabase
import id.alpine.beritamvvm.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.articleDao().upsert(article)

    fun getSavedNews() = db.articleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)
}