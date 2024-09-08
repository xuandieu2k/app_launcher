package vn.xdeuhug.launcher_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
@Entity(tableName = "recent_apps")
data class RecentApp(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val lastUsedTime: Long
)
