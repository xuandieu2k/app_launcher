package vn.xdeuhug.launcher_app.ui.main

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vn.xdeuhug.launcher_app.databinding.FragmentAllBinding
import vn.xdeuhug.launcher_app.ui.adapter.AppAdapter
import android.provider.Settings
import androidx.core.view.isVisible
import vn.xdeuhug.launcher_app.utils.AppUtils

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class RecentAppsFragment : Fragment() {
    private lateinit var binding: FragmentAllBinding
    private lateinit var viewModel: RecentAppViewModel
    private lateinit var adapter: AppAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[RecentAppViewModel::class.java]

        adapter = AppAdapter{
            AppUtils.openApp(requireContext(),it)
        }
        binding.rvApp.adapter = adapter
        binding.rvApp.layoutManager = LinearLayoutManager(context)

        requestUsageStatsPermission()

        viewModel.recentApps.observe(viewLifecycleOwner) { apps ->
            apps?.let {
                adapter.setApps(it)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateRecentApps()
    }

    private fun requestUsageStatsPermission() {
        if (!hasUsageStatsPermission()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val usageStatsManager = requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            System.currentTimeMillis() - 1000 * 60 * 60 * 24, // Trong vòng 24 giờ
            System.currentTimeMillis()
        )
        return stats.isNotEmpty()
    }
}
