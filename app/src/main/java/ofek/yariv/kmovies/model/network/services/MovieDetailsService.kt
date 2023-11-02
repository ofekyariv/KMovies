package ofek.yariv.kmovies.model.network.services

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsService {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetailsResponse
}

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "adult") val adult: Boolean? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "budget") val budget: Long? = null,
    @Json(name = "genres") val genres: List<Genre>? = null,
    @Json(name = "original_title") val originalTitle: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "popularity") val popularity: Double? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "revenue") val revenue: Long? = null,
    @Json(name = "runtime") val runtime: Int? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String? = null
)
