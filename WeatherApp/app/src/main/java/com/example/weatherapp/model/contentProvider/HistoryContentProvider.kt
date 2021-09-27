package com.example.weatherapp.model.contentProvider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.Transformations.map
import com.example.weatherapp.model.database.HistoryDAO
import com.example.weatherapp.view.App
import java.lang.IllegalArgumentException

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH = "HistoryEntity"

class HistoryContentProvider : ContentProvider() {

    private var authorities: String? = null // Адрес URI
    private lateinit var uriMatcher: UriMatcher // Помогает определить тип адреса URI

    // Типы данных
    private var entityContentType: String? = null // Набор строк
    private var entityContentItemType: String? = null // Одна строка

    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        val historyDao = App.getHistoryDao()
        val id = ContentUris.parseId(uri)
        historyDao.deleteById(id)
        context?.contentResolver?.notifyChange(uri, null)
        return 1

    }

    override fun getType(uri: Uri): String?=
        when (uriMatcher.match(uri)) {
            URI_ALL -> entityContentType
            URI_ID -> entityContentItemType
            else -> null
        }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO()

    }

    override fun onCreate(): Boolean {
        authorities = "geekbrains.provider"
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        uriMatcher.addURI(authorities, "$ENTITY_PATH/", URI_ID)
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true

    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val historyDao:HistoryDAO = App.getHistoryDao()

        val cursor = when(uriMatcher.match(uri)){
            URI_ALL->historyDao.getHistoryCursor()
            URI_ID->{
                val  id = ContentUris.parseId(uri)
                historyDao.getHistoryCursor()
            }
            else-> throw IllegalArgumentException("Wrong uri")
        }
        cursor.setNotificationUri(context?.contentResolver,contentUri)
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }


}