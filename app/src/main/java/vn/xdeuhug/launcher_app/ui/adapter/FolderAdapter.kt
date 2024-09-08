import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vn.xdeuhug.launcher_app.databinding.ItemFolderBinding
import vn.xdeuhug.launcher_app.model.AppInfo
import vn.xdeuhug.launcher_app.model.FolderEntity
import vn.xdeuhug.launcher_app.ui.adapter.AppAdapter
import vn.xdeuhug.launcher_app.ui.adapter.LogoAppAdapter
import vn.xdeuhug.launcher_app.ui.main.ManageFoldersViewModel
import vn.xdeuhug.launcher_app.utils.AppUtils

class FolderAdapter(
    private val onFolderClicked: (Long) -> Unit,
    private val onClickLogo: (app: AppInfo) -> Unit,
    private val viewModel: ManageFoldersViewModel
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    private var folders: List<FolderEntity> = emptyList()

    inner class FolderViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val appAdapter = LogoAppAdapter {
            onClickLogo.invoke(it)
        }

        fun bind(folder: FolderEntity) {
            binding.folderName.text = folder.name
            binding.rvAppList.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvAppList.adapter = appAdapter

            viewModel.viewModelScope.launch {
                val appPackageNames = viewModel.getAppsForFolder(folder.id)
                val apps = viewModel.getAppsByPackageNames(appPackageNames)
                appAdapter.setApps(apps)
            }

            binding.root.setOnClickListener {
                onFolderClicked(folder.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int = folders.size

    fun submitList(folders: List<FolderEntity>) {
        this.folders = folders
        notifyDataSetChanged()
    }
}