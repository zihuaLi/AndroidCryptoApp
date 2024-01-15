package fr.isep.zili62724.crypto

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.isep.zili62724.crypto.databinding.FragmentHomeBinding
import fr.isep.zili62724.crypto.databinding.FragmentNotificationBinding
import java.util.*
import java.util.prefs.Preferences

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragmentbinding: FragmentNotificationBinding
    private val notiviewModel: NotificationsViewModel by viewModels()

    private lateinit var currencyAdapter: CurrencyAdapter

    private var currencyList: MutableList<CurrencyData> = mutableListOf()

    private lateinit var datastore: DataStore<Preferences>
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data: ArrayList<Model>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        currencyAdapter = CurrencyAdapter(currencyList, object : CurrencyAdapter.OnCurrencyItemClickListener {
            override fun onCurrencyItemClick(currencyData: CurrencyData) {
                // 處理點擊事件
            }
        })
        notiviewModel.showNotificationEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                // Call the function to show notification
                showNotification(createNotification())

//                showStockNotification("bitcon","sucees", "33")
            }
        })
        return binding.root
    }

    private fun showNotification(notification: NotificationCompat.Builder) {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val notifId = generateNotificationId("name", "bitcon", "33")
        notificationManager.notify(notifId, notification.build())
    }


    private fun createNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(requireContext(), "channelID")
            .setContentTitle("Jérôme BATON")
            .setContentText("Wish You All Merry Christmas")
            .setSmallIcon(R.drawable.ic_baseline_info_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "StockNotifications"
            val descriptionText = "Channel for stock price notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("stock_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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

    private fun generateNotificationId(name: String, symbol: String, price: String): Int {
        // You can generate a unique ID based on the content of the notification.
        // For simplicity, you can concatenate the stock symbol, name, and price and then hash it.
        val combinedInfo = "$name$symbol$price"
        return combinedInfo.hashCode()
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