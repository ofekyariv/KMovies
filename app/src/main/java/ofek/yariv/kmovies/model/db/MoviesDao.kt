package ofek.yariv.kmovies.model.db

import androidx.room.*
import ofek.yariv.kmovies.model.data.MovieDetails

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movieDetails: MovieDetails): Long

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieDetails>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovie(movieId: Int): MovieDetails?

    @Delete
    suspend fun delete(movieDetails: MovieDetails)
}