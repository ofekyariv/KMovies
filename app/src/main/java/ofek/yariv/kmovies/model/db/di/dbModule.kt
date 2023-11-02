package ofek.yariv.kmovies.model.db.di

import ofek.yariv.kmovies.model.db.MoviesDatabase
import org.koin.dsl.module

val dbModule = module {
    single { MoviesDatabase(context = get()) }
}