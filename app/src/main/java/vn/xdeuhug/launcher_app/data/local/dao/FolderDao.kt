package vn.xdeuhug.launcher_app.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vn.xdeuhug.launcher_app.model.FolderEntity

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
@Dao
interface FolderDao {
    @Insert
    suspend fun insertFolder(folder: FolderEntity)

    @Query("SELECT * FROM folders")
    fun getAllFolders(): LiveData<List<FolderEntity>>

    @Query("SELECT MAX(id) FROM folders")
    suspend fun getLastInsertedFolderId(): Long

    @Query("DELETE FROM folders WHERE id = :folderId")
    suspend fun deleteFolder(folderId: Long)
}
