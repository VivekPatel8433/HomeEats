package com.project.homeeats.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {
    // Read Uri into sampled down Bitmap and encode to Base64
    fun uriToBase64(context: Context, uri: Uri, maxSize: Int = 400): String? {
        try {
            val contentResolver = context.contentResolver
            
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var inputStream: InputStream? = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            
            // Calculate inSampleSize
            var inSampleSize = 1
            if (options.outHeight > maxSize || options.outWidth > maxSize) {
                val halfHeight: Int = options.outHeight / 2
                val halfWidth: Int = options.outWidth / 2
                while (halfHeight / inSampleSize >= maxSize && halfWidth / inSampleSize >= maxSize) {
                    inSampleSize *= 2
                }
            }
            
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            options.inSampleSize = inSampleSize
            inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            
            if (bitmap == null) return null
            
            // Compress and encode
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos) // Compress aggressively to fit Firestore
            val b = baos.toByteArray()
            // Convert to Base64 with no wrapping so coil can decode it
            return "data:image/jpeg;base64," + Base64.encodeToString(b, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun base64ToByteArray(base64Str: String): ByteArray? {
        if (base64Str.isEmpty()) return null
        return try {
            val cleanStr = if (base64Str.contains(",")) base64Str.substringAfter(",") else base64Str
            Base64.decode(cleanStr, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
