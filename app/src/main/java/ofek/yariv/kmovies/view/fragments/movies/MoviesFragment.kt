package ofek.yariv.kmovies.view.fragments.movies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.FragmentMoviesBinding
import ofek.yariv.kmovies.model.data.TimeFrame
import ofek.yariv.kmovies.utils.Constants
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.utils.managers.TimeFrameManager
import ofek.yariv.kmovies.view.activities.main.MainActivityViewModel
import ofek.yariv.kmovies.view.activities.main.listeners.ScrollListener
import ofek.yariv.kmovies.view.activities.main.listeners.TopBarTextChangeListener
import ofek.yariv.kmovies.view.adapters.MoviesAdapter
import ofek.yariv.kmovies.view.fragments.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : BaseFragment(R.layout.fragment_movies) {
    private lateinit var binding: FragmentMoviesBinding
    override fun getFragmentName() = ReportConstants.MOVIES
    override fun getFragmentItemId() = R.id.moviesFragment

    private val moviesFragmentViewModel: MoviesFragmentViewModel by viewModel()
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private val timeFrameManager: TimeFrameManager by inject()

    private lateinit var moviesAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        setupRecyclerView()
        observeChanges()
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter(
            onMovieClickListener = { movie ->
                val bundle = Bundle().apply {
                    putParcelable(Constants.MOVIE, movie)
                }
                findNavController().navigate(
                    R.id.action_moviesFragment_to_movieDetailsFragment,
                    bundle
                )
            }
        )
        binding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            launch {
                mainActivityViewModel.trendingMovies.collect { pagingData ->
                    moviesAdapter.submitData(pagingData)
                }
            }
            launch {
                mainActivityViewModel.pagingError.collect { throwable ->
                    throwable?.let {
                        showToast(getString(R.string.error))
                    }
                }
            }
        }
        setupSwipeToRefresh()
        setupScrollToTop()
        setupChangeTimeFrame()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        moviesAdapter.refresh()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupScrollToTop() {
        binding.fabScrollToTop.setOnClickListener {
            binding.rvMovies.scrollToPosition(0)
        }
        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isAtTop(recyclerView)) {
                    binding.fabScrollToTop.show()
                    binding.fabChangeTimeFrame.hide()
                    (activity as? ScrollListener)?.onScrolledDown()
                } else {
                    binding.fabScrollToTop.hide()
                    binding.fabChangeTimeFrame.show()
                    (activity as? ScrollListener)?.onScrolledUp()
                }
            }

            private fun isAtTop(recyclerView: RecyclerView): Boolean {
                return !recyclerView.canScrollVertically(-1)
            }
        })
    }

    private fun setupChangeTimeFrame() {
        binding.fabChangeTimeFrame.setOnClickListener {
            timeFrameManager.changeTimeFrame()
            when (timeFrameManager.getTimeFrame()) {
                TimeFrame.DAY -> {
                    binding.fabChangeTimeFrame.text =
                        getString(R.string.change_to_trending_this_week)
                    (activity as? TopBarTextChangeListener)?.ChangeTopBarText(text = getString(R.string.trending_today))
                }

                TimeFrame.WEEK -> {
                    binding.fabChangeTimeFrame.text =
                        getString(R.string.change_to_trending_today)
                    (activity as? TopBarTextChangeListener)?.ChangeTopBarText(text = getString(R.string.trending_this_week))
                }
            }
            refreshData()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}