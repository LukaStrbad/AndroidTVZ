package hr.tvz.android.kalkulatorstrbad.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class ExchangeRate(
    @SerialName("broj_tecajnice") val brojTecajnice: String,
    @SerialName("datum_primjene") val datumPrimjene: String,
    val drzava: String,
    @SerialName("drzava_iso") val drzavaIso: String,
    @SerialName("sifra_valute") val sifraValute: String,
    val valuta: String,
    @SerialName("kupovni_tecaj") val kupovniTecaj: String,
    @SerialName("srednji_tecaj") val srednjiTecaj: String,
    @SerialName("prodajni_tecaj") val prodajniTecaj: String,
)