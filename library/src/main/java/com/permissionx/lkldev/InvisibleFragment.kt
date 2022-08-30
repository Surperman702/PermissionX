package com.permissionx.lkldev

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import java.util.ArrayList

/**
 * typealias关键字可以用于给任意类型指定一个别名，比如我们将(Boolean, List<String>) -> Unit的别名指定成了PermissionCallback
 */
typealias PermissionCallback = (Boolean, List<String>) -> Unit

class InvisibleFragment : Fragment() {

    /**
     * 定义了一个callback变量作为运行时权限申请结果的回调通知方式，
     * 并将它声明成了一种函数类型变量，该函数类型接收Boolean和List<String>这两种类型的参数，并且没有返回值
     */
    private var callback: PermissionCallback? = null

    /**
     * 该方法接收一个与callback变量类型相同的函数类型参数，同时还使用vararg关键字接收了一个可变长度的permissions参数列表。
     * 在requestNow()方法中，我们将传递进来的函数类型参数赋值给callback变量，
     * 然后调用Fragment中提供的requestPermissions()方法去立即申请运行时权限，
     * 并将permissions参数列表传递进去，这样就可以实现由外部调用方自主指定要申请哪些权限的功能了
     */
    fun requestNow(cb: PermissionCallback, vararg permission: String) {
        callback = cb
        requestPermissions(permission, 1)
    }

    /**
     * 重写onRequestPermissionsResult()方法，并在这里处理运行时权限的申请结果。
     * 可以看到，我们使用了一个deniedList列表来记录所有被用户拒绝的权限，然后遍历grantResults数组，
     * 如果发现某个权限未被用户授权，就将它添加到deniedList中。
     * 遍历结束后使用一个allGranted变量来标识是否所有申请的权限均已被授权，判断的依据就是deniedList列表是否为空。
     * 最后使用callback变量对运行时权限的申请结果进行回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            val deniedList = ArrayList<String>()
            for ((index, result) in grantResults.withIndex()) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[index])
                }
            }
            var allGranted = deniedList.isEmpty()
            callback?.let { it(allGranted, deniedList) }
        }
    }
}