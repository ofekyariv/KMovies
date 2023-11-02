package ofek.yariv.kmovies.model.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val id: Int? = null,
    val title: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val mediaType: String? = null,
    val genreIds: String? = null,
    val popularity: Double? = null,
    val releaseDate: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
) : Parcelable