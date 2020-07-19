package com.mike_milk.map.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.*
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.AMapException
import com.amap.api.services.weather.*
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener
import com.getbase.floatingactionbutton.FloatingActionButton
import com.mike_milk.map.R
import com.mike_milk.map.utlis.LocationUtil

/**
 *时间：2020/7/12
 *创建者：MIKE-MILK
 *描述：显示地图
 */
class MapActivity: AppCompatActivity(),LocationSource, OnWeatherSearchListener, View.OnClickListener {

    private val TAG:String="MapActivity"
    private var map: MapView?=null
    private var aMap: AMap?=null
    private var locationUtil: LocationUtil?=null
    private var mListener:LocationSource.OnLocationChangedListener?=null
    private var autoCompleteTextView:AutoCompleteTextView?=null
    private var Button:Button?=null
    private var locationStyle:MyLocationStyle?=null
    private val cityName:String="重庆市"
    private var forecast:TextView?=null
    private var report:TextView?=null
    private var reporttime:TextView?=null
    private var weather:TextView?=null
    private var temp:TextView?=null
    private var wind:TextView?=null
    private var humidity:TextView?=null
    private var query:WeatherSearchQuery?=null
    private var weatherLive:LocalWeatherLive?=null
    private var weatherSearch:WeatherSearch?=null
    private var weatherForecast:LocalWeatherForecast?=null
    private var forecastList:MutableList<LocalDayWeatherForecast>?=null
    private var night:FloatingActionButton?=null
    private var star:FloatingActionButton?=null
    private var first:FloatingActionButton?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet_behavior)
        map = findViewById(R.id.map)
        night=findViewById(R.id.btn_night)
        night!!.setOnClickListener(this)
        star=findViewById(R.id.btn_star)
        star!!.setOnClickListener(this)
        first=findViewById(R.id.btn_first)
        first!!.setOnClickListener(this)
        autoCompleteTextView=findViewById(R.id.auto_keyWord)
        map?.onCreate(savedInstanceState)
        initView()
        init()
        searchlive()
        searchforcast()
        iniListen()
    }

    private fun initView(){
        if(aMap == null){
            aMap = map?.getMap()
        }
        setLocationCallBack()
        //设置定位监听
        aMap!!.setLocationSource(this)
        //设置缩放级别
        aMap!!.moveCamera(CameraUpdateFactory.zoomTo(15F))
        //设置定位标点
        locationStyle= MyLocationStyle()
        locationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        aMap!!.setMyLocationStyle(locationStyle)
        //显示定位层并可触发，默认false
        aMap!!.setMyLocationEnabled(true)
    }
    //点击搜索框跳转至新的搜索activity
    private fun iniListen(){
        autoCompleteTextView?.setOnClickListener {
            val intent = Intent(this@MapActivity, InputtipsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init(){
        var city:TextView=findViewById(R.id.tx_city)
        city.setText(cityName)
        forecast=findViewById(R.id.forecast)
        report=findViewById(R.id.reporttime1)
        reporttime=findViewById(R.id.reporttime2)
        weather=findViewById(R.id.weather)
        temp=findViewById(R.id.temp)
        wind=findViewById(R.id.wind)
        humidity=findViewById(R.id.humidity)
    }

    private fun searchlive(){
        query = WeatherSearchQuery(cityName,WeatherSearchQuery.WEATHER_TYPE_FORECAST)
        weatherSearch = WeatherSearch(this)
        weatherSearch!!.setOnWeatherSearchListener(this)
        weatherSearch!!.setQuery(query)
        weatherSearch!!.searchWeatherAsyn()
    }

    private fun searchforcast(){
        query= WeatherSearchQuery(cityName,WeatherSearchQuery.WEATHER_TYPE_LIVE)
        weatherSearch= WeatherSearch(this)
        weatherSearch!!.setOnWeatherSearchListener(this)
        weatherSearch!!.setQuery(query)
        weatherSearch!!.searchWeatherAsyn()
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
                aMap?.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(lat,lgt)))
                mListener?.onLocationChanged(locationUtil?.getMarkerOption(str,lat,lgt))
                aMap?.addMarker(locationUtil!!.getMarkerOption(str, lat, lgt))
                Log.d("Map",""+lat+"  "+lgt)
            }
        })
    }

    override fun deactivate() {
        mListener=null
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        mListener=p0
        locationUtil?.startLocation(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
//        map?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
        map?.onSaveInstanceState(outState);
    }

    override fun onWeatherLiveSearched(p0: LocalWeatherLiveResult?, p1: Int) {
        if (p1==AMapException.CODE_AMAP_SUCCESS){
            if (p0!=null&&p0.getLiveResult()!=null){
                weatherLive=p0.getLiveResult()
                report?.setText(weatherLive!!.getReportTime()+"发布")
                weather?.setText(weatherLive!!.getWeather())
                temp?.setText(weatherLive?.getTemperature()+"°")
                wind?.setText(weatherLive?.getWindDirection()+"风   "+weatherLive?.getWindPower()+"级")
                humidity?.setText("湿度  "+weatherLive?.getHumidity()+"%")
                Log.d(TAG,"+"+weatherLive)
            }
        }
    }

    override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
        if (p1==AMapException.CODE_AMAP_SUCCESS){
            if (p0!=null&&p0.getForecastResult()!=null&&p0.getForecastResult().getWeatherForecast()!=null
                &&p0.getForecastResult().weatherForecast.size>0){
                weatherForecast=p0.forecastResult
                forecastList=weatherForecast!!.weatherForecast
                fillforecast()
            }
        }
    }
    private fun fillforecast(){
        reporttime?.setText(weatherForecast?.getReportTime()+"发布")
        var forecasts:String=""
        for (i in forecastList!!.indices) {
            val localdayweatherforecast: LocalDayWeatherForecast = forecastList!!.get(i)
            var week: String? = null
            when (Integer.valueOf(localdayweatherforecast.week)) {
                1 -> week = "周一"
                2 -> week = "周二"
                3 -> week = "周三"
                4 -> week = "周四"
                5 -> week = "周五"
                6 -> week = "周六"
                7 -> week = "周日"
                else -> {
                }
            }
            val temp = String.format("%-3s/%3s",
                localdayweatherforecast.dayTemp + "°",
                localdayweatherforecast.nightTemp + "°"
            )
            val date = localdayweatherforecast.date
            forecasts+=date+"  "+week+"  "+temp+"  " + "\n\n"
        }
        forecast?.setText(forecasts)
    }

    override fun onClick(v: View?) {
        when(v!!.getId()){
            R.id.btn_first->{
                aMap?.setMapType(AMap.MAP_TYPE_NORMAL)
            }
            R.id.btn_night->{
                aMap?.setMapType(AMap.MAP_TYPE_NIGHT)
            }
            R.id.btn_star->{
                aMap?.setMapType(AMap.MAP_TYPE_SATELLITE)
            }
        }
    }
}
private fun LocationSource.OnLocationChangedListener?.onLocationChanged(markerOption: MarkerOptions?) {}
