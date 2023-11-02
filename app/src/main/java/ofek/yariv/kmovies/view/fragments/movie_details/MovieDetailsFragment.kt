package ofek.yariv.kmovies.view.fragments.movie_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.FragmentMovieDetailsBinding
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.MovieDetails
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.model.network.api.Api
import ofek.yariv.kmovies.utils.Constants
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.utils.extensions.parcelable
import ofek.yariv.kmovies.utils.managers.InternetManager
import ofek.yariv.kmovies.view.activities.main.listeners.ScrollListener
import ofek.yariv.kmovies.view.fragments.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {
    private val movieDetailsFragmentViewModel: MovieDetailsFragmentViewModel by viewModel()
    private lateinit var binding: FragmentMovieDetailsBinding
    override fun getFragmentName() = ReportConstants.MOVIE_DETAILS
    override fun getFragmentItemId() = R.id.movieDetailsFragment

    private val internetManager: InternetManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        (activity as? ScrollListener)?.onScrolledDown()
        observeChanges()
        val movie = requireArguments().parcelable<Movie>(Constants.MOVIE)
        if (movie?.id == null) {
            handleError()
            return
        }
        movieDetailsFragmentViewModel.fetchMovieDetails(movieId = movie.id)
        movieDetailsFragmentViewModel.checkIfMovieIsSaved(movieId = movie.id)
        binding.fabSaveMovie.setOnClickListener {
            movieDetailsFragmentViewModel.onFabSaveMovieClicked(movieId = movie.id)
        }
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            launch {
                movieDetailsFragmentViewModel.movieDetailsResult.collect {
                    handleResourceState(it) { movieDetails ->
                        if (movieDetails != null) {
                            bindMovieDetails(
                                movieDetails = movieDetails
                            )
                        }
                    }
                }
            }
            launch {
                movieDetailsFragmentViewModel.saveMovieResult.collect {
                    handleResourceState(it) {
                        showToast(
                            getString(R.string.movie_saved)
                        ); changeFabIcon(true)
                    }
                }
            }
            launch {
                movieDetailsFragmentViewModel.deleteMovieResult.collect {
                    handleResourceState(
                        it
                    ) { showToast(getString(R.string.movie_deleted)); changeFabIcon(false) }
                }
            }
            launch {
                movieDetailsFragmentViewModel.isMovieSavedResult.collect { isMovieSaved ->
                    handleResourceState(
                        isMovieSaved
                    ) { changeFabIcon(it ?: false) }
                }
            }
        }
    }

    private fun <T> handleResourceState(resource: Resource<T>, onSuccess: (T?) -> Unit) {
        when (resource) {
            is Resource.None -> binding.progressBar.visibility = View.GONE
            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE; onSuccess(resource.data)
            }

            is Resource.Failure -> {
                binding.progressBar.visibility = View.GONE; handleError()
            }
        }
    }

    private fun bindMovieDetails(movieDetails: MovieDetails) {
        binding.apply {
            //use glide for poster
            Glide.with(requireContext())
                .load(movieDetails.posterPath?.let { Api.getPosterPath(it) })
                .into(ivMoviePoster)
            tvMovieTitle.text = movieDetails.title
            tvMovieOverview.text = movieDetails.overview
            tvMovieGenre.text = movieDetails.genres
            tvMovieReleaseDate.text = movieDetails.releaseDate
            tvMovieRating.text = String.format("%.1f", movieDetails.voteAverage)
        }
    }

    private fun changeFabIcon(isMovieSaved: Boolean) {
        val fabIconResource = if (isMovieSaved) {
            R.drawable.ic_full_heart
        } else {
            R.drawable.ic_empty_heart
        }
        binding.fabSaveMovie.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                fabIconResource
            )
        )
    }

    private fun handleError() {
        val errorMessage = if (internetManager.isInternetAvailable()) {
            getString(R.string.error)
        } else {
            getString(R.string.no_internet_connection)
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
            .show()
        findNavController().popBackStack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
