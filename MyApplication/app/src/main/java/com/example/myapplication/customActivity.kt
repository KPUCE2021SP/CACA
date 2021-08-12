package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_custom.*
import kotlinx.android.synthetic.main.activity_home.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import androidx.core.content.ContextCompat


class customActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        val layout1: (HorizontalScrollView) =findViewById(R.id.scrollview_color);
        val layout2: (HorizontalScrollView) =findViewById(R.id.scrollview_deco);
        val layout3: (HorizontalScrollView) =findViewById(R.id.scrollview_eye);



        btn_color.setOnClickListener {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.INVISIBLE);
    }
        btn_deco.setOnClickListener {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.INVISIBLE);
        }

        btn_face.setOnClickListener {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.VISIBLE);
        }


        val bodyframe: FrameLayout = findViewById(R.id.body)

        ImageButton_color1.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_1)

        }

        ImageButton_color2.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_2)

        }

        ImageButton_color3.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_3)

        }

        ImageButton_color4.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_4)

        }

        ImageButton_color5.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_5)

        }

        ImageButton_color6.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_6)

        }

        ImageButton_color7.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_7)

        }

        ImageButton_color8.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_8)

        }

        ImageButton_color9.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_9)

        }

        ImageButton_color10.setOnClickListener {

            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_10)

        }

        val decoframe: FrameLayout = findViewById(R.id.deco)

        ImageButton_color11.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. flushing)

        }

        ImageButton_color12.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. beret)

        }

        ImageButton_color13.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. glass)

        }

        ImageButton_color14.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. crown)

        }

        ImageButton_color15.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. flower)

        }

        ImageButton_color16.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. baby)

        }

        ImageButton_color17.setOnClickListener {

            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. sister)

        }




        val faceframe: FrameLayout = findViewById(R.id.face)

        ImageButton_color18.setOnClickListener {

            faceframe.foreground = ContextCompat.getDrawable(this, R.drawable. normaleye)

        }

        ImageButton_color19.setOnClickListener {

            faceframe.foreground = ContextCompat.getDrawable(this, R.drawable. blackeye)

        }

    }
}