package fr.isep.zili62724.crypto

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isep.zili62724.crypto.databinding.FragmentAddAlertBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddAlertFragment : Fragment() {
    private lateinit var binding: FragmentAddAlertBinding
    private lateinit var database: AppDatabase
    private var currencyNames: List<String> = listOf() // 从 API 获取的货币名称列表

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddAlertBinding.inflate(inflater, container, false)
        database = AppDatabase.getDatabase(requireContext())

        // 假设 fetchCurrencyNames() 从 API 获取货币名称并更新 currencyNames 列表
        fetchCurrencyNames()

        binding.buttonAddAlert.setOnClickListener {
            val selectedCurrencyName = binding.spinnerCurrencyName.selectedItem.toString()
            val targetValue = binding.editTextTargetValue.text.toString().toDoubleOrNull() ?: 0.0
            val expiryTime = System.currentTimeMillis() // 示例中直接使用当前时间

            val newAlert = CurrencyAlert(currencyName = selectedCurrencyName, targetValue = targetValue, expiryTime = expiryTime)
            insertNewAlert(newAlert)

            // back to NotificationFragment
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun fetchCurrencyNames() {
        // API 请求逻辑，更新 currencyNames 列表
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val queue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val names = parseCurrencyNames(response)
                    currencyNames = names
                    setupSpinner()
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
    private fun parseCurrencyNames(response: JSONObject): List<String> {
        val jsonArray = response.getJSONArray("data")
        val names = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            val currency = jsonArray.getJSONObject(i)
            val name = currency.getString("name")
            names.add(name)
        }
        return names
    }
    private fun setupSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrencyName.adapter = adapter
    }

    private fun insertNewAlert(alert: CurrencyAlert) {
        CoroutineScope(Dispatchers.IO).launch {
            database.currencyAlertDao().insertAlert(alert)
        }
    }
}


