package com.pratikbendre.randomstringgenerator.data.remote

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.pratikbendre.randomstringgenerator.model.RandomText
import com.pratikbendre.randomstringgenerator.utils.AppConstants
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentProviderService @Inject constructor(
    private val contentResolver: ContentResolver,
    private val gson: Gson
) {

    fun generateString(text_length: Int): RandomText? {
        val uri = Uri.parse(AppConstants.DATA_URI)

        var resultData: RandomText? = null

        val queryArgs = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, text_length)
        }
        val cursor = contentResolver.query(uri, null, queryArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val jsonString = it.getString(it.getColumnIndexOrThrow(AppConstants.DATA_COLUMN_NAME))
                try {
                    val jsonObject = JSONObject(jsonString)
                    val dataObject = jsonObject.getJSONObject("randomText")
                    resultData = gson.fromJson(dataObject.toString(), RandomText::class.java)

                } catch (e: Exception) {
                    Log.e("ContentResolver", "Error parsing JSON: ${e.message}")
                }
            }
        }

        return resultData
    }
}