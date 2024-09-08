package vn.xdeuhug.launcher_app.ui.main

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vn.xdeuhug.launcher_app.data.local.AppDatabase
import vn.xdeuhug.launcher_app.model.AppInfo
import vn.xdeuhug.launcher_app.model.FolderAppCrossRef
import vn.xdeuhug.launcher_app.model.FolderEntity

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

class ManageFoldersViewModel(application: Application) : AndroidViewModel(application) {
    private val folderDao = AppDatabase.getDatabase(application).folderDao()
    private val folderAppCrossRefDao = AppDatabase.getDatabase(application).folderAppCrossRefDao()

    val folders: LiveData<List<FolderEntity>> = folderDao.getAllFolders()

    fun createFolder(folder: FolderEntity) {
        viewModelScope.launch {
            folderDao.insertFolder(folder)
        }
    }

    fun addAppsToFolder(folderId: Long, packageNames: List<String>) {
        viewModelScope.launch {
            folderAppCrossRefDao.clearAppsForFolder(folderId)
            packageNames.forEach { packageName ->
                val crossRef = FolderAppCrossRef(folderId, packageName)
                folderAppCrossRefDao.insertFolderAppCrossRef(crossRef)
            }
        }
    }

    suspend fun getAppsForFolder(folderId: Long): List<String> {
        return folderAppCrossRefDao.getAppsForFolder(folderId)
    }

    suspend fun getLastInsertedFolderId(): Long {
        return folderDao.getLastInsertedFolderId()
    }

    suspend fun clearAppsForFolder(folderId: Long) {
        folderAppCrossRefDao.clearAppsForFolder(folderId)
    }

    suspend fun deleteFolder(folderId: Long) {
        folderDao.deleteFolder(folderId)
    }

    suspend fun getAppsByPackageNames(packageNames: List<String>): List<AppInfo> {
        return packageNames.mapNotNull { packageName ->
            try {
                val packageManager = getApplication<Application>().packageManager
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                AppInfo(
                    appName = packageManager.getApplicationLabel(appInfo).toString(),
                    packageName = appInfo.packageName,
                    icon = packageManager.getApplicationIcon(appInfo)
                )
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }
}
