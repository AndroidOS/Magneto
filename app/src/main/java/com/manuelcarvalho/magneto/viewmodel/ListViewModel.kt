package com.manuelcarvalho.magneto.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.manuelcarvalho.magneto.model.Mag
import com.manuelcarvalho.magneto.model.MagApiService
import com.manuelcarvalho.magneto.model.MagDatabase
import com.manuelcarvalho.magneto.model.MagnetoData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


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
                        storeQuakesLocally(magData)
                        //Log.d(TAG, "List size =  ${values}")
//                        if (values != null) {
//                            readings.value = values
//
//                        }
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

    private fun storeQuakesLocally(magData: MagnetoData) {
        launch {
            val list = createReadingList(magData)
//            Log.d(TAG, "List size Store =  ${list.size}")
            val dao = MagDatabase(getApplication()).magDao()
            dao.deleteAllReadings()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            Log.d(TAG, "Store result =  ${result}")
            fetchFromDatabase()
        }
    }

    private fun fetchFromDatabase() {

        launch {
            var list = mutableListOf<Double>()
            val mags = MagDatabase(getApplication()).magDao().getAllReadings()
            for (r in mags) {
                list.add(r.reading)
            }
            readings.value = list

            Toast.makeText(
                getApplication(),
                "Readings retrieved from database. ${mags.size} objects",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createReadingList(magData: MagnetoData): List<Mag> {
        var list = mutableListOf<Mag>()
        val b: List<Double>? = magData.values?.get(0)?.values

        for (c in 0 until (b?.size ?: 0)) {
            if (b?.get(c) != null) {
                list.add(Mag((b.get(c))))
            }
        }
        return list
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