package vn.xdeuhug.launcher_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.xdeuhug.launcher_app.ui.main.InstalledAppsFragment
import vn.xdeuhug.launcher_app.ui.main.ManageFoldersFragment
import vn.xdeuhug.launcher_app.ui.main.RecentAppsFragment

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InstalledAppsFragment()
            1 -> RecentAppsFragment()
            2 -> ManageFoldersFragment()
            else -> InstalledAppsFragment()
        }
    }
}
