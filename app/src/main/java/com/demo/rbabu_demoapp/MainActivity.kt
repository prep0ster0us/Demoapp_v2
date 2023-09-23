package com.demo.rbabu_demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.demo.rbabu_demoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // by default, set visibility of question text, answer field and submit button to be invisible (toggle visibility when random question button is pressed)
        binding.txtQuestion.visibility = View.INVISIBLE
        binding.txtAnswer.visibility = View.INVISIBLE
        binding.btnSubmit.visibility = View.INVISIBLE
    }
}