package com.demo.rbabu_demoapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import com.demo.rbabu_demoapp.databinding.ActivitySplashScreenBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashScreenBinding =
            ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashImage.alpha = 0f
        binding.splashImage.animate()
            .setDuration(2500)
            .alpha(1f)

        binding.splashText.alpha = 0f
        binding.splashText.animate()
            .setDuration(2500)
            .alpha(1f)
            .withEndAction {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
    }
}