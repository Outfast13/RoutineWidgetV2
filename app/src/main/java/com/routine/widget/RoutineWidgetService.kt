package com.routine.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RoutineWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RoutineRemoteViewsFactory(this.applicationContext)
    }
}

class RoutineRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var items: List<RoutineItem> = emptyList()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val prefs = context.getSharedPreferences("routine_prefs", Context.MODE_PRIVATE)
        val tab = prefs.getInt("active_tab", 0)
        val selectedDay = prefs.getInt("selected_day", Calendar.getInstance().get(Calendar.DAY_OF_WEEK))

        items = when (tab) {
            0 -> RoutineData.getDailySchedule()
            1 -> RoutineData.getWorkoutForDay(selectedDay).second
            2 -> RoutineData.getVocalRegimen()
            3 -> RoutineData.getAccentDrills()
            else -> RoutineData.getDailySchedule()
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = items.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position < 0 || position >= items.size) return RemoteViews(context.packageName, R.layout.widget_item)

        val item = items[position]
        val prefs = context.getSharedPreferences("routine_prefs", Context.MODE_PRIVATE)
        val todayKey = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val isChecked = prefs.getBoolean("${todayKey}_${item.id}", false)

        val views = RemoteViews(context.packageName, R.layout.widget_item)
        views.setTextViewText(R.id.tv_checkbox, if (isChecked) "☑" else "☐")
        views.setTextColor(R.id.tv_checkbox, if (isChecked) Color.parseColor("#66BB6A") else Color.parseColor("#90CAF9"))

        views.setTextViewText(R.id.tv_item_title, item.title)
        views.setTextViewText(R.id.tv_item_sub, item.subtitle)

        if (isChecked) {
            views.setTextColor(R.id.tv_item_title, Color.parseColor("#757575"))
            views.setInt(R.id.tv_item_title, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        } else {
            views.setTextColor(R.id.tv_item_title, Color.parseColor("#F5F5F5"))
            views.setInt(R.id.tv_item_title, "setPaintFlags", Paint.ANTI_ALIAS_FLAG)
        }

        val toggleIntent = Intent().apply {
            putExtra("item_id", item.id)
        }
        views.setOnClickFillInIntent(R.id.item_click_area, toggleIntent)

        if (item.alarmHour24 != null) {
            views.setViewVisibility(R.id.btn_set_alarm, View.VISIBLE)
            val alarmIntent = Intent().apply {
                putExtra("alarm_hour", item.alarmHour24)
                putExtra("alarm_minute", item.alarmMinute)
                putExtra("alarm_label", item.title)
            }
            views.setOnClickFillInIntent(R.id.btn_set_alarm, alarmIntent)
        } else {
            views.setViewVisibility(R.id.btn_set_alarm, View.GONE)
        }

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
