package com.overtimedevs.bordersproject.data.data_source.local

import com.overtimedevs.bordersproject.domain.model.CountriesStatistic
import com.overtimedevs.bordersproject.data.model.TrackedCountry
import com.overtimedevs.bordersproject.domain.model.Country
import io.reactivex.Observable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class CountryLocalDataSource @Inject constructor(val countryDao: CountryDao) {

    fun getAllCountries(): Observable<List<Country>> {
        return countryDao.getAllCountries()
    }

    fun getCountryById(countryId: Int) : Observable<Country?> {
        return countryDao.getCountryById(countryId)
    }

    fun getTrackedCountries(): Observable<List<Country>> {
        return countryDao.getTrackedCountries()
    }

    fun saveCountries(countries: List<Country>) {
        countryDao.insert(countries)
    }

    //todo: Not to use Global scope
    fun trackCountryById(countryId: Int) {
        GlobalScope.launch {
            countryDao.addTrackedCountry(TrackedCountry(countryId))
        }
    }

    fun removeTrackedCountryById(countryId: Int) {
        GlobalScope.launch {
            countryDao.removeTrackedCountry(countryId)
        }
    }

    fun getTreckedRestr(): Observable<Int> {
        return countryDao.getTrackedCountriesRestrictionCount()
    }

    fun getAllCountriesStatistic(): Observable<CountriesStatistic> {
        return Observable.zip(
            countryDao.getAllCountriesRestrictionCount(),
            countryDao.getAllCountriesOpenCount(),
            countryDao.getAllCountriesClosedCount(),
            { restriction, open, closed ->
                CountriesStatistic(
                    restrictions = restriction,
                    open = open,
                    closed = closed
                )
            }
        )
    }

    fun getTrackedCountriesStatistic(): Observable<CountriesStatistic> {
        return Observable.zip(
            countryDao.getTrackedCountriesRestrictionCount(),
            countryDao.getTrackedCountriesOpenCount(),
            countryDao.getTrackedCountriesClosedCount(),
            { restriction, open, closed ->
                CountriesStatistic(
                    restrictions = restriction,
                    open = open,
                    closed = closed
                )
            }
        )
    }
}