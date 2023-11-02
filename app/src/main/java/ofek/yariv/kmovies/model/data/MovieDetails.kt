package ofek.yariv.kmovies.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "saved_movies")
data class MovieDetails(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val budget: Long? = null,
    val genres: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagLine: String? = null,
    val title: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
) : Parcelable