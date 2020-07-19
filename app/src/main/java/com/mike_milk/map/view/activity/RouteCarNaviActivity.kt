package com.mike_milk.map.view.activity

import android.os.Bundle
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
 *时间：2020/7/16
 *创建者：MIKE-MILK
 *描述：
 */
class RouteCarNaviActivity:AppCompatActivity(), AMapNaviListener, AMapNaviViewListener {

    private val TAG:String="ROUTECAR"
    var mAMapNaviView: AMapNaviView? = null
    var mAMapNavi: AMapNavi? = null
    private var endlat:Double?=null
    private var endlog:Double?=null
    protected var starnaviLatLng:NaviLatLng= NaviLatLng(29.529753,106.609417)
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

    override fun onResume() {
        super.onResume()
        mAMapNaviView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAMapNaviView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAMapNaviView!!.onDestroy()
        mAMapNavi!!.destroy()
    }

    override fun onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onInitNaviSuccess() {
        var strategy = 0
        try {
            strategy = mAMapNavi!!.strategyConvert(true, false, false, false, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mAMapNavi!!.calculateDriveRoute(sList, eList, mWayPointList, strategy)
        Log.d(TAG,""+mAMapNavi!!.calculateDriveRoute(sList, eList, mWayPointList, strategy))
    }

    override fun onStartNavi(type: Int) {}

    override fun onTrafficStatusUpdate() {}

    override fun onLocationChange(location: AMapNaviLocation?) {}

    override fun onGetNavigationText(type: Int, text: String?) {}

    override fun onGetNavigationText(s: String?) {}

    override fun onEndEmulatorNavi() {}

    override fun onArriveDestination() {}

    override fun onCalculateRouteFailure(errorInfo: Int) {}

    override fun onReCalculateRouteForYaw() {}

    override fun onReCalculateRouteForTrafficJam() {}

    override fun onArrivedWayPoint(wayID: Int) {}

    override fun onGpsOpenStatus(enabled: Boolean) {}

    override fun onNaviSetting() {}

    override fun onNaviMapMode(isLock: Int) {}

    override fun onNaviCancel() {
    }

    override fun onNaviTurnClick() {}

    override fun onNextRoadClick() {}

    override fun onScanViewButtonClick() {}

    @Deprecated("")
    override fun onNaviInfoUpdated(naviInfo: AMapNaviInfo?) {

    }

    override fun updateCameraInfo(aMapCameraInfos: Array<AMapNaviCameraInfo?>?) {}

    override fun onServiceAreaUpdate(amapServiceAreaInfos: Array<AMapServiceAreaInfo?>?) {}

    override fun onNaviInfoUpdate(naviinfo: NaviInfo?) {
        lastNaviInfo = naviinfo
        if (null != naviinfo) {
//            //获取当前路段剩余距离
//            var distance:Int =naviinfo.getCurStepRetainDistance();
//            //下一个街道名称
//            var roadName:String = naviinfo.getNextRoadName();
//            //获取路线剩余距离(总的路程剩余距离)
//            var allDitance:Int =naviinfo.getPathRetainDistance();
//            //获取路线剩余时间(总的路程剩余时间)
//            var allTime : Int = naviinfo.getPathRetainTime()
//            //获取导航转向图标类型
//            var iconType:Int=naviinfo.getIconType()
//            //每次NaviInfo更新的时候 准确的获取接下来的路长，以及接下来的路况
            var x= lastNaviInfo!!.getPathRetainDistance()
            Toast.makeText(this,""+x,Toast.LENGTH_SHORT).show()
            Log.d(TAG,""+x)
        }
    }

    override fun OnUpdateTrafficFacility(trafficFacilityInfo: TrafficFacilityInfo?) {}

    override fun OnUpdateTrafficFacility(aMapNaviTrafficFacilityInfo: AMapNaviTrafficFacilityInfo?) {}

    override fun showCross(aMapNaviCross: AMapNaviCross?) {}

    override fun hideCross() {}

    override fun showLaneInfo(
        laneInfos: Array<AMapLaneInfo?>?,
        laneBackgroundInfo: ByteArray?,
        laneRecommendedInfo: ByteArray?
    ) {
    }

    override fun hideLaneInfo() {}

    override fun onCalculateRouteSuccess(ints: IntArray?) {
//        onCalculateRouteSuccess(ints )
        mAMapNavi!!.startNavi(NaviType.GPS)
    }

    override fun notifyParallelRoad(i: Int) {}

    override fun OnUpdateTrafficFacility(aMapNaviTrafficFacilityInfos: Array<AMapNaviTrafficFacilityInfo?>?) {}

    override fun updateAimlessModeStatistics(aimLessModeStat: AimLessModeStat?) {}

    override fun updateAimlessModeCongestionInfo(aimLessModeCongestionInfo: AimLessModeCongestionInfo?) {}

    override fun onPlayRing(i: Int) {}

    override fun onLockMap(isLock: Boolean) {}

    override fun onNaviViewLoaded() {}

    override fun onMapTypeChanged(i: Int) {}

    override fun onNaviViewShowMode(i: Int) {}

    override fun onNaviBackClick(): Boolean {
        return false
    }


    override fun showModeCross(aMapModelCross: AMapModelCross?) {}

    override fun hideModeCross() {}

    override fun updateIntervalCameraInfo(
        aMapNaviCameraInfo: AMapNaviCameraInfo?,
        aMapNaviCameraInfo1: AMapNaviCameraInfo?,
        i: Int
    ) {
    }

    override fun showLaneInfo(aMapLaneInfo: AMapLaneInfo?) {}

    override fun onCalculateRouteSuccess(aMapCalcRouteResult: AMapCalcRouteResult?) {}

    override fun onCalculateRouteFailure(aMapCalcRouteResult: AMapCalcRouteResult?) {}

    override fun onNaviRouteNotify(aMapNaviRouteNotifyData: AMapNaviRouteNotifyData?) {}

    override fun onGpsSignalWeak(b: Boolean) {}
}