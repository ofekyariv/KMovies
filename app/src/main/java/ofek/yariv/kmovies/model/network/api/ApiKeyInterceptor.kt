package ofek.yariv.kmovies.model.network.api

import okhttp3.Interceptor
import okhttp3.Response
import ofek.yariv.kmovies.BuildConfig

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
