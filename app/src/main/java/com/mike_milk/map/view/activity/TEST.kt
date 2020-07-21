package com.mike_milk.map.view.activity

/**
 *时间：2020/7/20
 *创建者：MIKE-MILK
 *描述：
 */

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.*
import com.amap.api.navi.view.RouteOverLay
import com.amap.api.services.route.*
import com.autonavi.tbt.TrafficFacilityInfo
import com.mike_milk.map.R
import com.mike_milk.map.utlis.LocationUtil
import java.util.*
import kotlin.collections.ArrayList

/**
 *时间：2020/7/16
 *创建者：MIKE-MILK
 *描述：测试activity
 */
class TEST:AppCompatActivity(), AMapNaviListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    LocationSource , RouteSearch.OnRouteSearchListener {

    //需要的几个参数
    private var congestion:Boolean?=null
    private var cost:Boolean?=null
    private var hightspeed:Boolean?=null
    private var avoidhightspeed:Boolean?=null
    //绘制地图
    private var mapView:MapView?=null
    private var startMarker:MarkerOptions?=null
    private var staMarkers:Marker?=null
    private var endEMarker:MarkerOptions?=null
    private var endMarkers:Marker?=null
    //导航的地图
    private var amapNavi:AMapNavi?=null
    private var amap:AMap?=null
    //得到到搜索传过来的经纬度
    private var startLatlng:NaviLatLng?=null
    private var endLatLng:NaviLatLng?=null
    private var startList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    //中途的坐标集合
    private var wayList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    //终点坐标的集合
    private var endList: MutableList<NaviLatLng> = ArrayList<NaviLatLng>()
    //当前的路线
    private var routeOverlays: SparseArray<RouteOverLay> = SparseArray<RouteOverLay>()
    private var calculateSuccess:Boolean?=false
    private var chooseRoute:Boolean?=false
    private var routeIndex:Int?=null
    private var zindex = 1
    private var TAG:String="restActivity"
    private var endlat:Double?=null
    private var endlog:Double?=null
    private var startlat:Double?=null
    private var startlog:Double?=null
    private var locationUtil: LocationUtil?=null
    private var mListener:LocationSource.OnLocationChangedListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restroute)
        setLocationCallBack()
        getData()
        val gpsnavi = findViewById<View>(R.id.im_car)
        gpsnavi.setOnClickListener(this)
        mapView=findViewById(R.id.navi_view)
        mapView?.onCreate(savedInstanceState)
        amap=mapView?.getMap()
        //设置起点终点
        startMarker= MarkerOptions()
        startMarker!!.position(LatLng(29.529753,106.609417))
        startMarker!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start))
        staMarkers= amap?.addMarker(startMarker)
        //获取搜索传过来的经纬度
//        endEMarker= MarkerOptions()
//        endEMarker!!.position(LatLng(endlat!!,endlog!!))
//        endEMarker!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_end))
//      endMarkers=amap?.addMarker(endEMarker)
//        var latLng =LatLng(endlat!!,endlog!!);
        //设置显示比例
