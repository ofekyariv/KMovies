package ofek.yariv.kmovies.model.db.saved_movies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ofek.yariv.kmovies.model.data.MovieDetails

@Database(
    entities = [MovieDetails::class],
    version = 1,
    exportSchema = false
)
abstract class SavedMoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): SavedMoviesDao

    companion object {
        @Volatile
        private var instance: SavedMoviesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SavedMoviesDatabase::class.java,
                "saved_movies_db.db"
            ).build()
    }
}