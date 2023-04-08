package hr.tvz.android.kalkulatorstrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.kalkulatorstrbad.databinding.ActivityMainBinding
import hr.tvz.android.kalkulatorstrbad.model.ExchangeRate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    val tecajUrl = "https://api.hnb.hr/tecajn-eur/v3"
    val exchangeRates = mutableListOf<ExchangeRate>()
    val currencies = mutableListOf("EUR")

    var currencyFrom = "EUR"
    var currencyTo = "EUR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertButton.setOnClickListener { convert(it) }

        setCurrencySpinnerAdapter(currencies)
        binding.currencyFromSpinner.onItemSelectedListener = this
        binding.currencyToSpinner.onItemSelectedListener = this
    }

    private fun setCurrencySpinnerAdapter(currencies: Iterable<String>) = ArrayAdapter(
        this, android.R.layout.simple_spinner_item, currencies.toList()
    ).also { arrayAdapter ->
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.currencyFromSpinner.adapter = arrayAdapter
        binding.currencyToSpinner.adapter = arrayAdapter
    }

    override fun onStart() {
        super.onStart()
        val client = HttpClient()
        lifecycle.coroutineScope.launch {
            val response = client.request(tecajUrl)
            exchangeRates.addAll(Json.decodeFromString<List<ExchangeRate>>(response.body()))
            currencies.addAll(exchangeRates.map { it.valuta })
            setCurrencySpinnerAdapter(currencies)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        currencyFrom = binding.currencyFromSpinner.selectedItem.toString()
        currencyTo = binding.currencyToSpinner.selectedItem.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    private fun convert(view: View) {
        val amount = binding.currencyAmountTextInput.text.toString().toDoubleOrNull()

        if (amount == null) {
            Snackbar.make(view, getString(R.string.enter_amount), Snackbar.LENGTH_LONG).show()
            return
        }

        binding.conversionTextView.text =
            getString(R.string.conversion_display, 1.0, currencyFrom, 0.98, currencyTo)
    }
}