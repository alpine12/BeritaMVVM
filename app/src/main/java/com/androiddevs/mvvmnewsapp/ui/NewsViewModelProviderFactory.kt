package id.alpine.beritamvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.alpine.beritamvvm.repository.NewsRepository

@Suppress("UNCHECKED_CAST")
class NewsViewModelProviderFactory(val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}