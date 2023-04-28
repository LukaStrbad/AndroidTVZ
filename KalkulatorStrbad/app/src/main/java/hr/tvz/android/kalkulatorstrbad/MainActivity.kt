package hr.tvz.android.kalkulatorstrbad

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    private val exchangeRates = mutableListOf<ExchangeRate>()
    private val currencies = mutableListOf("EUR")
    private var currencyFrom = "EUR"
    private var currencyTo = "EUR"
    private var appTheme = R.style.Theme_KalkulatorStrbad
    private lateinit var themes: List<Pair<String, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(THEME_KEY, R.style.Theme_KalkulatorStrbad)?.let {
            appTheme = it
        }
        setTheme(appTheme)
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
                // Clear the error if the user enters some value
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

        val themeArray = resources.getStringArray(R.array.app_themes)
        themes = themeArray.mapIndexed { index, s ->
            // Map a theme to the corresponding index
            val themeId = when (index) {
                0 -> R.style.Theme_KalkulatorStrbad
                1 -> R.style.Theme_KalkulatorStrbadBlue
                2 -> R.style.Theme_KalkulatorStrbadGreen
                else -> throw IndexOutOfBoundsException("Theme index was out of bounds")
            }

            Pair(s, themeId)
        }

        (binding.themeSelector.editText as? MaterialAutoCompleteTextView)?.apply {
            setSimpleItems(themes.map { it.first }.toTypedArray())
            // Find string format of the selected string
            setText(themes.first { it.second == appTheme }.first, false)

            setOnItemClickListener { _, _, _, _ -> changeTheme(this) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save app state to a Bundle
        outState.putString(AMOUNT_KEY, binding.currencyAmountTextInput.text?.toString())
        outState.putString(
            CURRENCY_FROM_KEY,
            binding.currencyFromSpinner.editText?.text?.toString()
        )
        outState.putString(CURRENCY_TO_KEY, binding.currencyToSpinner.editText?.text?.toString())
        outState.putString(CONVERSION_TEXT_KEY, binding.conversionTextView.text?.toString())
        outState.putInt(THEME_KEY, appTheme)
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

        // Fetch exchange rates
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

        // Convert to EUR if currency is not EUR
        if (currencyFrom != "EUR") {
            val exchangeRate = exchangeRates.find { it.valuta == currencyFrom } ?: return

            converted /= exchangeRate.srednjiTecaj.replace(',', '.').toDouble()
        }

        // Convert to requested currency if currency is not EUR
        if (currencyTo != "EUR") {
            val exchangeRate = exchangeRates.find { it.valuta == currencyTo } ?: return

            converted *= exchangeRate.srednjiTecaj.replace(',', '.').toDouble()
        }

        // Display value
        binding.conversionTextView.text =
            getString(R.string.conversion_display, amount, currencyFrom, converted, currencyTo)
    }

    private fun changeTheme(dropDown: MaterialAutoCompleteTextView) {
        appTheme = binding.themeSelector.editText?.text.toString().let { selectedTheme ->
            // Find theme id with selected text from the dropdown
            themes.first { it.first == selectedTheme }.second
        }
        // Both method calls are necessary to dismissDropDown
        dropDown.dismissDropDown()
        dropDown.clearFocus()
        // Recreate activity to apply selected theme
        recreate()
    }

    companion object {
        private const val TECAJ_URL = "https://api.hnb.hr/tecajn-eur/v3"
        private const val AMOUNT_KEY = "amount"
        private const val CURRENCY_FROM_KEY = "currencyFrom"
        private const val CURRENCY_TO_KEY = "currencyTo"
        private const val CONVERSION_TEXT_KEY = "conversionText"
        private const val THEME_KEY = "appTheme"
    }
}