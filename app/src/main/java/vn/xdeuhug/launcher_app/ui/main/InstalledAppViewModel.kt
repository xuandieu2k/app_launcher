package vn.xdeuhug.launcher_app.ui.main

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import vn.xdeuhug.launcher_app.model.AppInfo

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class InstalledAppViewModel(application: Application) : AndroidViewModel(application) {
    private val packageManager = application.packageManager
    val installedApps = MutableLiveData<List<AppInfo>>()

    init {
        loadUserInstalledApps()
    }

    private fun loadUserInstalledApps() {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val userInstalledApps = apps.filter { app ->
            packageManager.getLaunchIntentForPackage(app.packageName) != null && (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }.map { app ->
            AppInfo(
                appName = app.loadLabel(packageManager).toString(),
                packageName = app.packageName,
                icon = app.loadIcon(packageManager)
            )
        }

        installedApps.postValue(userInstalledApps)
    }

}
