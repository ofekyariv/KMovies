package ofek.yariv.kmovies.utils.managers.di

import android.app.Activity
import ofek.yariv.kmovies.utils.managers.AnalyticsManager
import ofek.yariv.kmovies.utils.managers.InternetManager
import ofek.yariv.kmovies.utils.managers.SpeechToTextManager
import ofek.yariv.kmovies.utils.managers.ThemeManager
import ofek.yariv.kmovies.utils.managers.TimeFrameManager
import org.koin.dsl.module

val managersModule = module {
    single { AnalyticsManager(context = get()) }
    single { ThemeManager(sharedPreferences = get()) }
    single { InternetManager(context = get()) }
    factory { (activity: Activity) -> SpeechToTextManager(activity = activity) }
    single { TimeFrameManager() }
}