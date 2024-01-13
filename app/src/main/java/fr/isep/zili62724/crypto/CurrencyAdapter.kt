package fr.isep.zili62724.crypto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isep.zili62724.crypto.databinding.ItemCurrencyBinding

class CurrencyAdapter(private val currencyList: List<CurrencyData>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyData) {
            binding.textViewCurrencyName.text = currency.name
            binding.textViewChange1h.text = "1h: ${currency.percentChange1h}%"
            binding.textViewChange24h.text = "24h: ${currency.percentChange24h}%"
            binding.textViewChange7d.text = "7d: ${currency.percentChange7d}%"
        }
    }
}

