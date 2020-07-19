package com.mike_milk.map.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mike_milk.map.R

/**
 *时间：2020/7/13
 *创建者：MIKE-MILK
 *描述：导航,从搜索那里拿到数据，然后提示用户是否开始导航
 */
class AMapNaviActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_navi)
    }
}