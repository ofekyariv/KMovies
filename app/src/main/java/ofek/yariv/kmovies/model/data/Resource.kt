package ofek.yariv.kmovies.model.data

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Failure<T>(message: String? = null, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
    class None<T> : Resource<T>()
}