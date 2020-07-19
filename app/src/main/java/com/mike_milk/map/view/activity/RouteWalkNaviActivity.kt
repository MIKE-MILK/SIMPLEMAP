package com.mike_milk.map.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewListener
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.*
import com.autonavi.tbt.TrafficFacilityInfo
import com.mike_milk.map.R

/**
 *时间：2020/7/18
 *创建者：MIKE-MILK
 *描述：
 */
class RouteWalkNaviActivity:AppCompatActivity() , AMapNaviListener, AMapNaviViewListener {

    private val TAG:String="ROUTEWALK"
    var mAMapNaviView: AMapNaviView? = null
    var mAMapNavi: AMapNavi? = null
    private var endlat:Double?=null
    private var endlog:Double?=null
    protected var starnaviLatLng: NaviLatLng = NaviLatLng(29.529753,106.609417)
    protected var endnaviLatLng: NaviLatLng? =null
    protected var sList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    protected var eList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    protected var mWayPointList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    private var lastNaviInfo: NaviInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_navi)
        getData()
        mAMapNaviView = findViewById<View>(R.id.navi_view) as AMapNaviView
        mAMapNaviView!!.onCreate(savedInstanceState)
        mAMapNaviView!!.setAMapNaviViewListener(this)
        mAMapNavi = AMapNavi.getInstance(applicationContext)
        endnaviLatLng= NaviLatLng(endlat!!,endlog!!)
        sList.add(starnaviLatLng)
        eList.add(endnaviLatLng!!)
        mAMapNavi!!.addAMapNaviListener(this)
        mAMapNavi!!.setUseInnerVoice(true)
        mAMapNavi!!.setEmulatorNaviSpeed(60)
        Log.d(TAG,""+endnaviLatLng)
        Log.d(TAG,"e:"+eList+" s:"+sList)
    }

    private fun getData(){
        var bundle = this.intent.extras
        endlat=bundle?.getDouble("lat")
        endlog=bundle?.getDouble("log")
        Log.d(TAG,"lat:"+endlat+"log:"+endlog)
    }

    override fun onNaviInfoUpdate(naviinfo: NaviInfo?) {
        lastNaviInfo = naviinfo
        if (null != naviinfo) {
            var x= lastNaviInfo!!.getPathRetainDistance()
            Toast.makeText(this,""+x, Toast.LENGTH_SHORT).show()
            Log.d(TAG,""+x)
        }
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        mAMapNavi!!.startNavi(NaviType.GPS)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAMapNaviView!!.onDestroy()
        mAMapNavi!!.destroy()
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {}

    override fun onCalculateRouteFailure(p0: Int) {}

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {}

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {}

    override fun onGpsSignalWeak(p0: Boolean) {}

    override fun onEndEmulatorNavi() {}

    override fun onArrivedWayPoint(p0: Int) {}

    override fun onArriveDestination() {}

    override fun onPlayRing(p0: Int) {}

    override fun onTrafficStatusUpdate() {}

    override fun onGpsOpenStatus(p0: Boolean) {}

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {}

    override fun showCross(p0: AMapNaviCross?) {}

    override fun onGetNavigationText(p0: Int, p1: String?) {}

    override fun onGetNavigationText(p0: String?) {}

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {}

    override fun hideCross() {}

    override fun onInitNaviFailure() {}

    override fun onInitNaviSuccess() {
        mAMapNavi!!.calculateWalkRoute(starnaviLatLng, endnaviLatLng)
        Log.d(TAG,""+mAMapNavi!!.calculateWalkRoute(starnaviLatLng, endnaviLatLng))
    }

    override fun onReCalculateRouteForTrafficJam() {}

    override fun updateIntervalCameraInfo(
        p0: AMapNaviCameraInfo?,
        p1: AMapNaviCameraInfo?,
        p2: Int
    ) {}

    override fun hideLaneInfo() {}

    override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {}

    override fun showModeCross(p0: AMapModelCross?) {}

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {}

    override fun hideModeCross() {}

    override fun onLocationChange(p0: AMapNaviLocation?) {}

    override fun onReCalculateRouteForYaw() {}

    override fun onStartNavi(p0: Int) {}

    override fun notifyParallelRoad(p0: Int) {}

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {}

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) { }

    override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {}

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {}

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {}

    override fun showLaneInfo(p0: AMapLaneInfo?) {}

    override fun onNaviTurnClick() {}

    override fun onScanViewButtonClick() {}

    override fun onLockMap(p0: Boolean) {}

    override fun onMapTypeChanged(p0: Int) {}

    override fun onNaviCancel() {}

    override fun onNaviViewLoaded() {}

    override fun onNaviBackClick(): Boolean {
        return false
    }

    override fun onNaviMapMode(p0: Int) {}

    override fun onNextRoadClick() {}

    override fun onNaviViewShowMode(p0: Int) {}

    override fun onNaviSetting() {}
}