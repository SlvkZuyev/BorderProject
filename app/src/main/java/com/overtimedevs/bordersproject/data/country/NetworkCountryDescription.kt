package com.overtimedevs.bordersproject.data.country

import com.google.gson.annotations.SerializedName
import com.overtimedevs.bordersproject.data.country.*


data class NetworkCountryDescription (
	@SerializedName("countryId") val countryId : Int,
	@SerializedName("lastModified") val lastModified : Int,
	@SerializedName("borderStatus") val borderStatus : String,
	@SerializedName("countryName") val countryName : String,
	@SerializedName("countryGroupCode") val countryGroupCode : String,
	@SerializedName("countryGroupName") val countryGroupName : String,
	@SerializedName("arrivalTestRequired") val arrivalTestRequired : Boolean,
	@SerializedName("variantData") val variantData : VariantData,
	@SerializedName("link") val link : String,
	@SerializedName("returnTestRequired") val returnTestRequired : Boolean,
	@SerializedName("activeCases") val activeCases : Int,
	@SerializedName("countryCode") val countryCode : String,
	@SerializedName("restaurantStatus") val restaurantStatus : String,
	@SerializedName("barStatus") val barStatus : String,
	@SerializedName("maskStatus") val maskStatus : MaskStatus,
	@SerializedName("arrivalTestStatus") val arrivalTestStatus : ArrivalTestStatus,
	@SerializedName("arrivalQuarantineStatus") val arrivalQuarantineStatus : ArrivalQuarantineStatus,
	@SerializedName("returnTestStatus") val returnTestStatus : ReturnTestStatus,
	@SerializedName("returnQuarantineStatus") val returnQuarantineStatus : ReturnQuarantineStatus,
	@SerializedName("arrivalDocumentation") val arrivalDocumentation : List<String>,
	@SerializedName("returnDocumentation") val returnDocumentation : List<ReturnDocumentation>,
	@SerializedName("borderStatusData") val borderStatusData : BorderStatusData,
	@SerializedName("openForVaccinated") val openForVaccinated : Boolean,
	@SerializedName("flightUrl") val flightUrl : String,
	@SerializedName("unvaccinatedBorderStatus") val unvaccinatedBorderStatus : String,
	@SerializedName("vaccinatedArrivalTestRequired") val vaccinatedArrivalTestRequired : Boolean,
	@SerializedName("vaccinatedArrivalQuarantineRequired") val vaccinatedArrivalQuarantineRequired : Boolean,
	@SerializedName("vaccinatedReturnTestRequired") val vaccinatedReturnTestRequired : Boolean,
	@SerializedName("vaccinatedReturnQuarantineRequired") val vaccinatedReturnQuarantineRequired : Boolean,
	@SerializedName("currentWeekVaccinatedPercent") val currentWeekVaccinatedPercent : Double,
	@SerializedName("previousWeekVaccinatedPercent") val previousWeekVaccinatedPercent : Double,
	@SerializedName("returnQuarantineRequired") val returnQuarantineRequired : Boolean,
	@SerializedName("arrivalQuarantineRequired") val arrivalQuarantineRequired : Boolean,
	@SerializedName("valid") val valid : Boolean
)