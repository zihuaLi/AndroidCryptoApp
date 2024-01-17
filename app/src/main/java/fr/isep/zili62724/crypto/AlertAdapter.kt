package fr.isep.zili62724.crypto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isep.zili62724.crypto.databinding.ItemAlertBinding
import java.util.Date
class AlertAdapter(private val deleteAlert: (CurrencyAlert) -> Unit) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {
    private var alerts: List<CurrencyAlert> = listOf()

    class AlertViewHolder(val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alert: CurrencyAlert,deleteAlert: (CurrencyAlert) -> Unit) {
            binding.currencyNameTextView.text = alert.currencyName
            binding.targetValueTextView.text = "Target: ${alert.targetValue}"
            binding.expiryTimeTextView.text = "Expiry: ${Date(alert.expiryTime).toString()}"
            // TODO: Handle delete button click
            binding.deleteButton.setOnClickListener { deleteAlert(alert) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alerts[position],deleteAlert)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    fun setAlerts(newAlerts: List<CurrencyAlert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }
}

