package ofek.yariv.kmovies.model.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ofek.yariv.kmovies.model.network.api.ApiKeyInterceptor
import ofek.yariv.kmovies.model.network.converters.MoviesConverter
import ofek.yariv.kmovies.model.network.services.MovieDetailsService
import ofek.yariv.kmovies.model.network.services.MoviesService
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MOVIES_RETROFIT = "MOVIES_RETROFIT"
private const val BASE_URL = "https://api.themoviedb.org/3/"

val networkModule = module {
    single {
        OkHttpClient
            .Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single(named(MOVIES_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single<MoviesService> {
        get<Retrofit>(named(MOVIES_RETROFIT)).create(MoviesService::class.java)
    }
    single<MovieDetailsService> {
        get<Retrofit>(named(MOVIES_RETROFIT)).create(MovieDetailsService::class.java)
    }
    single { MoviesConverter() }
}