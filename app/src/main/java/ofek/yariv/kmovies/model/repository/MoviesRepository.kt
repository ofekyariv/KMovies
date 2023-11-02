package ofek.yariv.kmovies.model.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.MovieDetails
import ofek.yariv.kmovies.model.data.TimeFrame
import ofek.yariv.kmovies.model.db.saved_movies.SavedMoviesDatabase
import ofek.yariv.kmovies.model.db.trending_movies_day.TrendingMoviesDayDatabase
import ofek.yariv.kmovies.model.db.trending_movies_week.TrendingMoviesWeekDatabase
import ofek.yariv.kmovies.model.network.converters.MoviesConverter
import ofek.yariv.kmovies.model.network.services.MovieDetailsService
import ofek.yariv.kmovies.model.network.services.MoviesService
import ofek.yariv.kmovies.model.network.services.SearchMoviesService
import ofek.yariv.kmovies.utils.managers.InternetManager
import ofek.yariv.kmovies.utils.managers.TimeFrameManager

const val TIMEOUT_DURATION = 5000L
class MoviesRepository(
    private val savedMoviesDatabase: SavedMoviesDatabase,
    private val trendingMoviesDayDatabase: TrendingMoviesDayDatabase,
    private val trendingMoviesWeekDatabase: TrendingMoviesWeekDatabase,
    private val moviesService: MoviesService,
    private val movieDetailsService: MovieDetailsService,
    private val searchMoviesService: SearchMoviesService,
    private val moviesConverter: MoviesConverter,
    private val timeFrameManager: TimeFrameManager,
    private val internetManager: InternetManager,
) {

    suspend fun getTrendingMovies(pageNumber: Int): List<Movie> {
        val timeFrame = timeFrameManager.getTimeFrame()
        val isInternetAvailable = internetManager.isInternetAvailable()
        val timeoutDuration = TIMEOUT_DURATION

        return try {
            withTimeout(timeoutDuration) {
                if (isInternetAvailable) {
                    fetchAndSaveMoviesFromService(pageNumber, timeFrame)
                } else {
                    if(pageNumber==1){
                        fetchMoviesFromDatabase(timeFrame)
                    }
                    else{
                        emptyList()
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            fetchMoviesFromDatabase(timeFrame)
        }
    }


    private suspend fun fetchAndSaveMoviesFromService(
        pageNumber: Int,
        timeFrame: TimeFrame
    ): List<Movie> {
        val moviesResponse = when (timeFrame) {
            TimeFrame.DAY -> moviesService.getTrendingMoviesDay(pageNumber = pageNumber)
            TimeFrame.WEEK -> moviesService.getTrendingMoviesWeek(pageNumber = pageNumber)
        }
        val movies = moviesConverter.convertMoviesResponseToMovies(moviesResponse)
        CoroutineScope(Dispatchers.IO).launch {
            saveMoviesToDatabase(timeFrame, movies)
        }
        return movies
    }

    private suspend fun saveMoviesToDatabase(timeFrame: TimeFrame, movies: List<Movie>) {
        when (timeFrame) {
            TimeFrame.DAY -> trendingMoviesDayDatabase.getTrendingMoviesDayDao().saveMovies(movies)
            TimeFrame.WEEK -> trendingMoviesWeekDatabase.getTrendingMoviesWeekDao()
                .saveMovies(movies = movies)
        }
    }

    private suspend fun fetchMoviesFromDatabase(timeFrame: TimeFrame): List<Movie> {
        return when (timeFrame) {
            TimeFrame.DAY -> trendingMoviesDayDatabase.getTrendingMoviesDayDao().getAllMovies()
            TimeFrame.WEEK -> trendingMoviesWeekDatabase.getTrendingMoviesWeekDao().getAllMovies()
        }
    }


    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return savedMoviesDatabase.getMoviesDao().getSavedMovie(movieId = movieId)
            ?: moviesConverter.convertMovieDetailsResponseToMovieDetails(
                movieDetailsResponse = movieDetailsService.getMovieDetails(movieId)
            )
    }

    suspend fun saveMovie(movieDetails: MovieDetails) {
        savedMoviesDatabase.getMoviesDao().saveMovie(movieDetails = movieDetails)
    }

    suspend fun deleteMovie(movieId: Int) {
        savedMoviesDatabase.getMoviesDao().deleteSavedMovie(movieId = movieId)
    }

    suspend fun isMovieInSavedMovies(movieId: Int): Boolean {
        return savedMoviesDatabase.getMoviesDao().getSavedMovie(movieId = movieId) != null
    }

    suspend fun getSavedMovies(): List<Movie> {
        val moviesDetails = savedMoviesDatabase.getMoviesDao().getAllSavedMovies()
        return moviesDetails.map {
            moviesConverter.convertMovieDetailsToMovie(movieDetails = it)
        }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        Log.d("TAG", "searchMovies: $query")
        val moviesResponse = searchMoviesService.searchMovies(query = query)
        Log.d("TAG", "searchMovies2: $moviesResponse")
        return moviesConverter.convertMoviesResponseToMovies(moviesResponse)
    }
}