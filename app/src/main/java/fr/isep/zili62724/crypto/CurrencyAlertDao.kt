package fr.isep.zili62724.crypto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyAlertDao {
    @Query("SELECT * FROM CurrencyAlert")
    fun getAllAlerts(): LiveData<List<CurrencyAlert>>

    @Insert
    suspend fun insertAlert(alert: CurrencyAlert):Long

    @Delete
    suspend fun deleteAlert(alert: CurrencyAlert)
}

