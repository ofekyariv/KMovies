package ofek.yariv.kmovies

import android.app.Application
import ofek.yariv.kmovies.utils.initializable.InitializeAppComponentsManager
import ofek.yariv.kmovies.di.modules
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KMoviesApp : Application() {
    private val initializeAppComponentsManager: InitializeAppComponentsManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KMoviesApp)
            modules(modules)
        }

        initializeAppComponentsManager.init()
    }
}