//        amap?.moveCamera(CameraUpdateFactory.zoomTo(15F));
//        amap?.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//        startMarker = amap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(
//                    BitmapFactory.decodeResource(resources, R.mipmap.ic_start))))
//        endEMarker=amap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.mipmap.ic_end))))
        amapNavi= AMapNavi.getInstance(application)

        amapNavi?.addAMapNaviListener(this)
    }

    private fun getData(){
        var bundle = this.intent.extras
        endlat=bundle?.getDouble("lat")
        endlog=bundle?.getDouble("log")
//        startlat=bundle?.getDouble("lats")
//        startlat=bundle?.getDouble("logs")
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        startList.clear()
        endList.clear()
        wayList.clear()
        routeOverlays.clear()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        routeOverlays.clear()
        val paths: HashMap<Int, AMapNaviPath> = amapNavi!!.getNaviPaths()
        for (i in p0!!.indices) {
            val path = paths[p0.get(i)]
            if (path != null) {
                drawRoutes(p0.get(i), path)
            }
        }
        amapNavi!!.startNavi(NaviType.GPS)
    }

    private fun drawRoutes(routeId:Int,path:AMapNaviPath){
        calculateSuccess=true
        amap?.moveCamera(CameraUpdateFactory.changeTilt(0F))
        val routeOverLay = RouteOverLay(amap, path, this)
        routeOverLay.isTrafficLine = false
        routeOverLay.addToMap()
        routeOverlays.put(routeId, routeOverLay)
    }

    public fun changRoute(){
        if(calculateSuccess!!){
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show()
            return
        }
        if (routeOverlays.size()==1){
            chooseRoute=true
            amapNavi?.selectRouteId(routeOverlays.keyAt(0))
            Toast.makeText(this, "导航距离:" + (amapNavi?.getNaviPath()?.getAllLength() ) + "m" + "\n" + "导航时间:" + (amapNavi?.getNaviPath()?.getAllTime()) + "s", Toast.LENGTH_SHORT).show()
            return
        }

        if (routeIndex!! >= routeOverlays.size()) {
            routeIndex = 0
        }
        var routeID:Int=routeOverlays.keyAt(routeIndex!!)
        //突出选择的那条路
        for (i in 0 until routeOverlays.size()) {
            val key = routeOverlays.keyAt(i)
            routeOverlays[key].setTransparency(0.4f)
        }
        val routeOverlay = routeOverlays[routeID]
        if (routeOverlay != null) {
            routeOverlay.setTransparency(1f)
            routeOverlay.setZindex(zindex++)
        }
        amapNavi?.selectRouteId(routeID)
        routeIndex!!.inc()
        chooseRoute=true
        var info: AMapRestrictionInfo? = amapNavi?.getNaviPath()?.getRestrictionInfo()
        if (info != null) {
            if (!TextUtils.isEmpty(info.restrictionTitle)) {
                Toast.makeText(this, info.restrictionTitle, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearRoute() {
        for (i in 0 until routeOverlays.size()) {
            val routeOverlay = routeOverlays.valueAt(i)
            routeOverlay.removeFromMap()
        }
        routeOverlays.clear()
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {

    }

    override fun onCalculateRouteFailure(p0: Int) {
        calculateSuccess=false
        Toast.makeText(applicationContext, "计算路线失败，errorcode＝"+p0, Toast.LENGTH_SHORT).show()
    }

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

    override fun onInitNaviSuccess() {}

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

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {}

    override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {}

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {}

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {}

    override fun showLaneInfo(p0: AMapLaneInfo?) {}

    override fun onClick(v: View?) {
        when(v?.getId()){
            R.id.im_car->{
                clearRoute()
//                if (avoidhightspeed!! && hightspeed!!) {
//                    Toast.makeText(applicationContext,"不走高速与高速优先不能同时为true.",Toast.LENGTH_LONG).show()
//                }
//                if (hightspeed!!&&hightspeed!!){
//                    Toast.makeText(applicationContext,"高速优先与避免收费不能同时为true.", Toast.LENGTH_LONG).show();
//                }
                var strategyFlag:Int=0
                try {
                    strategyFlag = amapNavi!!.strategyConvert(
                        congestion!!,
                        avoidhightspeed!!,
                        cost!!,
                        hightspeed!!,
                        true
                    )
                }catch (e:Exception){
                    e.printStackTrace()
                }
                if (strategyFlag>=0){
                    amapNavi?.calculateDriveRoute(startList,endList,wayList,strategyFlag)
                }
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.getParcelableExtra<Parcelable?>("poi") != null) {
            clearRoute()
            val poi: Poi = data.getParcelableExtra("poi")
            if (requestCode == 100) { //起点选择完成
                startLatlng =
                    NaviLatLng(poi.coordinate.latitude, poi.coordinate.longitude)
                staMarkers?.setPosition(
                    LatLng(
                        poi.coordinate.latitude,
                        poi.coordinate.longitude
                    )
                )
                startList.clear()
                startList.add(startLatlng!!)
            }
            if (requestCode == 200) { //终点选择完成
                Toast.makeText(this, "200", Toast.LENGTH_SHORT).show();
                endLatLng = NaviLatLng(poi.coordinate.latitude, poi.coordinate.longitude)
                endMarkers?.setPosition(
                    LatLng(
                        poi.coordinate.latitude,
                        poi.coordinate.longitude
                    )
                )
                endList.clear()
                endList.add(endLatLng!!)
                Log.d(TAG,""+poi.coordinate.latitude)
            }
        }
    }

    private fun setLocationCallBack(){
        locationUtil= LocationUtil()
        locationUtil!!.setLocationCallBack(object : LocationUtil.LocationCallBack{
            override fun callBack(
                str: String,
                lat: Double,
                lgt: Double,
                aMapLocation: AMapLocation?
            ) {
                amap?.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(lat,lgt)))
                mListener?.onLocationChanged(locationUtil?.getMarkerOption(str,lat,lgt))
                amap?.addMarker(locationUtil!!.getMarkerOption(str, lat, lgt))
                startlat=lat
                startlog=lgt
            }
        })
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        mListener=p0
        locationUtil?.startLocation(applicationContext)
    }

    override fun deactivate() {
        mListener=null
    }

    private fun LocationSource.OnLocationChangedListener?.onLocationChanged(markerOption: MarkerOptions?) { }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
        if (p1==1000) { //成功
            val list_path: MutableList<DrivePath>? = p0?.getPaths()
            for (i in 0 until list_path!!.size) {
                val list_step = list_path?.get(i)?.steps
                for (j in 0 until list_step!!.size) {
                    val listlatlone = list_step?.get(j)?.polyline
                    //画线
                    val latLngs: MutableList<LatLng> = ArrayList()
                    for (k in 0 until listlatlone!!.size) {
                        latLngs.add(
                            LatLng(
                                listlatlone[k].latitude,
                                listlatlone[k].longitude
                            )
                        )
                    }
                    val polyline: Polyline = amap!!.addPolyline(
                        PolylineOptions().addAll(latLngs)
                            .width(20F) //设置线宽度
                            .setDottedLine(true) //设置虚线
                            .color(-0xae8103)
                    )  //设置线的颜色
                }
            }
        }
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {}

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {}

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {}
}