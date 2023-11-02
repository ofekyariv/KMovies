package ofek.yariv.kmovies.view.fragments.di

import ofek.yariv.kmovies.view.fragments.movie_details.MovieDetailsFragmentViewModel
import ofek.yariv.kmovies.view.fragments.movies.MoviesFragmentViewModel
import ofek.yariv.kmovies.view.fragments.saved_movies.SavedMoviesViewModel
import ofek.yariv.kmovies.view.fragments.search_movies.SearchMoviesFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fragmentModule = module {
    viewModel {
        MovieDetailsFragmentViewModel(
            moviesRepository = get(),
        )
    }

    viewModel {
        SavedMoviesViewModel(
            moviesRepository = get(),
        )
    }

    viewModel {
        MoviesFragmentViewModel()
    }

    viewModel {
        SearchMoviesFragmentViewModel(
            moviesRepository = get(),
        )
    }
}