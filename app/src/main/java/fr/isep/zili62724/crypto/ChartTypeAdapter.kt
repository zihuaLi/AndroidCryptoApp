package fr.isep.zili62724.crypto

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.isep.zili62724.crypto.databinding.FragmentChartBinding

class ChartTypeAdapter(
    private val chartTypeList: List<ChartTypeItem>,
    private val onChartTypeClickListener: (ChartType) -> Unit
) : RecyclerView.Adapter<ChartTypeAdapter.ChartTypeViewHolder>(), SpinnerAdapter {

    inner class ChartTypeViewHolder(private val binding: FragmentChartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chartTypeItem: ChartTypeItem) {


            // Assuming that chartTypeList is a property in your Adapter
            // Set the data source for the RecyclerView
            binding.chartTypeSpinner.adapter = ChartTypeAdapter(chartTypeList) { chartType ->
                // Handle click events if needed
            }

            binding.root.setOnClickListener {
                onChartTypeClickListener(chartTypeItem.chartType)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentChartBinding.inflate(inflater, parent, false)
        return ChartTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartTypeViewHolder, position: Int) {
        val chartTypeItem = chartTypeList[position]
        holder.bind(chartTypeItem)
    }

    override fun getItemCount(): Int = chartTypeList.size
    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}