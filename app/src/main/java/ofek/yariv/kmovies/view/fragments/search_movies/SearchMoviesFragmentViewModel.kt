package ofek.yariv.kmovies.view.fragments.search_movies

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchMoviesFragmentViewModel : ViewModel() {
    private val moviesResultFlow = MutableStateFlow<List<Boolean>>(emptyList())
    val moviesResult = moviesResultFlow.asStateFlow()
}