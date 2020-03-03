package com.manuelcarvalho.magneto.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.manuelcarvalho.magneto.model.MagApiService
import com.manuelcarvalho.magneto.model.MagnetoData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "ListViewModel"

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val magService = MagApiService()
    private val disposable = CompositeDisposable()

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
                        Log.d(TAG, "List size =  ${magData}")
                        Toast.makeText(
                            getApplication(),
                            "Jobs retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, " RxJava error")
                        //loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }
}