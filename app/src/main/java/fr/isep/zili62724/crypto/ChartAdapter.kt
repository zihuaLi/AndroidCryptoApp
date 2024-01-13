package fr.isep.zili62724.crypto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import fr.isep.zili62724.crypto.databinding.ItemChartBinding

class ChartAdapter(private var currencyList: List<CurrencyData>) : RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {
    fun updateData(newList: List<CurrencyData>) {
        currencyList = newList
        notifyDataSetChanged()
    }
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
    fun setAnimation(view: View){
        val anim= AlphaAnimation(0.0f,1.0f)
        anim.duration=1000
        view.startAnimation(anim)
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
            // 配置X轴
            val xAxis = binding.barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("1h", "24h", "7d"))
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            // 配置Y轴
            binding.barChart.axisLeft.isEnabled = true
            binding.barChart.axisRight.isEnabled = false

            binding.barChart.description.isEnabled = false
            binding.barChart.setFitBars(true)
            binding.barChart.invalidate() // refresh the chart
        }
    }
}


