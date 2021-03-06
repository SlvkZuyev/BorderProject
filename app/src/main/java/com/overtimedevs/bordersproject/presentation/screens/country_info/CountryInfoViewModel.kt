package com.overtimedevs.bordersproject.presentation.screens.country_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.overtimedevs.bordersproject.data.repository.CountryRepository
import com.overtimedevs.bordersproject.domain.model.Country
import com.overtimedevs.bordersproject.domain.use_case.country.CountryUseCases
import com.overtimedevs.bordersproject.extensions.plusAssign
import com.overtimedevs.bordersproject.extensions.toCountryCard
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CountryInfoViewModel @Inject constructor (
    private val countryUseCases: CountryUseCases
    ) : ViewModel() {

    private val _country : MutableLiveData<Country> = MutableLiveData(null)
    val country : LiveData<Country> = _country

    private val compositeDisposable = CompositeDisposable()

    fun loadCountry(countryId: Int){
        compositeDisposable += countryUseCases.getCountryById(countryId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<Country>() {

                    override fun onError(e: Throwable) {
                        Log.d("Tracked", "onError: ${e.message}")
                    }

                    override fun onComplete() {
                        Log.d("Tracked", "onComplete: ")
                    }

                    override fun onNext(t: Country) {
                        _country.value = t
                    }
                })
    }
}