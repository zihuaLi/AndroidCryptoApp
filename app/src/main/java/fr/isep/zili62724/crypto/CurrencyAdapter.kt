package fr.isep.zili62724.crypto


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isep.zili62724.crypto.databinding.RvItemBinding

class CurrencyAdapter(
    private val currencyList: MutableList<CurrencyData>,
    private val itemClickListener: OnCurrencyItemClickListener

) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    interface OnCurrencyItemClickListener {
        fun onCurrencyItemClick(currencyData: CurrencyData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyData = currencyList[position]
        holder.bind(currencyData)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class CurrencyViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val subscribeButton = binding.subscribe

        fun bind(currencyData: CurrencyData) {
            binding.apply {
                name.text = currencyData.name
                //root.setOnClickListener

                subscribeButton.setOnClickListener {

                    addCurrency(currencyData)
                    itemClickListener.onCurrencyItemClick(currencyData)

                }


            }
        }
    }

    // 新增資料
    fun addCurrency(currencyData: CurrencyData) {
        currencyList.add(currencyData)
        notifyItemInserted(currencyList.size - 1)
    }

    // 刪除資料
    fun removeCurrency(position: Int) {
        if (position in 0 until currencyList.size) {
            currencyList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // 修改資料
    fun updateCurrency(position: Int, newCurrencyData: CurrencyData) {
        if (position in 0 until currencyList.size) {
            currencyList[position] = newCurrencyData
            notifyItemChanged(position)
        }
    }
}
