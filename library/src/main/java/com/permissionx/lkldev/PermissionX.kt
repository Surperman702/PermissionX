package com.permissionx.lkldev

import androidx.fragment.app.FragmentActivity

object PermissionX {

    private const val TAG = "InvisibleFragment"

    /**
     * 定义了一个request()方法，这个方法接收一个FragmentActivity参数、一个可变长度的permissions参数列表，
     * 以及一个callback回调。其中，FragmentActivity是AppCompatActivity的父类
     */
    fun request(
        activity: FragmentActivity,
        vararg permission: String,
        callback: PermissionCallback
    ) {
        /**
         * 首先获取FragmentManager的实例，
         * 然后调用findFragmentByTag()方法来判断传入的Activity参数中是否已经包含了指定TAG的 Fragment，
         * 也就是我们刚才编写的InvisibleFragment。如果已经包含则直接使用该Fragment，
         * 否则就创建一个新的InvisibleFragment实例，并将它添加到Activity中，同时指定一个TAG。
         */
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null) {
            existedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        // *表示将一个数组转换成可变长度参数传递过去
        fragment.requestNow(callback, *permission)
    }

}