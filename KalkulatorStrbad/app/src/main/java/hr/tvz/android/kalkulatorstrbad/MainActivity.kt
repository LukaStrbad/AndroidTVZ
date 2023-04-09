package hr.tvz.android.kalkulatorstrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import hr.tvz.android.kalkulatorstrbad.databinding.ActivityMainBinding
import hr.tvz.android.kalkulatorstrbad.model.ExchangeRate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val exchangeRates = mutableListOf<ExchangeRate>()
    val currencies = mutableListOf("EUR")

    var currencyFrom = "EUR"
    var currencyTo = "EUR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertButton.setOnClickListener { convert(it) }

        // Restore state
        savedInstanceState?.getString(AMOUNT_KEY)?.let {
            binding.currencyAmountTextInput.setText(it)
        }
        savedInstanceState?.getString(CURRENCY_FROM_KEY)?.let { currencyFrom = it }
        savedInstanceState?.getString(CURRENCY_TO_KEY)?.let { currencyTo = it }
        savedInstanceState?.getString(CONVERSION_TEXT_KEY)?.let {
            binding.conversionTextView.text = it
        }

        setCurrencySpinnerAdapter(currencies, currencyFrom, currencyTo)

        binding.currencyAmountTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.currencyAmountTextInput.error = null
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        (binding.currencyFromSpinner.editText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener { _, _, _, _ ->
                currencyFrom = binding.currencyFromSpinner.editText?.text.toString()
            }
        (binding.currencyToSpinner.editText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener { _, _, _, _ ->
                currencyTo = binding.currencyToSpinner.editText?.text.toString()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(AMOUNT_KEY, binding.currencyAmountTextInput.text?.toString())
        outState.putString(
            CURRENCY_FROM_KEY,
            binding.currencyFromSpinner.editText?.text?.toString()
        )
        outState.putString(CURRENCY_TO_KEY, binding.currencyToSpinner.editText?.text?.toString())
        outState.putString(CONVERSION_TEXT_KEY, binding.conversionTextView.text?.toString())
    }

    private fun setCurrencySpinnerAdapter(
        currencies: Iterable<String>,
        currencyFrom: String,
        currencyTo: String
    ) {
        (binding.currencyFromSpinner.editText as? MaterialAutoCompleteTextView)?.apply {
            setSimpleItems(currencies.toList().toTypedArray())
            setText(currencyFrom, false)
        }

        (binding.currencyToSpinner.editText as? MaterialAutoCompleteTextView)?.apply {
            setSimpleItems(currencies.toList().toTypedArray())
            setText(currencyTo, false)
        }
    }

    override fun onStart() {
        super.onStart()
        val client = HttpClient()
        lifecycle.coroutineScope.launch {
            val response = client.request(TECAJ_URL)
            exchangeRates.addAll(Json.decodeFromString<List<ExchangeRate>>(response.body()))
            currencies.addAll(exchangeRates.map { it.valuta })
            setCurrencySpinnerAdapter(currencies, currencyFrom, currencyTo)
        }
    }

    private fun convert(view: View) {
        val amount = binding.currencyAmountTextInput.text.toString().toDoubleOrNull()

        if (amount == null) {
            Snackbar.make(view, getString(R.string.enter_amount), Snackbar.LENGTH_LONG).show()
            binding.currencyAmountTextInput.error = getString(R.string.enter_amount)
            return
        }

        var converted = amount

        if (currencyFrom != "EUR") {
            val exchangeRate = exchangeRates.find { it.valuta == currencyFrom } ?: return

            converted /= exchangeRate.srednjiTecaj.replace(',', '.').toDouble()
        }

        if (currencyTo != "EUR") {
            val exchangeRate = exchangeRates.find { it.valuta == currencyTo } ?: return

            converted *= exchangeRate.srednjiTecaj.replace(',', '.').toDouble()
        }

        binding.conversionTextView.text =
            getString(R.string.conversion_display, amount, currencyFrom, converted, currencyTo)
    }

    companion object {
        private const val TECAJ_URL = "https://api.hnb.hr/tecajn-eur/v3"
        private const val AMOUNT_KEY = "amount"
        private const val CURRENCY_FROM_KEY = "currencyFrom"
        private const val CURRENCY_TO_KEY = "currencyTo"
        private const val CONVERSION_TEXT_KEY = "conversionText"
    }
}