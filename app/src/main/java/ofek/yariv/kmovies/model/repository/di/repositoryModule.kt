package ofek.yariv.kmovies.model.repository.di

import ofek.yariv.kmovies.model.repository.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MoviesRepository(
            moviesDatabase = get(),
            moviesService = get(),
            movieDetailsService = get(),
            moviesConverter = get(),
            timeFrameManager = get(),
        )
    }
}