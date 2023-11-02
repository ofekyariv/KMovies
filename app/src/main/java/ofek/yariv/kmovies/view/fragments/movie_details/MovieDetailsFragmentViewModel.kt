package ofek.yariv.kmovies.view.fragments.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.model.data.MovieDetails
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.model.repository.MoviesRepository

class MovieDetailsFragmentViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val movieDetailsResultFlow = MutableStateFlow<Resource<MovieDetails>>(Resource.None())
    val movieDetailsResult = movieDetailsResultFlow.asStateFlow()

    private val saveMovieResultFlow = MutableStateFlow<Resource<Void>>(Resource.None())
    val saveMovieResult = saveMovieResultFlow.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        movieDetailsResultFlow.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val movieDetails = moviesRepository.getMovieDetails(movieId = movieId)
                movieDetailsResultFlow.value = Resource.Success(movieDetails)
            } catch (e: Exception) {
                movieDetailsResultFlow.value = Resource.Failure(e.message)
            }
        }
    }

    fun saveMovie() {
        saveMovieResultFlow.value = Resource.Loading()
        viewModelScope.launch {
            try {
                moviesRepository.saveMovie(movieDetailsResultFlow.value.data!!)
                saveMovieResultFlow.value = Resource.Success()
            } catch (e: Exception) {
                saveMovieResultFlow.value = Resource.Failure(e.message)
            }
        }
    }
}