package ofek.yariv.kmovies.model.db.trending_movies_day

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ofek.yariv.kmovies.model.data.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class TrendingMoviesDayDatabase : RoomDatabase() {

    abstract fun getTrendingMoviesDayDao(): TrendingMoviesDayDao

    companion object {
        @Volatile
        private var instance: TrendingMoviesDayDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TrendingMoviesDayDatabase::class.java,
                "trending_movies_day_db.db"
            ).build()
    }
}