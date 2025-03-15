package com.example.launcher_for_tv.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.example.launcher_for_tv.R
import com.example.launcher_for_tv.data.AppModel

@SuppressLint("NewApi")
@Composable
fun AppItem(app: AppModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(app.packageName) },
    ) {
        val painter: Painter? = when (val icon = app.icon) {
            is BitmapDrawable -> BitmapPainter(icon.bitmap.asImageBitmap())
            is AdaptiveIconDrawable -> getAdaptiveIconBitmap(icon)?.asImageBitmap()
                ?.let { BitmapPainter(it) }

            else -> null
        } ?: painterResource(id = R.mipmap.ic_launcher)

        painter?.let {
            Image(
                painter = it,
                contentDescription = app.name,
                modifier = Modifier.size(64.dp)
            )
        }

        Text(text = app.name, style = MaterialTheme.typography.bodyMedium)
    }
}

fun getAdaptiveIconBitmap(drawable: Drawable): Bitmap? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable is AdaptiveIconDrawable) {
        val bitmap = createBitmap(108, 108)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } else {
        null
    }
}
