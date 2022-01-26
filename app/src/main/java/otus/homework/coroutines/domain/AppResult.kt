package otus.homework.coroutines.domain

sealed class AppResult<out T : Any> {

    object Loading : AppResult<Nothing>()
    object Empty : AppResult<Nothing>()
    data class Failure(val throwable: Throwable) : AppResult<Nothing>()
    data class Success<out T : Any>(val data: T) : AppResult<T>()
}