package com.yellowdo.library.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int, action: FragmentTransaction.() -> Unit = {}) {
    supportFragmentManager.transact {
        this.action()
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragmentInActivity(fragment: Fragment, frameId: Int, action: FragmentTransaction.() -> Unit = {}) {
    supportFragmentManager.transact {
        this.action()
        add(frameId, fragment)
    }
}

inline fun <reified T : Activity> AppCompatActivity.startActivity(noinline block: (Intent.() -> Unit)? = null, noinline action: (() -> Unit)? = null) {
    startActivity(Intent(this, T::class.java).apply {
        block?.let { this.apply(block) }
    })
    action?.invoke()
}

fun AppCompatActivity.toastShow(msg: Any?, duration: Int = Toast.LENGTH_SHORT, action: (() -> Unit)? = null) {
    if (isFinishing) return
    when (msg) {
        is String -> msg
        is Int -> getString(msg)
        else -> null
    }?.let { Toast.makeText(applicationContext, it, duration).show() }
    action?.invoke()
}

@Suppress("DEPRECATION")
fun AppCompatActivity.setShowWhenLockedAndTurnScreenOn() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
    } else {
        this.window.addFlags(
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }
}

/*
    AndroidManifest.xml 추가
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
* */
@SuppressLint("BatteryLife")
fun AppCompatActivity.checkBatteryOptimization(
    packageName: String = this.packageName,
    title: String = "권한이 필요합니다.",
    message: String = "서비스를 유지하기 위해서는 해당 어플을 \"배터리 사용량 최적화\" 목록에서 제외하는 권한이 필요합니다. 계속하시겠습니까?"
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

    (this.getSystemService(Context.POWER_SERVICE) as PowerManager).takeIf { it.isIgnoringBatteryOptimizations(packageName) }?.run {
        logE("checkBatteryOptimization: Already ignored Battery Optimizations.")
    } ?: run {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("예") { _, _ ->
                this.startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply { data = Uri.parse(String.format("package:%s", packageName)) })
            }
            .setNegativeButton("아니오") { _, _ -> }
            .show()
    }
}

fun AppCompatActivity.keepScreen(onoff: Boolean = true) {
    when (onoff) {
        true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@SuppressLint("NewApi")
fun AppCompatActivity.fullScreenWithTranslucent() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
}

@Suppress("DEPRECATION")
fun AppCompatActivity.wakeUp() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
    } else {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )
    }
    acquire()
}

fun AppCompatActivity.hardwareAccelerated() {
    window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
}


@SuppressLint("ObsoleteSdkInt")
inline fun <reified T> AppCompatActivity.restart(triggerAtMillis: Long = System.currentTimeMillis() + 100, intent: Intent.() -> Unit = {}, action: () -> Unit = {}) {
    val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, T::class.java).apply(intent), 0)
    (getSystemService(Context.ALARM_SERVICE) as AlarmManager).run {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            else -> set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }
    action()
}

fun ComponentActivity.showAlertExit(block: () -> Unit = {}) {
    showAlertDialog(title = "종료", message = "앱을 종료 하시겠습니까?", ok = "확인", positiveListener = { _, _ ->
        block()
    }, cancel = "취소")
}


fun ComponentActivity.showOverlayPermission(
    title: String = "권한 요청",
    message: String = "다른 앱 위에 표시 하기 위해 권한이 필요 합니다.",
    block: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays()) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { d, _ ->
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    it.resultCode
                    block.invoke(canDrawOverlays())
                }.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                d.dismiss()
            }
        }.show()
    } else {
        block.invoke(true)
    }
}

fun ComponentActivity.requestPermissions(permissions: Array<String>, success: () -> Unit, fail: (String) -> Unit) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        map.filterValues { !it }.run {
            when {
                isEmpty() -> success()
                else -> AlertDialog.Builder(this@requestPermissions).apply {
                    setTitle("권한 요청")
                    setMessage(map { permissionToKr(it.key) }.toList().distinct().joinToString() + " 권한을 위해 설정 화면으로 이동 하겠습니까?")
                    setPositiveButton("확인") { d, _ ->
                        d.dismiss()
                        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            checkPermissions(permissions).let { permissions ->
                                permissions.filterValues { !it }.run { isEmpty().isTrue { success() }.isNot { fail(map { permissionToKr(it.key) }.toList().distinct().joinToString()) } }
                            }
                        }.launch(actionApplicationDetailsSettings())
                    }
                    setNegativeButton("종료") { d, _ ->
                        d.dismiss()
                        finishAffinity()
                    }
                }.show()
            }
        }
    }.launch(permissions)
}

fun Activity.checkPermissions(permissions: Array<String>): Map<String, Boolean> =
    mutableMapOf<String, Boolean>().apply {
        if (permissions.isEmpty()) return@apply

        permissions.forEach {
            put(it, ActivityCompat.checkSelfPermission(this@checkPermissions, it) == PackageManager.PERMISSION_GRANTED)
        }
    }
