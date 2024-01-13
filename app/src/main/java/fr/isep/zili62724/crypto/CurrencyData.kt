package fr.isep.zili62724.crypto

data class CurrencyData(
    val name: String,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double
)
