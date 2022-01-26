package otus.homework.coroutines.data

import otus.homework.coroutines.data.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("random?animal_type=cat")
    suspend fun getCatFact(): Response<Fact>
}