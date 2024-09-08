package vn.xdeuhug.launcher_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vn.xdeuhug.launcher_app.model.FolderAppCrossRef

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

@Dao
interface FolderAppCrossRefDao {
    @Insert
    suspend fun insertFolderAppCrossRef(crossRef: FolderAppCrossRef)

    @Query("SELECT packageName FROM folder_app_cross_ref WHERE folderId = :folderId")
    suspend fun getAppsForFolder(folderId: Long): List<String>

    @Query("DELETE FROM folder_app_cross_ref WHERE folderId = :folderId")
    suspend fun clearAppsForFolder(folderId: Long)
}
