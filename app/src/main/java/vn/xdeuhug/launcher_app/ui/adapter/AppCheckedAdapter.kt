package vn.xdeuhug.launcher_app.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.xdeuhug.launcher_app.R
import vn.xdeuhug.launcher_app.model.AppInfo

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

class AppCheckedAdapter : RecyclerView.Adapter<AppCheckedAdapter.AppViewHolder>() {
    private var apps: List<AppInfo> = emptyList()
    private val selectedApps = mutableSetOf<String>()

    var onAppSelected: ((String) -> Unit)? = null

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appName: TextView = itemView.findViewById(R.id.appName)
        private val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(app: AppInfo) {
            appName.text = app.appName
            appIcon.setImageDrawable(app.icon)
            checkBox.isChecked = selectedApps.contains(app.packageName)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedApps.add(app.packageName)
                } else {
                    selectedApps.remove(app.packageName)
                }
                onAppSelected?.invoke(app.packageName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_checked, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size

    fun setApps(apps: List<AppInfo>) {
        this.apps = apps
        notifyDataSetChanged()
    }

    fun getSelectedApps(): List<String> = selectedApps.toList()
}