package ofek.yariv.kmovies.view.activities.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.repository.MoviesRepository
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.utils.managers.AnalyticsManager
import ofek.yariv.kmovies.view.fragments.movies.MoviesPagingSource

private const val PAGE_SIZE = 20

class MainActivityViewModel(
    private val analyticsManager: AnalyticsManager,
    private val moviesRepository: MoviesRepository,
) : ViewModel() {
    val trendingMovies: Flow<PagingData<Movie>> = getPaginationFlow().cachedIn(viewModelScope)

    fun reportMainActivityShown() {
        analyticsManager.report(
            "${ReportConstants.MAIN_ACTIVITY} ${ReportConstants.SHOWN}",
            ReportConstants.SHOWN
        )
    }

    private fun getPaginationFlow(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(moviesRepository = moviesRepository) }
        ).flow//todo handle error
        //todo move to fragment
    }

}