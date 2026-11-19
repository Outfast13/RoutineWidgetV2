package com.routine.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.SystemClock
import android.provider.AlarmClock
import android.view.View
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RoutineWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_TOGGLE_ITEM = "com.routine.widget.ACTION_TOGGLE_ITEM"
        const val ACTION_SWITCH_TAB = "com.routine.widget.ACTION_SWITCH_TAB"
        const val ACTION_CYCLE_DAY = "com.routine.widget.ACTION_CYCLE_DAY"
        const val ACTION_POMODORO_TOGGLE = "com.routine.widget.ACTION_POMODORO_TOGGLE"
        const val ACTION_POMODORO_PHASE = "com.routine.widget.ACTION_POMODORO_PHASE"
        const val ACTION_POMODORO_RESET = "com.routine.widget.ACTION_POMODORO_RESET"

        private const val STUDY_MILLIS = 50L * 60L * 1000L
        private const val FREE_MILLIS = 10L * 60L * 1000L
        private const val DISPLAY_OFFSET_MS = 500L

        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, RoutineWidgetProvider::class.java))
            for (id in ids) {
                updateAppWidget(context, manager, id)
            }
            manager.notifyAppWidgetViewDataChanged(ids, R.id.lv_routine_items)
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val prefs = context.getSharedPreferences("routine_prefs", Context.MODE_PRIVATE)
            val activeTab = prefs.getInt("active_tab", 0)
            val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val selectedDay = prefs.getInt("selected_day", currentDay)

            val isStudyPhase = prefs.getBoolean("pomo_is_study", true)
            val isRunning = prefs.getBoolean("pomo_running", false)
            val defaultDuration = if (isStudyPhase) STUDY_MILLIS else FREE_MILLIS
            val remainingMillis = prefs.getLong("pomo_remaining_ms", defaultDuration)
            val baseElapsed = prefs.getLong("pomo_base_elapsed", SystemClock.elapsedRealtime() + defaultDuration + DISPLAY_OFFSET_MS)

            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            when (activeTab) {
                0 -> {
                    views.setTextViewText(R.id.tv_header_title, "DAILY SCHEDULE")
                    views.setViewVisibility(R.id.btn_cycle_day, View.GONE)
                }
                1 -> {
                    views.setTextViewText(R.id.tv_header_title, RoutineData.getWorkoutForDay(selectedDay).first)
                    views.setViewVisibility(R.id.btn_cycle_day, View.VISIBLE)
                }
                2 -> {
                    views.setTextViewText(R.id.tv_header_title, "VOCAL & SINGING (25M)")
                    views.setViewVisibility(R.id.btn_cycle_day, View.GONE)
                }
                3 -> {
                    views.setTextViewText(R.id.tv_header_title, "HINDI -> ENGLISH ACCENT")
                    views.setViewVisibility(R.id.btn_cycle_day, View.GONE)
                }
            }

            val tabs = listOf(R.id.tab_daily, R.id.tab_workout, R.id.tab_vocal, R.id.tab_accent)
            tabs.forEachIndexed { index, viewId ->
                val isSelected = index == activeTab
                views.setInt(viewId, "setBackgroundColor", if (isSelected) Color.parseColor("#4090CAF9") else Color.parseColor("#1AFFFFFF"))
                views.setTextColor(viewId, if (isSelected) Color.parseColor("#FFFFFF") else Color.parseColor("#B0BEC5"))
                views.setOnClickPendingIntent(viewId, getTabPendingIntent(context, index))
            }

            views.setTextViewText(R.id.tv_pomo_phase, if (isStudyPhase) "STUDY 50m" else "FREE 10m")
            views.setChronometerCountDown(R.id.chrono_pomo, true)
            if (isRunning) {
                views.setChronometer(R.id.chrono_pomo, baseElapsed, null, true)
                views.setTextViewText(R.id.btn_pomo_toggle, "PAUSE")
            } else {
                val pausedBase = SystemClock.elapsedRealtime() + remainingMillis + DISPLAY_OFFSET_MS
                views.setChronometer(R.id.chrono_pomo, pausedBase, null, false)
                views.setTextViewText(R.id.btn_pomo_toggle, "START")
            }
            views.setOnClickPendingIntent(R.id.btn_pomo_toggle, getActionPendingIntent(context, ACTION_POMODORO_TOGGLE, 51))
            views.setOnClickPendingIntent(R.id.btn_pomo_phase, getActionPendingIntent(context, ACTION_POMODORO_PHASE, 52))
            views.setOnClickPendingIntent(R.id.btn_pomo_reset, getActionPendingIntent(context, ACTION_POMODORO_RESET, 53))
            views.setOnClickPendingIntent(R.id.btn_cycle_day, getActionPendingIntent(context, ACTION_CYCLE_DAY, 99))

            val serviceIntent = Intent(context, RoutineWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.lv_routine_items, serviceIntent)

            val clickIntent = Intent(context, RoutineWidgetProvider::class.java).apply {
                action = ACTION_TOGGLE_ITEM
            }
            val clickPendingIntent = PendingIntent.getBroadcast(
                context, 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            views.setPendingIntentTemplate(R.id.lv_routine_items, clickPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getTabPendingIntent(context: Context, tabIndex: Int): PendingIntent {
            val intent = Intent(context, RoutineWidgetProvider::class.java).apply {
                action = ACTION_SWITCH_TAB
                putExtra("tab_index", tabIndex)
            }
            return PendingIntent.getBroadcast(
                context, 10 + tabIndex, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun getActionPendingIntent(context: Context, actionStr: String, reqCode: Int): PendingIntent {
            val intent = Intent(context, RoutineWidgetProvider::class.java).apply {
                action = actionStr
            }
            return PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val prefs = context.getSharedPreferences("routine_prefs", Context.MODE_PRIVATE)
        val todayKey = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val lastDate = prefs.getString("last_update_date", "")
        if (lastDate != todayKey) {
            prefs.edit()
                .putString("last_update_date", todayKey)
                .putInt("selected_day", Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                .apply()
        }
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_routine_items)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val prefs = context.getSharedPreferences("routine_prefs", Context.MODE_PRIVATE)

        when (intent.action) {
            ACTION_SWITCH_TAB -> {
                val tabIndex = intent.getIntExtra("tab_index", 0)
                val editor = prefs.edit().putInt("active_tab", tabIndex)
                if (tabIndex == 1) {
                    editor.putInt("selected_day", Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                }
                editor.apply()
                updateAllWidgets(context)
            }
            ACTION_CYCLE_DAY -> {
                val current = prefs.getInt("selected_day", Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                val next = (current % 7) + 1
                prefs.edit().putInt("selected_day", next).apply()
                updateAllWidgets(context)
            }
            ACTION_POMODORO_TOGGLE -> {
                val isStudy = prefs.getBoolean("pomo_is_study", true)
                val defaultMs = if (isStudy) STUDY_MILLIS else FREE_MILLIS
                val isRunning = prefs.getBoolean("pomo_running", false)
                val now = SystemClock.elapsedRealtime()

                if (isRunning) {
                    val base = prefs.getLong("pomo_base_elapsed", now + defaultMs + DISPLAY_OFFSET_MS)
                    val rem = (base - DISPLAY_OFFSET_MS - now).coerceIn(0L, defaultMs)
                    prefs.edit()
                        .putBoolean("pomo_running", false)
                        .putLong("pomo_remaining_ms", rem)
                        .apply()
                } else {
                    val rem = prefs.getLong("pomo_remaining_ms", defaultMs).let { if (it <= 0L) defaultMs else it }
                    prefs.edit()
                        .putBoolean("pomo_running", true)
                        .putLong("pomo_base_elapsed", now + rem + DISPLAY_OFFSET_MS)
                        .apply()
                }
                updateAllWidgets(context)
            }
            ACTION_POMODORO_PHASE -> {
                val nextStudy = !prefs.getBoolean("pomo_is_study", true)
                val nextMs = if (nextStudy) STUDY_MILLIS else FREE_MILLIS
                prefs.edit()
                    .putBoolean("pomo_is_study", nextStudy)
                    .putBoolean("pomo_running", false)
                    .putLong("pomo_remaining_ms", nextMs)
                    .apply()
                updateAllWidgets(context)
            }
            ACTION_POMODORO_RESET -> {
                val isStudy = prefs.getBoolean("pomo_is_study", true)
                val resetMs = if (isStudy) STUDY_MILLIS else FREE_MILLIS
                prefs.edit()
                    .putBoolean("pomo_running", false)
                    .putLong("pomo_remaining_ms", resetMs)
                    .apply()
                updateAllWidgets(context)
            }
            ACTION_TOGGLE_ITEM -> {
                if (intent.hasExtra("alarm_hour")) {
                    val hour = intent.getIntExtra("alarm_hour", 16)
                    val minute = intent.getIntExtra("alarm_minute", 0)
                    val label = intent.getStringExtra("alarm_label") ?: "Routine Block"
                    val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                        putExtra(AlarmClock.EXTRA_HOUR, hour)
                        putExtra(AlarmClock.EXTRA_MINUTES, minute)
                        putExtra(AlarmClock.EXTRA_MESSAGE, label)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    try {
                        context.startActivity(alarmIntent)
                    } catch (_: Exception) {
                    }
                    return
                }

                val itemId = intent.getStringExtra("item_id") ?: return
                val todayKey = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
                val key = "${todayKey}_${itemId}"
                val currentVal = prefs.getBoolean(key, false)
                prefs.edit().putBoolean(key, !currentVal).apply()
                updateAllWidgets(context)
            }
        }
    }
}
