package ofek.yariv.kmovies.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.data.MovieDetails

@Database(
    entities = [MovieDetails::class],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies_db.db"
            ).build()
    }
}