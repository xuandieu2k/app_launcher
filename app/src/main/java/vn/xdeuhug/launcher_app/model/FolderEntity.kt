package vn.xdeuhug.launcher_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)