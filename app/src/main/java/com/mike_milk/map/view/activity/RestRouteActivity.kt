package com.mike_milk.map.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
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
import com.amap.api.navi.model.*
import com.amap.api.navi.view.RouteOverLay
import com.amap.api.services.route.*
import com.autonavi.tbt.TrafficFacilityInfo
import com.mike_milk.map.R
import com.mike_milk.map.utlis.LocationUtil
import com.mike_milk.map.utlis.NaviUtil
import java.util.*
import kotlin.collections.ArrayList

/**
 *时间：2020/7/16
 *创建者：MIKE-MILK
 *描述：
 */
class RestRouteActivity:AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    LocationSource , RouteSearch.OnRouteSearchListener {

    //绘制地图
    private var mapView:MapView?=null
    private var startMarker:MarkerOptions?=null
    private var staMarkers:Marker?=null
    private var endEMarker:MarkerOptions?=null
    private var endMarkers:Marker?=null
    //导航的地图
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
    private var naviUtil:NaviUtil?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restroute)
        setLocationCallBack()
        getData()
        val car = findViewById<View>(R.id.im_car) as ImageView
        val walk= findViewById<View>(R.id.im_walk) as ImageView
        car.setOnClickListener(this)
        walk.setOnClickListener(this)
        mapView=findViewById(R.id.navi_view)
        mapView?.onCreate(savedInstanceState)
        amap=mapView?.getMap()
        //设置起点终点
        startMarker= MarkerOptions()
        startMarker!!.position(LatLng(29.529753,106.609417))
        startMarker!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start))
        staMarkers= amap?.addMarker(startMarker)
        //获取搜索传过来的经纬度
        endEMarker= MarkerOptions()
        endEMarker!!.position(LatLng(endlat!!,endlog!!))
        endEMarker!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_end))
        endMarkers=amap?.addMarker(endEMarker)
         var latLng =LatLng(endlat!!,endlog!!)
        //设置显示比例
       amap?.moveCamera(CameraUpdateFactory.zoomTo(15F));
        amap?.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        startLatlng= NaviLatLng(29.529753,106.609417)
        endLatLng= NaviLatLng(endlat!!, endlog!!)
        Log.d(TAG,""+naviUtil?.calculateDistance(startLatlng!!,endLatLng!!)+" "+endLatLng)
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
        routeIndex!!.inc()
        chooseRoute=true
    }

    private fun clearRoute() {
        for (i in 0 until routeOverlays.size()) {
            val routeOverlay = routeOverlays.valueAt(i)
            routeOverlay.removeFromMap()
        }
        routeOverlays.clear()
    }


    override fun onClick(v: View?) {
        when(v?.getId()){
            R.id.im_walk->{
                val bundle = Bundle()
                val intent = Intent(this@RestRouteActivity, RouteWalkNaviActivity::class.java)
                bundle.putDouble("lat",endlat!!)
                bundle.putDouble("log",endlog!! )
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.im_car->{
                val bundle = Bundle()
                val intent = Intent(this@RestRouteActivity, RouteCarNaviActivity::class.java)
                bundle.putDouble("lat",endlat!!)
                bundle.putDouble("log",endlog!! )
                intent.putExtras(bundle)
                startActivity(intent)
                }
            }
        }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {}

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
