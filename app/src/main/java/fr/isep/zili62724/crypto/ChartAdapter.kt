package fr.isep.zili62724.crypto
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import fr.isep.zili62724.crypto.databinding.ItemChartBinding

class ChartAdapter(private val currencyList: List<CurrencyData>) : RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ItemChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    class ChartViewHolder(private val binding: ItemChartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyData) {
            binding.textViewCurrencyName.text = currency.name

            val entries = listOf(
                BarEntry(0f, currency.percentChange1h.toFloat()),
                BarEntry(1f, currency.percentChange24h.toFloat()),
                BarEntry(2f, currency.percentChange7d.toFloat())
            )

            val dataSet = BarDataSet(entries, "Price Changes")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

            val barData = BarData(dataSet)
            binding.barChart.data = barData
            binding.barChart.description.isEnabled = false
            binding.barChart.setFitBars(true)
            binding.barChart.invalidate() // refresh the chart
        }
    }
}


