package ofek.yariv.kmovies.model.repository.di

import ofek.yariv.kmovies.model.repository.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MoviesRepository(
            savedMoviesDatabase = get(),
            trendingMoviesDayDatabase = get(),
            trendingMoviesWeekDatabase = get(),
            moviesService = get(),
            movieDetailsService = get(),
            moviesConverter = get(),
            timeFrameManager = get(),
            internetManager = get(),
        )
    }
}