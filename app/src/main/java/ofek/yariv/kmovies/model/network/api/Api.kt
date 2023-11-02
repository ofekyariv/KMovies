package ofek.yariv.kmovies.model.network.api

object Api {
    private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w780"

    fun getPosterPath(posterPath: String): String {
        return "$BASE_POSTER_PATH$posterPath"
    }
}