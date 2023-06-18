package hr.tvz.android.mvpstrbad

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.RemoteViews
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import hr.tvz.android.mvpstrbad.model.Picture
import hr.tvz.android.mvpstrbad.viewmodels.ListViewModel
import hr.tvz.android.mvpstrbad.webserver.WebServerService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.awaitResponse

/**
 * Implementation of App Widget functionality.
 */
class LatestPictureWidget : AppWidgetProvider() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            GlobalScope.launch(Dispatchers.IO) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal suspend fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val retrofit = Retrofit.Builder()
        .baseUrl(ListViewModel.API_URL)
        .build()
    val service = retrofit.create(WebServerService::class.java)
    val response = service.getLatestPicture().awaitResponse()
    if (!response.isSuccessful) {
        return
    }

    val body = response.body() ?: return
    val picture = Json.decodeFromString<Picture>(body.string())
    val views = RemoteViews(context.packageName, R.layout.latest_picture_widget)

    val bitmap = withContext(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(picture.image)
            .allowHardware(false)
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        (result as BitmapDrawable).bitmap
    }
    views.setTextViewText(R.id.widget_title, picture.title)
    views.setImageViewBitmap(R.id.widget_image, bitmap)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    delay(1000)
}