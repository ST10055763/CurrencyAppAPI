package com.example.currencyappapi

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("base_currency_code")
    val baseCurrencyCode: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("rates")
    val rates: Map<String, Rate>
)

data class Rate(
    @SerializedName("currency_name")
    val currencyName: String,

    @SerializedName("rate")
    val rate: String,

    @SerializedName("rate_for_amount")
    val rateForAmount: String
)
