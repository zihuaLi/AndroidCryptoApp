package fr.isep.zili62724.crypto
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class ChartFragment : Fragment() {
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        barChart = view.findViewById(R.id.barChart)
        fetchData()
        return view
    }

    private fun fetchData() {
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val queue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener { response ->
                val currencyDataList = parseData(response)
                setupBarChart(currencyDataList)
            },
            Response.ErrorListener { error ->
                // Handle Error
                error.printStackTrace()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"] = "YOUR_API_KEY"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    private fun parseData(response: JSONObject): List<CurrencyData> {
        val currencyList = ArrayList<CurrencyData>()
        val dataArray = response.getJSONArray("data")
        for (i in 0 until dataArray.length()) {
            val currencyObject = dataArray.getJSONObject(i)
            val name = currencyObject.getString("name")
            val quote = currencyObject.getJSONObject("quote").getJSONObject("USD")
            val percentChange1h = quote.getDouble("percent_change_1h")
            val percentChange24h = quote.getDouble("percent_change_24h")
            val percentChange7d = quote.getDouble("percent_change_7d")

            currencyList.add(CurrencyData(name, percentChange1h, percentChange24h, percentChange7d))
        }
        return currencyList
    }

    private fun setupBarChart(currencyDataList: List<CurrencyData>) {
        val entries = currencyDataList.mapIndexed { index, currencyData ->
            BarEntry(
                index.toFloat(), floatArrayOf(
                    currencyData.percentChange1h.toFloat(),
                    currencyData.percentChange24h.toFloat(),
                    currencyData.percentChange7d.toFloat()
                )
            )
        }
        val barDataSet = BarDataSet(entries, "Currency Data")
        barDataSet.colors = listOf(
            getResources().getColor(R.color.color1),
            getResources().getColor(R.color.color2),
            getResources().getColor(R.color.color3)
        )
        barDataSet.stackLabels = arrayOf("1h Change", "24h Change", "7d Change")

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(currencyDataList.map { it.name })
        barChart.setFitBars(true)
        barChart.invalidate() // Refresh the chart
    }
}

