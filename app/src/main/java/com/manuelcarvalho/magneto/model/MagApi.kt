package com.casa.azul.dogs.model

import com.manuelcarvalho.magneto.model.MagnetoData
import io.reactivex.Single
import retrofit2.http.GET

interface MagApi {
    @GET("/?id=BOU&format=json")
    fun getGitJobs(): Single<List<MagnetoData>>
}