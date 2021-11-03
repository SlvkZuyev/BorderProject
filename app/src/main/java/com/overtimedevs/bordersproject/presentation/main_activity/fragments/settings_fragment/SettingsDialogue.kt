package com.overtimedevs.bordersproject.presentation.main_activity.fragments.settings_fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.overtimedevs.bordersproject.R
import com.overtimedevs.bordersproject.data.repository.UserRepository
import com.overtimedevs.bordersproject.databinding.FragmentSettingsBinding
import com.overtimedevs.bordersproject.domain.model.UserSettings
import com.overtimedevs.bordersproject.presentation.main_activity.fragments.all_countries_fragment.AllCountriesViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsDialogue : BottomSheetDialogFragment() {
    private val viewModel by viewModels<SettingsViewModel>()
    var onNewSettingsApplied : (oldSettings: UserSettings, newSettings: UserSettings) -> Unit = {_, _ -> }

    companion object {
        fun newInstance() = SettingsDialogue()
    }

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SettingsBottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = viewModel.getCountries()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list_country, items)
        (binding.menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        showSavedSettings(viewModel.savedUserSettings)

        binding.btnSubmit.setOnClickListener { onClickSubmit() }
        binding.vaccinationStatusButtons.addOnButtonCheckedListener { _, checkedId, _ ->
           onVaccineButtonClick(checkedId)
        }
    }

    private fun showSavedSettings(savedSettings: UserSettings) {
        showOriginCountry(savedSettings.originCountry)
        showVaccinationStatus(savedSettings.isVaccinated)
    }

    private fun showOriginCountry(originCountry: String){
        if(originCountry != UserSettings.defaultOriginCountry){
            (binding.menu.editText as AutoCompleteTextView).setText(originCountry, false)
        }
    }

    private fun showVaccinationStatus(isVaccinated: Boolean){
        if(isVaccinated){
            binding.vaccinationStatusButtons.check(R.id.vaccinated)
        } else {
            binding.vaccinationStatusButtons.check(R.id.not_vaccinated)
        }
    }

    private fun onClickSubmit(){
        val selectedCountry = binding.menu.editText?.text.toString()
        viewModel.setOriginCountry(selectedCountry)
        viewModel.saveNewSettings()

        if(viewModel.settingsChanged() && viewModel.settingsAreValid()){
            onNewSettingsApplied(
                viewModel.savedUserSettings,
                viewModel.newUserSettings)
        }

        dialog?.dismiss()
    }

    private fun onVaccineButtonClick(btnId: Int){
        when(btnId){
            R.id.vaccinated -> viewModel.setVaccinationsStatus(true)
            R.id.not_vaccinated -> viewModel.setVaccinationsStatus(false)
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}