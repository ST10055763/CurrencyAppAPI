package com.example.currencyappapi

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var spinnerFromCurrency: Spinner? = null
    private var spinnerToCurrency: Spinner? = null
    private var editTextAmount: EditText? = null
    private var buttonConvert: Button? = null
    private var textViewResult: TextView? = null
    private var apiService: CurrencyApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        spinnerFromCurrency = findViewById(R.id.spinner_from_currency)
        spinnerToCurrency = findViewById(R.id.spinner_to_currency)
        editTextAmount = findViewById(R.id.edit_text_amount)
        buttonConvert = findViewById(R.id.button_convert)
        textViewResult = findViewById(R.id.text_view_result)

        // Set up spinners
        val currencies = arrayOf("USD", "EUR", "GBP", "INR", "JPY")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        spinnerFromCurrency?.adapter = adapter
        spinnerToCurrency?.adapter = adapter

        // Initialize API service
        apiService = RetrofitClient.getRetrofitInstance().create(CurrencyApiService::class.java)

        buttonConvert?.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val fromCurrency = spinnerFromCurrency?.selectedItem.toString()
        val toCurrency = spinnerToCurrency?.selectedItem.toString()
        val amountStr = editTextAmount?.text.toString()

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDouble()

        val call = apiService?.getExchangeRates("b4d47ce1bab1bd1c8b099882ce4af39b9d0a9f30", fromCurrency, toCurrency, amount)
        if (call != null) {
            call.enqueue(object : Callback<CurrencyResponse> {
                override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val rates = response.body()!!.rates
                        val rate = rates[toCurrency]
                        if (rate != null) {
                            val convertedAmount = amount * rate.rate.toDouble()
                            textViewResult?.text = String.format("%.2f %s", convertedAmount, toCurrency)
                        } else {
                            Toast.makeText(this@MainActivity, "Conversion rate not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to get exchange rates. Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}