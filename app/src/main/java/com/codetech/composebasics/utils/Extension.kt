package com.codetech.composebasics.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.codetech.composebasics.abstraction.sealed.handleError

object Extension {

    fun Activity.changeStatusBarColor(
        window: Window,
        color: Int,
        opacity: Float = 0f,
    ) {
        handleError {
            // Clear FLAG_TRANSLUCENT_STATUS flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // Add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // Get the color from resources
            val adjustedColor = ContextCompat.getColor(this, color)

            // Finally, change the color
            window.statusBarColor = adjustedColor

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val isLightColor = isColorLight(adjustedColor)
                window.decorView.systemUiVisibility = if (isLightColor) {
                    // Light background, set dark status bar content
                    window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    // Dark background, set light status bar content
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}

private fun isColorLight(color: Int): Boolean {
    val darkness = 1 - (0.299 * ((color shr 16) and 0xFF) +
            0.587 * ((color shr 8) and 0xFF) +
            0.114 * (color and 0xFF)) / 255
    return darkness < 0.5
}