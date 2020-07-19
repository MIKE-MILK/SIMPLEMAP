package com.mike_milk.map.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewOptions
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.NaviLatLng
import com.mike_milk.map.R
import com.mike_milk.map.base.BaseActivity
import kotlin.math.log

/**
 *时间：2020/7/16
 *创建者：MIKE-MILK
 *描述：
 */
class NaviActivity :BaseActivity(){

    private var TAG="NAAA"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_base_navi)
        aMapNaviView= findViewById<AMapNaviView>(R.id.navi_view)
        aMapNaviView?.onCreate(savedInstanceState)
        aMapNaviView?.setAMapNaviViewListener(this)
        var amapNaviViewOptions:AMapNaviViewOptions= AMapNaviViewOptions()
        amapNaviViewOptions.setScreenAlwaysBright(false)
        aMapNaviView?.setViewOptions(amapNaviViewOptions)
    }

    override fun onInitNaviSuccess() {
        super.onInitNaviSuccess()
        /**
         * 开发文档:
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        var bundle = this.intent.extras
        var endlat=bundle?.getDouble("lat")
        var endlog=bundle?.getDouble("log")
        var endnaviLatLng: NaviLatLng = NaviLatLng(endlat!!,endlog!!)
        eList= listOf(endnaviLatLng)
        var strategy:Int=1
        try {
//            strategy= amapNavi?.strategyConvert(true,false,false,true,false)!!
        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.d(TAG," "+sList+" "+eList)
        amapNavi?.calculateDriveRoute(sList, eList, mWayPointList, strategy)
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        super.onCalculateRouteSuccess(p0)
        amapNavi?.startNavi(NaviType.GPS)
    }
}