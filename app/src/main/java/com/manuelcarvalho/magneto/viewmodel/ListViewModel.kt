package com.manuelcarvalho.magneto.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.manuelcarvalho.magneto.model.MagApiService
import com.manuelcarvalho.magneto.model.MagnetoData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


private const val TAG = "ListViewModel"

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val magService = MagApiService()
    private val disposable = CompositeDisposable()

    val readings = MutableLiveData<List<Double>>()

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        //loading.value = true
        disposable.add(
            magService.getMagData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MagnetoData>() {
                    override fun onSuccess(magData: MagnetoData) {
                        val values = magData.values?.get(0)?.values
                        //Log.d(TAG, "List size =  ${values}")
                        if (values != null) {
                            readings.value = values
                            
                        }
                        //Log.d(TAG, "List size =  ${createModel(values!!)}")
                        Toast.makeText(
                            getApplication(),
                            "samples retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(TAG, "List size =  ${readings.value?.size}")
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, " RxJava error")
                        //loading.value = false
                        e.printStackTrace()
                    }

                })
        )

    }

    private fun createModel(values: List<Double?>) {
        val i = values.size
        val localList = mutableListOf<Double>()

        if (i != 0) {
            for (n in 0..i) {
                //Log.d(TAG, " ${values.get(n)}")
                val sample = values.get(n)?.toDouble()
                if (sample != null) {
                    Log.d(TAG, "Add ${sample}")
                    localList.add(sample)
                }
            }
        }
        Log.d(TAG, "list Size  ${localList.size}")
        //return localList
        readings.value = localList
    }
}