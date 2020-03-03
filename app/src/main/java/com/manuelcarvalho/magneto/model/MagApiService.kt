package com.manuelcarvalho.magneto.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MagApiService {
    //https://geomag.usgs.gov/ws/edge/?id=BOU&format=json
    private val BASE_URL = "https://geomag.usgs.gov/ws/edge/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MagApi::class.java)

    fun getMagData(): Single<MagnetoData> {
        return api.getGitJobs()
    }
}