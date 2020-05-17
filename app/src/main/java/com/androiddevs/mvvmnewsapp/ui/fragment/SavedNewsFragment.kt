package id.alpine.beritamvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import id.alpine.beritamvvm.R
import id.alpine.beritamvvm.adapters.NewsAdapter
import id.alpine.beritamvvm.ui.NewsActivity
import id.alpine.beritamvvm.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var mView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view

        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnCLickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment2,
                bundle
            )
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer { article ->
            newsAdapter.differ.submitList(article)
        })

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val article = newsAdapter.differ.currentList[position]
            viewModel.deleteArticle(article)
            Snackbar.make(mView, "Successfuly deleted article", Snackbar.LENGTH_LONG).apply {
                setAction("undo") {
                    viewModel.saveArticle(article)
                }
                show()
            }
        }
    }
}