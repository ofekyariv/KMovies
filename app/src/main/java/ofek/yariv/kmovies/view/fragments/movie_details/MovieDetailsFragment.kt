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
import ofek.yariv.kmovies.view.fragments.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {
    private val movieDetailsFragmentViewModel: MovieDetailsFragmentViewModel by viewModel()
    private lateinit var binding: FragmentMovieDetailsBinding
    override fun getFragmentName() = ReportConstants.MOVIE_DETAILS
    override fun getFragmentItemId() = R.id.movieDetailsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        val movie = requireArguments().parcelable<Movie>(Constants.MOVIE)
        if (movie?.id == null) {
            handleError()
            return
        }
        movieDetailsFragmentViewModel.fetchMovieDetails(movieId = movie.id)
        observeChanges()
        binding.fab.setOnClickListener {
            movieDetailsFragmentViewModel.saveMovie()
            //todo make sure it's saved, enable delete
        }
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            launch {
                movieDetailsFragmentViewModel.movieDetailsResult.collect { result ->
                    when (result) {
                        is Resource.None -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data?.let { bindMovieDetails(it) }
                        }

                        is Resource.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            handleError()
                        }
                    }
                }
            }

            launch {
                movieDetailsFragmentViewModel.saveMovieResult.collect { result ->
                    when (result) {
                        is Resource.None -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                R.string.movie_saved,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.fab.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_full_heart
                                )
                            )
                        }

                        is Resource.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                R.string.error_save_movie,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

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
            tvMovieTagline.text = movieDetails.tagLine
            tvMovieOverview.text = movieDetails.overview
            tvMovieGenre.text = movieDetails.genres
            tvMovieReleaseDate.text = movieDetails.releaseDate
            tvMovieRating.text = movieDetails.voteAverage.toString()
            tvMovieRuntime.text = movieDetails.runtime.toString()
            tvMovieBudget.text = movieDetails.budget.toString()
            tvMovieRevenue.text = movieDetails.revenue.toString()
        }
    }

    private fun handleError() {
        Toast.makeText(requireContext(), R.string.error_movie_details, Toast.LENGTH_SHORT)
            .show()
        findNavController().popBackStack()
    }
}
