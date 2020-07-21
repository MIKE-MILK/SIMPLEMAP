package com.mike_milk.map.utlis

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 *时间：2020/7/21
 *创建者：MIKE-MILK
 *描述：权限的动态申请
 */
object PermissionUtils {
    private val TAG = PermissionUtils::class.java.simpleName
    const val CODE_RECORD_AUDIO = 0
    const val CODE_GET_ACCOUNTS = 1
    const val CODE_READ_PHONE_STATE = 2
    const val CODE_CALL_PHONE = 3
    const val CODE_CAMERA = 4
    const val CODE_ACCESS_FINE_LOCATION = 5
    const val CODE_ACCESS_COARSE_LOCATION = 6
    const val CODE_READ_EXTERNAL_STORAGE = 7
    const val CODE_WRITE_EXTERNAL_STORAGE = 8
    const val CODE_SEND_SMS = 9
    const val CODE_CHANGE_WIFI_STATE = 10
    const val CODE_CHANGE_NETWORK_STATE = 11
    const val CODE_ACCESS_WIFI_STATE = 12
    const val CODE_WRITE_SETTINGS = 13
    const val CODE_MULTI_PERMISSION = 100
    const val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
    const val PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS
    const val PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
    const val PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE
    const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    const val PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS
    const val PERMISSION_CHANGE_WIFI_STATE = Manifest.permission.CHANGE_WIFI_STATE
    const val PERMISSION_CHANGE_NETWORK_STATE = Manifest.permission.CHANGE_NETWORK_STATE
    const val PERMISSION_ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE
    const val PERMISSION_WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS
    private val requestPermissions = arrayOf(
        PERMISSION_RECORD_AUDIO,
        PERMISSION_GET_ACCOUNTS,
        PERMISSION_READ_PHONE_STATE,
        PERMISSION_CALL_PHONE,
        PERMISSION_CAMERA,
        PERMISSION_ACCESS_FINE_LOCATION,
        PERMISSION_ACCESS_COARSE_LOCATION,
        PERMISSION_READ_EXTERNAL_STORAGE,
        PERMISSION_WRITE_EXTERNAL_STORAGE,
        PERMISSION_SEND_SMS,
        PERMISSION_CHANGE_WIFI_STATE,
        PERMISSION_CHANGE_NETWORK_STATE,
        PERMISSION_ACCESS_WIFI_STATE,
        PERMISSION_WRITE_SETTINGS
    )

    fun requestPermission(activity: Activity?, requestCode: Int) {
        if (activity == null) {
            return
        }
        Log.i(TAG, "requestPermission requestCode:$requestCode")
        if (requestCode < 0 || requestCode >= requestPermissions.size) {
            Log.w(TAG, "requestPermission illegal requestCode:$requestCode")
            return
        }
        val requestPermission = requestPermissions[requestCode]
        Log.i(TAG, "requestPermission :$requestPermission")
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有某个权限
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, requestPermission)
            //判断这个权限是否已经授权过
            println("checkCallPhonePermission  0代表已经获得权限：：：$checkCallPhonePermission")
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) { //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        requestPermission
                    )
                ) Toast.makeText(activity, requestPermission, Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(requestPermission),
                    requestCode
                )
                return
            }
        }
    }
}