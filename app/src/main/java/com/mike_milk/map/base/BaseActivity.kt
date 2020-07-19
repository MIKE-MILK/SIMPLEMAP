package com.mike_milk.map.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewListener
import com.amap.api.navi.model.*
import com.autonavi.tbt.TrafficFacilityInfo
import java.util.*
import kotlin.collections.ArrayList

/**
 *时间：2020/7/13
 *创建者：MIKE-MILK
 *描述：一个BaseActivity
 */
open class BaseActivity:AppCompatActivity(),AMapNaviListener,AMapNaviViewListener {

    protected var aMapNaviView:AMapNaviView?=null
    protected var starnaviLatLng:NaviLatLng= NaviLatLng(29.529753,106.609417)
    protected var endnaviLatLng:NaviLatLng= NaviLatLng()
    protected var amapNavi:AMapNavi?=null
    protected var sList: List<NaviLatLng> = ArrayList<NaviLatLng>()
    protected var eList: List<NaviLatLng> = ArrayList<NaviLatLng>()
    protected var mWayPointList: List<NaviLatLng> = ArrayList<NaviLatLng>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        amapNavi= AMapNavi.getInstance(application)
        amapNavi?.addAMapNaviListener(this)
        amapNavi?.setUseInnerVoice(true)

        amapNavi?.setEmulatorNaviSpeed(70)
        sList= listOf(starnaviLatLng)
        eList= listOf(endnaviLatLng)
    }

    override fun onResume() {
        super.onResume()
        aMapNaviView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        aMapNaviView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        aMapNaviView?.onDestroy()
        amapNavi?.stopNavi()
        amapNavi?.destroy()
    }
    override fun onNaviInfoUpdate(p0: NaviInfo?) {

    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalculateRouteFailure(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGpsSignalWeak(p0: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEndEmulatorNavi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onArrivedWayPoint(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onArriveDestination() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayRing(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTrafficStatusUpdate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGpsOpenStatus(p0: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCross(p0: AMapNaviCross?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetNavigationText(p0: Int, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetNavigationText(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideCross() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInitNaviFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInitNaviSuccess() {

    }

    override fun onReCalculateRouteForTrafficJam() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateIntervalCameraInfo(
        p0: AMapNaviCameraInfo?,
        p1: AMapNaviCameraInfo?,
        p2: Int
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLaneInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showModeCross(p0: AMapModelCross?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideModeCross() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReCalculateRouteForYaw() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartNavi(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyParallelRoad(p0: Int) {
        if (p0==0){
            Toast.makeText(this,"当前在主辅路过渡",Toast.LENGTH_SHORT).show()
            Log.d("...","当前在主辅路过渡")
            return
        }
        if (p0==1){
            Toast.makeText(this,"当前在主路",Toast.LENGTH_SHORT).show()
            Log.d("...","当前在主路")
            return
        }
        if (p0==2){
            Toast.makeText(this,"当前在铺路",Toast.LENGTH_SHORT).show()
            Log.d("...","当前在铺路")
        }
    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviTurnClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScanViewButtonClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLockMap(p0: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapTypeChanged(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviCancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviViewLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviBackClick(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviMapMode(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNextRoadClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviViewShowMode(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNaviSetting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}