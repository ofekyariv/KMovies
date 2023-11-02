package ofek.yariv.kmovies.model.network.services

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMoviesService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviesResponse
}

