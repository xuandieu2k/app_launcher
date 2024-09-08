package vn.xdeuhug.launcher_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vn.xdeuhug.launcher_app.data.local.dao.FolderAppCrossRefDao
import vn.xdeuhug.launcher_app.data.local.dao.FolderDao
import vn.xdeuhug.launcher_app.model.FolderAppCrossRef
import vn.xdeuhug.launcher_app.model.FolderEntity

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
@Database(entities = [FolderEntity::class, FolderAppCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun folderAppCrossRefDao(): FolderAppCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}