package fr.isep.zili62724.crypto

data class TimeSeriesResponse(
    val metaData: MetaData,
    val timeSeries: Map<String, DayData>
)

data class MetaData(
    val information: String,
    val symbol: String,
    val lastRefreshed: String,
    val outputSize: String,
    val timeZone: String
)

data class DayData(
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val adjustedClose: String,
    val volume: String,
    val dividendAmount: String,
    val splitCoefficient: String
)
