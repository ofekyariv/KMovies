package ofek.yariv.kmovies.view.fragments.search_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.model.repository.MoviesRepository

class SearchMoviesFragmentViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val searchMoviesResultFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.None())
    val searchMoviesResult = searchMoviesResultFlow.asStateFlow()

    fun searchMovies(query: String) {
        searchMoviesResultFlow.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val movies = moviesRepository.searchMovies(query = query)
                searchMoviesResultFlow.value = Resource.Success(movies)
            } catch (e: Exception) {
                searchMoviesResultFlow.value = Resource.Failure(e.message)
            }
        }
    }
}