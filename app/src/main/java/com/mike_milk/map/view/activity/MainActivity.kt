package com.mike_milk.map.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mike_milk.map.R

/**
 *时间：2020/7/11
 *创建者：MIKE-MILK
 *描述：添加一个地图的fragment用于显示地图，一个底部导航进行选择
 */
class MainActivity :AppCompatActivity(){

    private var navPosition: BottomNavigationView?=null
    private var fragment:Fragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navPosition=findViewById(R.id.bottom_navigation)
        supportFragmentManager.inTransaction {
            fragment?.let { add(R.id.bottom_navigation, it) }
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}