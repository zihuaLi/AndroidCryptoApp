package fr.isep.zili62724.crypto
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isep.zili62724.crypto.databinding.FragmentNotificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var database: AppDatabase
    private lateinit var alertAdapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        database = AppDatabase.getDatabase(requireContext())
        setupRecyclerView()
        loadAlerts()

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_notificationFragment_to_addAlertFragment)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        // 正确初始化适配器
        alertAdapter = AlertAdapter { alert ->
            deleteAlert(alert)
        }

        // 设置 RecyclerView 的布局管理器和适配器
        binding.recyclerViewAlerts.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlerts.adapter = alertAdapter
    }

    private fun deleteAlert(alert: CurrencyAlert) {
        CoroutineScope(Dispatchers.IO).launch {
            database.currencyAlertDao().deleteAlert(alert)

            // 切换回主线程来观察 LiveData
            withContext(Dispatchers.Main) {
                loadAlerts()
            }
        }
    }

    private fun loadAlerts() {
        // Assuming that the DAO returns LiveData
        database.currencyAlertDao().getAllAlerts().observe(viewLifecycleOwner, { alerts ->
            alertAdapter.setAlerts(alerts)

            })
    }


}


