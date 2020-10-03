package com.yellowdo.library.ext

import android.Manifest

fun permissionToKr(permission: String) = permissionGroupToKr(permissionToGroup(permission))

@Suppress("DEPRECATION")
fun permissionToGroup(permission: String) = when (permission) {
    Manifest.permission.READ_CALENDAR,
    Manifest.permission.WRITE_CALENDAR -> PermissionGroup.CALENDAR
    Manifest.permission.CAMERA -> PermissionGroup.CAMERA
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.WRITE_CONTACTS,
    Manifest.permission.GET_ACCOUNTS -> PermissionGroup.CONTACTS
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION -> PermissionGroup.LOCATION
    Manifest.permission.RECORD_AUDIO -> PermissionGroup.MICROPHONE
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.ADD_VOICEMAIL,
    Manifest.permission.USE_SIP -> PermissionGroup.PHONE
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG,
    Manifest.permission.PROCESS_OUTGOING_CALLS -> PermissionGroup.CALL_LOG
    Manifest.permission.BODY_SENSORS -> PermissionGroup.SENSORS
    Manifest.permission.SEND_SMS,
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.READ_SMS,
    Manifest.permission.RECEIVE_WAP_PUSH,
    Manifest.permission.RECEIVE_MMS -> PermissionGroup.SMS
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE -> PermissionGroup.STORAGE
    else -> PermissionGroup.NON
}

fun permissionGroupToKr(group: PermissionGroup) = when (group) {
    is PermissionGroup.CALENDAR -> "캘린더"
    is PermissionGroup.CALL_LOG -> "통화 기록"
    is PermissionGroup.CAMERA -> "카메라"
    is PermissionGroup.CONTACTS -> "주소록"
    is PermissionGroup.LOCATION -> "위치"
    is PermissionGroup.MICROPHONE -> "마이크"
    is PermissionGroup.PHONE -> "전화"
    is PermissionGroup.SENSORS -> "신체 센서"
    is PermissionGroup.SMS -> "SMS"
    is PermissionGroup.STORAGE -> "저장공간"
    is PermissionGroup.NON -> ""

}

sealed class PermissionGroup {
    object CALENDAR : PermissionGroup()
    object CALL_LOG : PermissionGroup()
    object CAMERA : PermissionGroup()
    object CONTACTS : PermissionGroup()
    object LOCATION : PermissionGroup()
    object MICROPHONE : PermissionGroup()
    object PHONE : PermissionGroup()
    object SENSORS : PermissionGroup()
    object SMS : PermissionGroup()
    object STORAGE : PermissionGroup()
    object NON : PermissionGroup()
}
