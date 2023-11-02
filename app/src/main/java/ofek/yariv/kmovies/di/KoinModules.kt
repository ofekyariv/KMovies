package ofek.yariv.kmovies.di

import ofek.yariv.kmovies.model.db.di.dbModule
import ofek.yariv.kmovies.model.network.di.networkModule
import ofek.yariv.kmovies.model.repository.di.repositoryModule
import ofek.yariv.kmovies.utils.initializable.di.initializableModule
import ofek.yariv.kmovies.utils.managers.di.managersModule
import ofek.yariv.kmovies.view.activities.main.di.mainActivityModule
import ofek.yariv.kmovies.view.fragments.di.fragmentModule
import org.koin.dsl.module

val modules = module {
    includes(
        appModule,
        initializableModule,
        mainActivityModule,
        fragmentModule,
        networkModule,
        dbModule,
        repositoryModule,
        managersModule,
    )
}