package otus.homework.coroutines.data

import retrofit2.http.GET

interface CatsImageService {

    @GET("meow")
    suspend fun getCatImage(): CatsImage
}