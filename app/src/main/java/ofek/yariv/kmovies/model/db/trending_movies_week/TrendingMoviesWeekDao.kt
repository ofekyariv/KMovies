package ofek.yariv.kmovies.model.db.trending_movies_week

import androidx.room.*
import ofek.yariv.kmovies.model.data.Movie

@Dao
interface TrendingMoviesWeekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<Movie>)

    @Query("SELECT * FROM trending_movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("DELETE FROM trending_movies")
    suspend fun deleteAllMovies()
}
