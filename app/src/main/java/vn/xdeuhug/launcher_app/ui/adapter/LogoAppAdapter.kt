package vn.xdeuhug.launcher_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.xdeuhug.launcher_app.R
import vn.xdeuhug.launcher_app.databinding.ItemAppBinding
import vn.xdeuhug.launcher_app.databinding.ItemLogoAppBinding
import vn.xdeuhug.launcher_app.model.AppInfo

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

class LogoAppAdapter(
    private val onItemClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<LogoAppAdapter.AppViewHolder>() {

    private var apps: List<AppInfo> = listOf()

    fun setApps(apps: List<AppInfo>) {
        this.apps = apps
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemLogoAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size

    class AppViewHolder(
        private val binding: ItemLogoAppBinding,
        private val onItemClick: (AppInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppInfo) {
            binding.logoApp.setImageDrawable(app.icon)
            binding.logoApp.setOnClickListener {
                onItemClick.invoke(app)
            }
        }
    }


}