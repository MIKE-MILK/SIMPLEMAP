package com.mike_milk.map.utlis

import android.util.Log
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.navi.model.NaviLatLng

/**
 *时间：2020/7/14
 *创建者：MIKE-MILK
 *描述：导航计算的工具类
 */
class NaviUtil {

    private val TAG:String="NaviUtil"
    public fun calculateDistance(star:NaviLatLng,end:NaviLatLng):Float{
        var y1:Double=star.getLatitude()
        var x1:Double=star.getLongitude()
        var y2:Double=end.getLatitude()
        var x2:Double=end.getLongitude()
        Log.d(TAG,"zhezhe")
        return AMapUtils.calculateLineDistance(LatLng(y1,x1), LatLng(y2,x2))
    }
}