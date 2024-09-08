package vn.xdeuhug.launcher_app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vn.xdeuhug.launcher_app.databinding.ActivityMainBinding
import vn.xdeuhug.launcher_app.databinding.FragmentAllBinding
import vn.xdeuhug.launcher_app.ui.adapter.AppAdapter
import vn.xdeuhug.launcher_app.utils.AppUtils

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class InstalledAppsFragment : Fragment() {
    private lateinit var binding: FragmentAllBinding
    private lateinit var viewModel: InstalledAppViewModel
    private lateinit var adapter: AppAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvApp.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this)[InstalledAppViewModel::class.java]
        viewModel.installedApps.observe(viewLifecycleOwner) { apps ->
            adapter = AppAdapter{
                AppUtils.openApp(requireContext(),it)
            }
            adapter.setApps(apps)
            binding.rvApp.adapter = adapter
        }
    }
}
