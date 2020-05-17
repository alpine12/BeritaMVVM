package id.alpine.beritamvvm.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.alpine.beritamvvm.models.Article
import id.alpine.beritamvvm.models.NewsResponse
import id.alpine.beritamvvm.repository.NewsRepository
import id.alpine.beritamvvm.utils.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsRespose: NewsResponse? = null

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsRespose: NewsResponse? = null

    init {
        getBreakingNews("id")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)

        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resources.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)

        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsRespose == null) {
                    breakingNewsRespose = resultResponse
                } else {
                    val oldArticle = breakingNewsRespose?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resources.Success(breakingNewsRespose ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsRespose == null) {
                    searchNewsRespose = resultResponse
                } else {
                    val oldArticle = searchNewsRespose?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resources.Success(searchNewsRespose ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedArticle() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}