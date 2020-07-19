package com.mike_milk.map.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mike_milk.map.R


/**
 *时间：2020/7/11
 *创建者：MIKE-MILK
 *描述：欢迎页
 */
class WelcomeActivity : AppCompatActivity() {

    private var ivBackground: View? = null
    private var text:TextView?=null
    private var alphaAnimation: AlphaAnimation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        ivBackground = findViewById(R.id.image)
        text=findViewById(R.id.tx_title)
        //动画
        initAnimation()
        //动画结束进入主页
        initTo()
    }
    //动画显示
   private fun initAnimation(){
        alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation!!.setDuration(3000)
        ivBackground!!.animation = alphaAnimation
        text!!.animation=alphaAnimation

    }
    //主页
   private fun initTo(){
        alphaAnimation!!.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            // 动画完成跳转至登录界面
            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(this@WelcomeActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

}