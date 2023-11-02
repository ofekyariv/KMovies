package ofek.yariv.kmovies.model.network.services

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {
    @GET("trending/movie/day")
    suspend fun getTrendingMoviesDay(@Query("page") pageNumber: Int): MoviesResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesWeek(@Query("page") pageNumber: Int): MoviesResponse
}

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    @Json(name = "page") val page: Int? = null,
    @Json(name = "total_results") val totalResults: Int? = null,
    @Json(name = "total_pages") val totalPages: Int? = null,
    @Json(name = "results") val movies: List<MovieResponse>? = null
)

@JsonClass(generateAdapter = true)
data class MovieResponse(
    @Json(name = "adult") val adult: Boolean? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "original_language") val originalLanguage: String? = null,
    @Json(name = "original_title") val originalTitle: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "media_type") val mediaType: String? = null,
    @Json(name = "genre_ids") val genreIds: List<Int>? = null,
    @Json(name = "popularity") val popularity: Double? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "video") val video: Boolean? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null
)