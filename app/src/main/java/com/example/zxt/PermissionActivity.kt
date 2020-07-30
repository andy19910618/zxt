package com.example.zxt

import android.Manifest
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tezwez.base.common.BaseActivity

import permissions.dispatcher.*

@RuntimePermissions
open class PermissionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStorageWithPermissionCheck()
    }

    @NeedsPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA)
    fun showStorage() {
        Toast.makeText(this, "权限已打开", Toast.LENGTH_SHORT).show()
    }

    @OnShowRationale(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA)
    fun showRationaleForStorage(request: PermissionRequest) {
        showRationaleDialog("请点击允许,申请必要的权限", request)
    }

    @OnPermissionDenied(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA)
    fun onStorageDenied() {
        Toast.makeText(this, "已拒绝权限申请", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA)
    fun onStorageNeverAskAgain() {
        Toast.makeText(this, "请在手机设置中打开所有权限", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun showRationaleDialog(messageResId: String, request: PermissionRequest) {
        AlertDialog.Builder(this)
                .setPositiveButton("允许") { _, _ -> request.proceed() }
                .setNegativeButton("拒绝") { _, _ -> request.cancel() }
                .setCancelable(false)
                .setMessage(messageResId)
                .show()
    }
}