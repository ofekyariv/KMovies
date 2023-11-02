package ofek.yariv.kmovies.view.fragments.saved_movies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.FragmentSavedMoviesBinding
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.utils.Constants
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.view.adapters.SavedMoviesAdapter
import ofek.yariv.kmovies.view.fragments.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedMoviesFragment : BaseFragment(R.layout.fragment_saved_movies) {
    private val savedMoviesViewModel: SavedMoviesViewModel by viewModel()
    private lateinit var binding: FragmentSavedMoviesBinding

    override fun getFragmentName() = ReportConstants.SAVED_MOVIES
    override fun getFragmentItemId() = R.id.savedMoviesFragment

    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedMoviesBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        setupRecyclerView()
        observeChanges()
        savedMoviesViewModel.fetchSavedMovies()
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            launch {
                savedMoviesViewModel.savedMovies.collect { result ->
                    when (result) {
                        is Resource.None -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data?.let { savedMoviesAdapter.submitList(it) }
                        }

                        is Resource.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            handleError()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        savedMoviesAdapter = SavedMoviesAdapter(
            onMovieClickListener = { movie ->
                val bundle = Bundle().apply {
                    putParcelable(Constants.MOVIE, movie)
                }
                findNavController().navigate(
                    R.id.action_savedMoviesFragment_to_movieDetailsFragment,
                    bundle
                )
            }
        )
        binding.rvSavedMovies.apply {
            adapter = savedMoviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun handleError() {
        Toast.makeText(requireContext(), R.string.error_movie_details, Toast.LENGTH_SHORT)
            .show()
        findNavController().popBackStack()
    }
}