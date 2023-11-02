package ofek.yariv.kmovies.view.fragments.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.repository.MoviesRepository

class MoviesPagingSource(
    private val moviesRepository: MoviesRepository,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageNumber = params.key ?: 1
        return try {
            val movies = moviesRepository.getTrendingMovies(pageNumber = pageNumber)
            LoadResult.Page(
                data = movies,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (movies.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}