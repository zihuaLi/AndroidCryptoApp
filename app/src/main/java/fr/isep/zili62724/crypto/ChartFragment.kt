package fr.isep.zili62724.crypto
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.utils.ColorTemplate
import fr.isep.zili62724.crypto.databinding.FragmentChartBinding
import org.json.JSONObject

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var chartAdapter: ChartAdapter
    private var currencyList = listOf<CurrencyData>()
    private var filteredList = listOf<CurrencyData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        fetchData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.search2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterData(s.toString())
            }
        })
    }

    private fun filterData(searchQuery: String) {
        filteredList = if (searchQuery.isEmpty()) {
            currencyList
        } else {
            currencyList.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
        chartAdapter.updateData(filteredList)
    }

    private fun fetchData() {
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val queue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener { response ->
                val currencyList = parseData(response)
                setupRecyclerView(currencyList)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"] = "3aefaa5b-0d4a-458b-b362-a4b2e1fa3d4b"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    private fun parseData(response: JSONObject): List<CurrencyData> {
        val currencyList = ArrayList<CurrencyData>()
        val dataArray = response.getJSONArray("data")
        for (i in 0 until dataArray.length()) {
            val item = dataArray.getJSONObject(i)
            val name = item.getString("name")
            val quote = item.getJSONObject("quote").getJSONObject("USD")
            val percentChange1h = quote.getDouble("percent_change_1h")
            val percentChange24h = quote.getDouble("percent_change_24h")
            val percentChange7d = quote.getDouble("percent_change_7d")
            currencyList.add(CurrencyData(name, percentChange1h, percentChange24h, percentChange7d))
        }
        return currencyList
    }

    private fun setupRecyclerView(currencyList: List<CurrencyData>) {
        chartAdapter = ChartAdapter(currencyList)
        binding.RecyclerView2.layoutManager = LinearLayoutManager(context)
        binding.RecyclerView2.adapter = chartAdapter
        this.currencyList = currencyList
        this.filteredList = currencyList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
