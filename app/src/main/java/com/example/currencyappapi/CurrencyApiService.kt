package com.example.currencyappapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("currency/convert")
    fun getExchangeRates(
        @Query("api_key") apiKey: String,
        @Query("from") baseCurrency: String,
        @Query("to") targetCurrency: String,
        @Query("amount") amount: Double,
        @Query("format") format: String = "json"
    ): Call<CurrencyResponse>
}
