package vn.xdeuhug.launcher_app.utils

import android.content.Context
import android.widget.Toast
import vn.xdeuhug.launcher_app.model.AppInfo

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
object AppUtils {
    fun openApp(context: Context, app: AppInfo) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show()
        }
    }
}