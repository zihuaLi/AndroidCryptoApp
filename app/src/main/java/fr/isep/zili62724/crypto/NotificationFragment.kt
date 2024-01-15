package fr.isep.zili62724.crypto


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import fr.isep.zili62724.crypto.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var chartAdapter: ChartAdapter
    private lateinit var pieChart: PieChart

    // ... (other properties and methods)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        fetchData()

        // Add PieChart to the layout
        val stockTitle = binding.stockTitle
        val notificationTitle = binding.notificationTitle

        stockTitle.text = "Stock Details" + "\n" + "\"1. Information\": \"Daily Time Series with Splits and Dividend Events\",\n" +
                "        \"2. Symbol\": \"TSCO.LON\",\n" +
                "        \"3. Last Refreshed\": \"2024-01-12\",\n" +
                "        \"4. Output Size\": \"Full size\",\n" +
                "        \"5. Time Zone\": \"US/Eastern\""


        notificationTitle.text = "Notification Details"
        pieChart = binding.pieChart
        setupPieChart()

        return binding.root
    }

    private fun fetchData() {
        val url =
            "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=TSCO.LON&outputsize=full&apikey=demo"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val timeSeriesResponse =
                    Gson().fromJson(response.toString(), TimeSeriesResponse::class.java)
                // Now you have parsed data in timeSeriesResponse, and you can use it as needed.
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun setupPieChart() {
        // Create a list of PieEntry
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(1.6f, "others"))
        entries.add(PieEntry(1.6f, "debit"))
        entries.add(PieEntry(0.4f, "financial"))
        entries.add(PieEntry(14.7f, "Investment"))

        entries.add(PieEntry(36f, "Stock"))
        entries.add(PieEntry(39.3f, "Saving"))

        // Create a dataset
        val dataSet = PieDataSet(entries, "Assets ")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.asList()

        // Create a PieData object
        val pieData = PieData(dataSet)

        // Set data to the pie chart
        pieChart.data = pieData

        // Customize the pie chart as needed
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = true
        pieChart.setDrawEntryLabels(false)
        pieChart.invalidate()
    }

    // ... (other methods)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
