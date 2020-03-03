package com.manuelcarvalho.magneto.model

import io.reactivex.Single
import retrofit2.http.GET

interface MagApi {
    @GET("?id=BOU&format=json")
    fun getGitJobs(): Single<MagnetoData>
}