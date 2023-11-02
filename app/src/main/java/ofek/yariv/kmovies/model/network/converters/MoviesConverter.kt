package ofek.yariv.kmovies.model.network.converters

import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.MovieDetails
import ofek.yariv.kmovies.model.network.services.MovieDetailsResponse
import ofek.yariv.kmovies.model.network.services.MoviesResponse

class MoviesConverter {

    fun convertMoviesResponseToMovies(moviesResponse: MoviesResponse): List<Movie> =
        moviesResponse.movies?.map { movieResponse ->
            Movie(
                id = movieResponse.id,
                adult = movieResponse.adult,
                backdropPath = movieResponse.backdropPath,
                title = movieResponse.title,
                originalLanguage = movieResponse.originalLanguage,
                originalTitle = movieResponse.originalTitle,
                overview = movieResponse.overview,
                posterPath = movieResponse.posterPath,
                mediaType = movieResponse.mediaType,
                genreIds = movieResponse.genreIds?.joinToString(separator = ","),
                popularity = movieResponse.popularity,
                releaseDate = movieResponse.releaseDate,
                video = movieResponse.video,
                voteAverage = movieResponse.voteAverage,
                voteCount = movieResponse.voteCount
            )
        } ?: emptyList()

    fun convertMovieDetailsResponseToMovieDetails(movieDetailsResponse: MovieDetailsResponse): MovieDetails =
        MovieDetails(
            adult = movieDetailsResponse.adult,
            backdropPath = movieDetailsResponse.backdropPath,
            budget = movieDetailsResponse.budget,
            genres = movieDetailsResponse.genres?.joinToString(separator = ", ") { it.name.toString() },
            id = movieDetailsResponse.id,
            originalTitle = movieDetailsResponse.originalTitle,
            overview = movieDetailsResponse.overview,
            popularity = movieDetailsResponse.popularity,
            posterPath = movieDetailsResponse.posterPath,
            releaseDate = movieDetailsResponse.releaseDate,
            revenue = movieDetailsResponse.revenue,
            runtime = movieDetailsResponse.runtime,
            status = movieDetailsResponse.status,
            title = movieDetailsResponse.title,
            voteAverage = movieDetailsResponse.voteAverage,
            voteCount = movieDetailsResponse.voteCount
        )

    fun convertMovieDetailsToMovie(movieDetails: MovieDetails): Movie =
        Movie(
            adult = movieDetails.adult,
            backdropPath = movieDetails.backdropPath,
            id = movieDetails.id,
            title = movieDetails.title,
            originalTitle = movieDetails.originalTitle,
            overview = movieDetails.overview,
            posterPath = movieDetails.posterPath,
            popularity = movieDetails.popularity,
            releaseDate = movieDetails.releaseDate,
            voteAverage = movieDetails.voteAverage,
            voteCount = movieDetails.voteCount
        )
}
