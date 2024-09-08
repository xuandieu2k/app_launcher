package vn.xdeuhug.launcher_app.ui.main

import FolderAdapter
import android.app.AlertDialog
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.xdeuhug.launcher_app.databinding.DialogCreateFolderBinding
import vn.xdeuhug.launcher_app.databinding.DialogFolderDetailsBinding
import vn.xdeuhug.launcher_app.databinding.FragmentManageFoldersBinding
import vn.xdeuhug.launcher_app.model.AppInfo
import vn.xdeuhug.launcher_app.model.FolderEntity
import vn.xdeuhug.launcher_app.ui.adapter.AppAdapter
import vn.xdeuhug.launcher_app.ui.adapter.AppCheckedAdapter
import vn.xdeuhug.launcher_app.utils.AppUtils

class ManageFoldersFragment : Fragment() {
    private lateinit var binding: FragmentManageFoldersBinding
    private lateinit var viewModel: ManageFoldersViewModel
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var appAdapter: AppCheckedAdapter
    private lateinit var systemApps: List<AppInfo>
    private var selectedApps: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageFoldersBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ManageFoldersViewModel::class.java]

        folderAdapter = FolderAdapter(
            onFolderClicked = { folderId -> showFolderDetails(folderId) },
            onClickLogo = { AppUtils.openApp(requireContext(), it) },
            viewModel = viewModel
        )
        binding.rvFolders.adapter = folderAdapter
        binding.rvFolders.layoutManager = LinearLayoutManager(context)

        viewModel.folders.observe(viewLifecycleOwner) { folders ->
            folderAdapter.submitList(folders)
        }

        binding.btnAddFolder.setOnClickListener {
            showCreateFolderDialog()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apps = getSystemApps()
                withContext(Dispatchers.Main) {
                    systemApps = apps
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return binding.root
    }

    private fun showCreateFolderDialog() {
        val dialogBinding = DialogCreateFolderBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Create Folder")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val folderName = dialogBinding.edtFolderName.text.toString()
                if (folderName.isNotBlank()) {
                    val folder = FolderEntity(name = folderName)
                    viewModel.createFolder(folder)
                    viewModel.viewModelScope.launch {
                        val folderId = viewModel.getLastInsertedFolderId()
                        viewModel.addAppsToFolder(folderId, selectedApps)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialogBinding.rvAppSelector.layoutManager = LinearLayoutManager(context)
        appAdapter = AppCheckedAdapter()
        dialogBinding.rvAppSelector.adapter = appAdapter
        appAdapter.setApps(systemApps)

        appAdapter.onAppSelected = { packageName ->
            selectedApps = appAdapter.getSelectedApps()
        }

        dialog.show()
    }

    private fun showFolderDetails(folderId: Long) {
        val folderDetailDialogBinding =
            DialogFolderDetailsBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Folder Details")
            .setView(folderDetailDialogBinding.root)
            .setPositiveButton("Close", null)
            .setNeutralButton("Delete") { _, _ ->
                deleteFolder(folderId)
            }
            .create()

        folderDetailDialogBinding.rvAppList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val appAdapter = AppAdapter {
            //
        }
        folderDetailDialogBinding.rvAppList.adapter = appAdapter
        viewModel.viewModelScope.launch {
            val appPackageNames = viewModel.getAppsForFolder(folderId)
            val apps = getAppsByPackageNames(appPackageNames)
            appAdapter.setApps(apps)
        }
        dialog.show()
    }

    private fun deleteFolder(folderId: Long) {
        viewModel.viewModelScope.launch {
            viewModel.clearAppsForFolder(folderId)
            viewModel.deleteFolder(folderId)
        }
    }

    private fun getSystemApps(): List<AppInfo> {
        val apps =
            requireActivity().packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val userInstalledApps = apps.filter { app ->
            requireActivity().packageManager.getLaunchIntentForPackage(app.packageName) != null && (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }.map { app ->
            AppInfo(
                appName = app.loadLabel(requireActivity().packageManager).toString(),
                packageName = app.packageName,
                icon = app.loadIcon(requireActivity().packageManager)
            )
        }
        return userInstalledApps
    }

    private suspend fun getAppsByPackageNames(packageNames: List<String>): List<AppInfo> {
        return packageNames.mapNotNull { packageName ->
            val pm = requireActivity().packageManager
            try {
                val app = pm.getApplicationInfo(packageName, 0)
                AppInfo(
                    appName = app.loadLabel(pm).toString(),
                    packageName = app.packageName,
                    icon = app.loadIcon(pm)
                )
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }
}