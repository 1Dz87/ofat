package ofat.my.ofat.logging

import android.os.Environment
import android.util.Log
import ofat.my.ofat.permission.isExternalStorageWritable
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!isLoggable(tag, priority)) return
        val result = File(getLogDir().absolutePath + File.separator, SimpleDateFormat("dd_MM_yy", Locale.getDefault()).format(
            Date()
        ) + ".txt")
        val outputLogMessage: String = prepareLogMessage(tag, message, t)
        if (isExternalStorageWritable()) {
            if (!result.exists()) {
                result.createNewFile()
                writeLog {
                    result.appendText(outputLogMessage, Charsets.UTF_8)
                }
            } else {
                writeLog {
                    println(outputLogMessage)
                }
            }
        }
    }

    private fun writeLog(func: () -> Unit) {
        func.invoke()
    }

    private fun prepareLogMessage(tag: String?, message: String, t: Throwable?): String {
        return if (t != null && t.stackTrace != null)
            "$message ${t.stackTrace} \n"
        else "$message  No stack trace.\n"
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + "[" + element.lineNumber + "]:"
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
    }

    private fun getLogDir(): File {
        val file: File = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/ofat/logs")
        } else {
            File("${Environment.getDataDirectory()}/ofat/logs")
        }
        if (!file.exists()) file.mkdirs()
        return file
    }
}