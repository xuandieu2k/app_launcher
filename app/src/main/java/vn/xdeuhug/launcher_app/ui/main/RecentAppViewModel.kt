package vn.xdeuhug.launcher_app.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vn.xdeuhug.launcher_app.model.AppInfo

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class RecentAppViewModel(application: Application) : AndroidViewModel(application) {
    private val _recentApps = MutableLiveData<List<AppInfo>>()
    val recentApps: LiveData<List<AppInfo>> get() = _recentApps

    fun updateRecentApps() {
        viewModelScope.launch {
            val recentUsedApps = getRecentlyUsedApps()
            _recentApps.postValue(recentUsedApps)
        }
    }

    @SuppressLint("WrongConstant")
    private fun getRecentlyUsedApps(): List<AppInfo> {
        val usageStatsManager = getApplication<Application>().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - 1000 * 60 * 60 * 24,
            currentTime
        )

        if (stats.isNullOrEmpty()) {
            return emptyList()
        }

        val packageManager = getApplication<Application>().packageManager
        val installedApps = packageManager.getInstalledApplications(0)
        val installedPackageNames = installedApps.filter {
            (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }.map { it.packageName }

        return stats.sortedByDescending { it.lastTimeUsed }
            .filter { it.lastTimeUsed > 0 && installedPackageNames.contains(it.packageName) }
            .map { usageStat ->
                val appInfo = getApplication<Application>().packageManager.getApplicationInfo(usageStat.packageName, 0)
                AppInfo(
                    appName = appInfo.loadLabel(packageManager).toString(),
                    packageName = usageStat.packageName,
                    icon = appInfo.loadIcon(packageManager)
                )
            }
    }
}