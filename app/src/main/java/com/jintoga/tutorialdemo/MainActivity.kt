package com.jintoga.tutorialdemo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jintoga.tutorialdemo.viewtooltip.ViewTooltip
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        Handler().postDelayed({
            startTutorial()
        }, 900)
    }

    private fun init() {
        recyclerView.adapter = MyAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun startTutorial() {
        firstTutorial()
    }

    private fun firstTutorial() {
        val view = recyclerView.findViewHolderForAdapterPosition(2)!!.itemView
        val textView1 = view.findViewById<TextView>(R.id.textView5)
        val customTooltip = LayoutInflater.from(this).inflate(R.layout.custom_tooltip, null)
        ViewTooltip
                .on(this, textView1)
                .autoHide(false, 0)
                .corner(resources.getDimension(R.dimen.my_tooltip_corner_radius).toInt())
                .extraMarginLeftRight(resources.getDimension(R.dimen.extra_margin_left_right).toInt())
                .position(ViewTooltip.Position.BOTTOM)
                .withShadow(false)
                .backgroundColor(ContextCompat.getColor(this, R.color.black_50))
                .color(ContextCompat.getColor(this, R.color.white))
                .customView(customTooltip)
                .backGroundClickToHide(true)
                .clickToHide(true)
                .show()
    }
}
