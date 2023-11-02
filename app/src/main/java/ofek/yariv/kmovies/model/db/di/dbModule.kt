package ofek.yariv.kmovies.model.db.di

import ofek.yariv.kmovies.model.db.saved_movies.SavedMoviesDatabase
import ofek.yariv.kmovies.model.db.trending_movies_day.TrendingMoviesDayDatabase
import ofek.yariv.kmovies.model.db.trending_movies_week.TrendingMoviesWeekDatabase
import org.koin.dsl.module

val dbModule = module {
    single { SavedMoviesDatabase(context = get()) }
    single { TrendingMoviesDayDatabase(context = get()) }
    single { TrendingMoviesWeekDatabase(context = get()) }
}