package com.manuelcarvalho.magneto.model


import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MagApiService"
class MagApiService {
    //https://geomag.usgs.gov/ws/edge/?id=BOU&format=json
    private val BASE_URL = "https://geomag.usgs.gov/ws/edge/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(MagApi::class.java)

    fun getMagData(): Single<MagnetoData> {
        return api.getReadings()
    }

}