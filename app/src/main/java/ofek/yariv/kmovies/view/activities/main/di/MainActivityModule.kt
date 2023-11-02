package ofek.yariv.kmovies.view.activities.main.di

import androidx.appcompat.app.AppCompatActivity
import ofek.yariv.kmovies.view.activities.main.MainActivityViewModel
import ofek.yariv.kmovies.view.activities.main.MenuItemClickHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel { MainActivityViewModel(analyticsManager = get(), moviesRepository = get()) }
    factory { (activity: AppCompatActivity) ->
        MenuItemClickHelper(
            context = get(),
            activity = activity,
            analyticsManager = get(),
            themeManager = get(),
        )
    }
}