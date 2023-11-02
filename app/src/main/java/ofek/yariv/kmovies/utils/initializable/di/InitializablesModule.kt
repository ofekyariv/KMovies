package ofek.yariv.kmovies.utils.initializable.di

import ofek.yariv.kmovies.utils.initializable.FirebaseInitializer
import ofek.yariv.kmovies.utils.initializable.InitializeAppComponentsManager
import ofek.yariv.kmovies.utils.managers.ThemeManager
import org.koin.dsl.module

val initializableModule = module {
    factory { FirebaseInitializer() }
    factory {
        InitializeAppComponentsManager(
            initializableAppComponent = listOf(
                get<FirebaseInitializer>(),
                get<ThemeManager>(),
            )
        )
    }
}