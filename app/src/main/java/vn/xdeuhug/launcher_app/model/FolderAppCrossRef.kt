package vn.xdeuhug.launcher_app.model

import androidx.room.Entity
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

@Entity(
    primaryKeys = ["folderId", "packageName"],
    tableName = "folder_app_cross_ref"
)
data class FolderAppCrossRef(
    val folderId: Long,
    val packageName: String
)