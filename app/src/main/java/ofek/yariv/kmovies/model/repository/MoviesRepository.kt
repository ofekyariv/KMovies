package ofek.yariv.kmovies.model.repository

import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.MovieDetails
import ofek.yariv.kmovies.model.data.TimeFrame
import ofek.yariv.kmovies.model.db.MoviesDatabase
import ofek.yariv.kmovies.model.network.converters.MoviesConverter
import ofek.yariv.kmovies.model.network.services.MovieDetailsService
import ofek.yariv.kmovies.model.network.services.MoviesService
import ofek.yariv.kmovies.utils.managers.TimeFrameManager


class MoviesRepository(
    private val moviesDatabase: MoviesDatabase,
    private val moviesService: MoviesService,
    private val movieDetailsService: MovieDetailsService,
    private val moviesConverter: MoviesConverter,
    private val timeFrameManager: TimeFrameManager,
) {

    suspend fun getTrendingMovies(pageNumber: Int): List<Movie> {
        val response = when (timeFrameManager.getTimeFrame()) {
            TimeFrame.DAY -> moviesService.getTrendingMoviesDay(pageNumber = pageNumber)
            TimeFrame.WEEK -> moviesService.getTrendingMoviesWeek(pageNumber = pageNumber)
        }
        return moviesConverter.convertMoviesResponseToMovies(moviesResponse = response)
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return moviesConverter.convertMovieDetailsResponseToMovieDetails(
            movieDetailsService.getMovieDetails(movieId)
        )
    }

    suspend fun saveMovie(movieDetails: MovieDetails) {
        moviesDatabase.getMoviesDao().upsert(movieDetails = movieDetails)
    }

    suspend fun deleteMovie(movieDetails: MovieDetails) {
        moviesDatabase.getMoviesDao().delete(movieDetails = movieDetails)
    }

    suspend fun getSavedMovies(): List<Movie> {
        val moviesDetails = moviesDatabase.getMoviesDao().getAllMovies()
        return moviesDetails.map {
            moviesConverter.convertMovieDetailsToMovie(movieDetails = it)
        }
    }

    suspend fun getSavedMovieDetails(movieId: Int): MovieDetails? {
        return moviesDatabase.getMoviesDao().getMovie(movieId = movieId)
    }
}