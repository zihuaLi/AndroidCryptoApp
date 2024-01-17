package fr.isep.zili62724.crypto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CurrencyAlert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val currencyName: String,
    val targetValue: Double,
    val expiryTime: Long
)
