package com.overtimedevs.bordersproject.presentation.screens.settings_dialogue

import androidx.lifecycle.ViewModel
import com.overtimedevs.bordersproject.data.repository.UserRepository
import com.overtimedevs.bordersproject.domain.model.UserSettings
import com.overtimedevs.bordersproject.domain.use_case.user_settings.UserSettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val userSettingsUseCases: UserSettingsUseCases
) : ViewModel() {
    val savedUserSettings = userSettingsUseCases.getUserSettings()
    val newUserSettings = UserSettings()

    init{
        newUserSettings.originCountry = savedUserSettings.originCountry
        newUserSettings.isVaccinated = savedUserSettings.isVaccinated
        newUserSettings.isNotificationsEnabled = savedUserSettings.isNotificationsEnabled
    }

    fun getCountries() : List<String>{
        val locales: Array<Locale> = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country: String = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()

        return countries
    }

    fun saveNewSettings(){
        userSettingsUseCases.saveUserSettings(newUserSettings)
    }

    fun setOriginCountry(countryName: String){
        if(countryName != ""){
            newUserSettings.originCountry = countryName
        }
    }

    fun setVaccinationsStatus(isVaccinated: Boolean){
        newUserSettings.isVaccinated = isVaccinated
    }

    fun setNotificationStatus(isEnabled: Boolean){
        newUserSettings.isNotificationsEnabled = isEnabled
    }

    private fun getCountryCodeByName(countryName: String) : String? {
        return Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }
    }

    fun settingsChanged(): Boolean{
        return  savedUserSettings.isVaccinated != newUserSettings.isVaccinated ||
                savedUserSettings.originCountry != newUserSettings.originCountry
    }

    fun settingsAreValid() : Boolean{
        return newUserSettings.originCountry != UserSettings.defaultOriginCountry
    }
}