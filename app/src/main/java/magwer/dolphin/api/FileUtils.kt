package magwer.dolphin.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*
import java.util.*

fun loadBitmapAsset(context: Context, fileName: String): Bitmap {
    var ins: InputStream? = null
    try {
        ins = context.assets.open(fileName)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        val image = BitmapFactory.decodeStream(ins)
        ins?.close()
        return image
    }
}

fun loadStringAsset(context: Context, fileName: String): List<String> {
    var ins: InputStream? = null
    var inr: InputStreamReader? = null
    var reader: BufferedReader? = null
    try {
        ins = context.assets.open(fileName)
        inr = InputStreamReader(ins)
        reader = BufferedReader(inr)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        val result = reader?.readLines() ?: listOf()
        reader?.close()
        inr?.close()
        ins?.close()
        return result
    }
}

fun List<String>.join(): String {
    val builder = StringJoiner("\n")
    for (line in this)
        builder.add(line)
    return builder.toString()
}
