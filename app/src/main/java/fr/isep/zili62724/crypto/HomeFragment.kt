package fr.isep.zili62724.crypto

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isep.zili62724.crypto.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAdapter: RvAdapter
    private lateinit var data: ArrayList<Model>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = ArrayList()
        apiData
        rvAdapter = RvAdapter(requireContext(), data)
        binding.Rv.layoutManager = LinearLayoutManager(context)
        binding.Rv.adapter = rvAdapter

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val filterdata = ArrayList<Model>()
                for (item in data) {
                    if (item.name.lowercase(Locale.getDefault()).contains(p0.toString().lowercase(Locale.getDefault()))) {
                        filterdata.add(item)
                    }
                }
                if (filterdata.isEmpty()) {
                    Toast.makeText(context, "No data available", Toast.LENGTH_LONG).show()
                } else {
                    rvAdapter.changeData(filterdata)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }

    private val apiData: Unit
        get() {
            val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
            val queue = Volley.newRequestQueue(context)
            val jsonObjectRequest: JsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
                    binding.progressBar.isVisible = false
                    try {
                        val dataArray = response.getJSONArray("data")
                        for (i in 0 until dataArray.length()) {
                            val dataObject = dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val quote = dataObject.getJSONObject("quote")
                            val USD = quote.getJSONObject("USD")
                            val price = String.format("$" + "%.2f", USD.getDouble("price"))

                            data.add(Model(name, symbol, price))
                        }
                        rvAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error 2", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(context, "Error 1", Toast.LENGTH_LONG).show()
                }) {
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["X-CMC_PRO_API_KEY"] = "3aefaa5b-0d4a-458b-b362-a4b2e1fa3d4b"
                        return headers
                    }
                }
                    queue.add(jsonObjectRequest)
                }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}