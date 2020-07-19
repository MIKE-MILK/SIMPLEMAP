package com.mike_milk.map.base

import android.os.Bundle
import android.os.PersistableBundle
import com.amap.api.navi.AMapNaviView
import com.mike_milk.map.R

/**
 *时间：2020/7/15
 *创建者：MIKE-MILK
 *描述：
 */
class BaseNaviActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_base_navi)
        aMapNaviView= findViewById<AMapNaviView>(R.id.navi_view)
        aMapNaviView?.onCreate(savedInstanceState)
        aMapNaviView?.setAMapNaviViewListener(this)
    }
}