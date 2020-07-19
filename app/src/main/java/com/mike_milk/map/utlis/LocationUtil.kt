package com.mike_milk.map.utlis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.mike_milk.map.R
import com.mike_milk.map.view.activity.RestRouteActivity


/**
 *时间：2020/7/11
 *创建者：MIKE-MILK
 *描述：确定位置的工具类
 */
class LocationUtil:AMapLocationListener{

    private var aMapLocationClient: AMapLocationClient? = null
    private var clientOption: AMapLocationClientOption? = null
    private var callBack: LocationCallBack? = null

    fun startLocation(context: Context){

        aMapLocationClient= AMapLocationClient(context)
        aMapLocationClient!!.setLocationListener(this)
        //初始化
        clientOption= AMapLocationClientOption()
        clientOption!!.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving)
        clientOption!!.setNeedAddress(true)
        clientOption!!.setOnceLocation(false)
        //刷新WiFi
        clientOption!!.setWifiScan(true)
        //模拟位置
        clientOption!!.setMockEnable(true)
        //定位的时间间隔,设置2000ms刷新一次
        clientOption!!.setInterval(2000)
        aMapLocationClient!!.setLocationOption(clientOption)
        aMapLocationClient!!.startLocation()

    }

    override fun onLocationChanged(p0: AMapLocation?) {
      if (p0!=null){
          if (p0.errorCode==0){
              var country:String=p0.getCountry()
              var province:String=p0.getProvince()
              var city:String=p0.getCity()
              var district:String=p0.getDistrict()
              var street:String=p0.getStreet()
              var lat:Double=p0.getLatitude()
              var lgt:Double=p0.getLongitude()
              callBack!!.callBack(country + province + city + district + street, lat, lgt, p0)
          }else{
              Log.e("AmapError", "location Error, ErrCode:"
                      + p0.getErrorCode() + ", errInfo:"
                      +p0.getErrorInfo())
          }
      }
    }

    public fun getMarkerOption (str:String,lat:Double,lgt:Double): MarkerOptions {
        var markerOptions:MarkerOptions= MarkerOptions()
        //自己设置的原点图
        markerOptions!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_position))
        markerOptions.position(LatLng(lat, lgt))
        markerOptions.title(str)
        markerOptions.snippet("纬度"+lat+"经度"+lgt)
        markerOptions.period(100)
        return markerOptions
    }

    interface LocationCallBack {
        fun callBack(
            str: String,
            lat: Double,
            lgt: Double,
            aMapLocation: AMapLocation?
        )
    }

    fun setLocationCallBack(callBack: LocationCallBack?) {
        this.callBack = callBack
    }
}