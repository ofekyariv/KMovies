package ofek.yariv.kmovies.di

import android.content.Context
import android.content.SharedPreferences
import ofek.yariv.kmovies.utils.SharedPreferencesKeys.SHARED_PREFERENCES_KEY
import org.koin.dsl.module

val appModule = module {
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            0
        )
    }
}