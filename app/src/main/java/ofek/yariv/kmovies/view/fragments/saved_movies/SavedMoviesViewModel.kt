package ofek.yariv.kmovies.view.fragments.saved_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.model.repository.MoviesRepository

class SavedMoviesViewModel(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    private val savedMoviesFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.None())
    val savedMovies = savedMoviesFlow.asStateFlow()

    fun fetchSavedMovies() {
        savedMoviesFlow.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val savedMovies = moviesRepository.getSavedMovies()
                savedMoviesFlow.value = Resource.Success(savedMovies)
            } catch (e: Exception) {
                savedMoviesFlow.value = Resource.Failure(e.message)
            }
        }
    }

}