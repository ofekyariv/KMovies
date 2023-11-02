package ofek.yariv.kmovies.view.fragments.search_movies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.FragmentSearchMoviesBinding
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.view.fragments.BaseFragment
import ofek.yariv.kmovies.view.fragments.movie_details.MovieDetailsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMoviesFragment : BaseFragment(R.layout.fragment_search_movies) {
    private val searchMoviesFragmentViewModel: MovieDetailsFragmentViewModel by viewModel()
    private lateinit var binding: FragmentSearchMoviesBinding

    override fun getFragmentName() = ReportConstants.SEARCH_MOVIES
    override fun getFragmentItemId() = R.id.searchMoviesFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchMoviesBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        observeChanges()
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            launch {
                //todo
            }
        }
    }
}