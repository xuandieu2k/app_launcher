package vn.xdeuhug.launcher_app.ui.main

import android.app.usage.UsageStatsManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import vn.xdeuhug.launcher_app.databinding.ActivityMainBinding
import vn.xdeuhug.launcher_app.ui.adapter.ViewPagerAdapter
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator( binding.tabLayout,  binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Installed App"
                1 -> "Recent App"
                2 ->"Management App"
                else -> ""
            }
        }.attach()
    }
}