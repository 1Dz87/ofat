package ofat.my.ofat.permission

import android.Manifest
import android.app.Activity
import android.os.Environment
import androidx.core.app.ActivityCompat

private object REQUEST_CODE {
    fun value() : Int = 2
}

fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

fun requestExternalStoragePermission(activity: Activity) {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE.value())
}