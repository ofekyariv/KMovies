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

    private val isMovieSavedResultFlow = MutableStateFlow<Resource<Boolean>>(Resource.None())
    val isMovieSavedResult = isMovieSavedResultFlow.asStateFlow()

    private val saveMovieResultFlow = MutableStateFlow<Resource<Void>>(Resource.None())
    val saveMovieResult = saveMovieResultFlow.asStateFlow()

    private val deleteMovieResultFlow = MutableStateFlow<Resource<Void>>(Resource.None())
    val deleteMovieResult = deleteMovieResultFlow.asStateFlow()

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

    fun checkIfMovieIsSaved(movieId: Int) {
        viewModelScope.launch {
            try {
                val isMovieSaved = moviesRepository.isMovieInSavedMovies(movieId = movieId)
                isMovieSavedResultFlow.value = Resource.Success(data = isMovieSaved)
            } catch (e: Exception) {
                isMovieSavedResultFlow.value = Resource.Failure(e.message)
            }
        }
    }

    fun onFabSaveMovieClicked(movieId: Int) {
        viewModelScope.launch {
            if (moviesRepository.isMovieInSavedMovies(movieId = movieId)) {
                deleteMovie(movieId = movieId)
            } else {
                saveMovie()
            }
        }
    }

    private suspend fun deleteMovie(movieId: Int) {
        deleteMovieResultFlow.value = Resource.Loading()
        try {
            moviesRepository.deleteMovie(movieId = movieId)
            deleteMovieResultFlow.value = Resource.Success()
        } catch (e: Exception) {
            deleteMovieResultFlow.value = Resource.Failure(e.message)
        }
    }

    private suspend fun saveMovie() {
        movieDetailsResultFlow.value.data?.let { movieDetails ->
            saveMovieResultFlow.value = Resource.Loading()
            try {
                moviesRepository.saveMovie(movieDetails)
                saveMovieResultFlow.value = Resource.Success()
            } catch (e: Exception) {
                saveMovieResultFlow.value = Resource.Failure(e.message)
            }
        } ?: run { saveMovieResultFlow.value = Resource.Failure() }
    }
}