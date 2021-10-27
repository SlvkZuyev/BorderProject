package com.overtimedevs.bordersproject.data.repository

import android.annotation.SuppressLint
import com.overtimedevs.bordersproject.data.data_source.local.CountryDao
import com.overtimedevs.bordersproject.data.data_source.local.CountryLocalDataSource
import com.overtimedevs.bordersproject.data.data_source.local.model.CountriesStatistic
import com.overtimedevs.bordersproject.data.data_source.remote.CountryRemoteDataSource
import com.overtimedevs.bordersproject.data.data_source.remote.CountryApi
import com.overtimedevs.bordersproject.data.util.NetManager
import com.overtimedevs.bordersproject.domain.model.Country
import com.overtimedevs.bordersproject.domain.model.SessionInfo
import io.reactivex.Observable


class CountryRepository(
    private val netManager: NetManager,
    countryDao: CountryDao,
    countryApi: CountryApi,
    private val sessionRepository: SessionRepository
) {
    private val localDataSource = CountryLocalDataSource(countryDao)
    private val remoteDataSource = CountryRemoteDataSource(countryApi)

    @SuppressLint("CheckResult")
    fun getAllCountries(originCountryCode: String = "ua", loadFromRemote: Boolean = false): Observable<List<Country>> {
       if(netManager.isConnected() && loadFromRemote){
           return remoteDataSource.getCountries(originCountryCode).doOnNext {
               localDataSource.saveCountries(it)
               refreshSessionInfo(originCountryCode)
           }
       }
        return localDataSource.getAllCountries()
    }

    //todo: Починить это ебаное говнище
    fun getTrackedCountries(): Observable<List<Country>> {
        return localDataSource.getTrackedCountries()
    }

    fun addTrackedCountryById(countryId: Int){
        localDataSource.trackCountryById(countryId)
    }

    fun removeTrackedCountryById(countryId: Int){
        localDataSource.removeTrackedCountryById(countryId)
    }

    fun getTrackedCountriesStatistic() : Observable<CountriesStatistic>{
        return localDataSource.getTrackedCountriesStatistic()
    }

    fun getAllCountriesStatistic() : Observable<CountriesStatistic>{
        return localDataSource.getAllCountriesStatistic()
    }

    fun getCountryById(countryId: Int): Observable<Country?>{
        return localDataSource.getCountryById(countryId)
    }

    private fun refreshSessionInfo(loadedCountry: String){
        sessionRepository.saveSessionInfo(SessionInfo(
            loadedCountriesOriginCode = loadedCountry,
            lastFetchTime = System.currentTimeMillis()
        ))
    }
}
