package ofek.yariv.kmovies.model.db.saved_movies

import androidx.room.*
import ofek.yariv.kmovies.model.data.MovieDetails

@Dao
interface SavedMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movieDetails: MovieDetails): Long

    @Query("SELECT * FROM saved_movies")
    suspend fun getAllSavedMovies(): List<MovieDetails>

    @Query("SELECT * FROM saved_movies WHERE id = :movieId")
    suspend fun getSavedMovie(movieId: Int): MovieDetails?

    @Query("DELETE FROM saved_movies WHERE id = :movieId")
    suspend fun deleteSavedMovie(movieId: Int)
}