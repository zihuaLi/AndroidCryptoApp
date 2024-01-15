package fr.isep.zili62724.crypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import fr.isep.zili62724.crypto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        // 不再需要 replaceFragment 方法
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding=ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        replaceFragment(HomeFragment())
//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.home -> replaceFragment(HomeFragment())
//                R.id.chart -> replaceFragment(ChartFragment())
//                R.id.notification -> replaceFragment(NotificationFragment())
//                else -> {
//
//                }
//            }
//            true
//        }
//    }
//   private fun replaceFragment(fragment: Fragment){
//       val fragmentManager=supportFragmentManager
//       val fragmentTransaction=fragmentManager.beginTransaction()
//       fragmentTransaction.replace(R.id.frame_layout,fragment)
//       fragmentTransaction.commit()
//   }
}