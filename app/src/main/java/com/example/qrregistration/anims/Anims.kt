package com.example.qrregistration.anims

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.qrregistration.R

class Anims {
    fun wobble(context: Context, view: ImageView){
        val wobbleAnimation = AnimationUtils.loadAnimation(context, R.anim.wobble)
        view.startAnimation(wobbleAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            view.clearAnimation()
        }, 1000)
    }

    fun bounce(context: Context, view: ImageView){
        val bounceAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        view.startAnimation(bounceAnimation)
    }
}