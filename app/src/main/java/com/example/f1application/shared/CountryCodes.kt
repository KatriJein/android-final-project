package com.example.f1application.shared

object CountryCodes {
    private val countryMap = mapOf(
        "Australia" to "AU",
        "Great Britain" to "GB",
        "Netherlands" to "NL",
        "Monaco" to "MC",
        "Italy" to "IT",
        "Germany" to "DE",
        "Thailand" to "TH",
        "France" to "FR",
        "Spain" to "ES",
        "Canada" to "CA",
        "United States" to "US",
        "Argentina" to "AR",
        "New Zealand" to "NZ",
        "Brazil" to "BR",
        "Japan" to "JP",
        "Switzerland" to "CH",
        "Austria" to "AT"
    )

    fun getCode(countryName: String): String? = countryMap[countryName]

    fun getFlagUrl(countryName: String?, style: String = "flat", size: Int = 64): String? {
        val code = countryName?.let { getCode(it) } ?: return null
        return "https://flagsapi.com/$code/$style/$size.png"
    }
}