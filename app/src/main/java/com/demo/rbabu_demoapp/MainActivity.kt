package com.demo.rbabu_demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.demo.rbabu_demoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "IceBreakerAndroidF23Tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fetch questions from firestore database (doing ahead of time to avoid async access issues with read/write later on)
        getQuestionsFromFirebase()

        // by default, set visibility of question text, answer field and submit button to be invisible (toggle visibility when random question button is pressed)
        binding.txtQuestion.visibility = View.INVISIBLE
        binding.txtAnswer.visibility = View.INVISIBLE
        binding.btnSubmit.visibility = View.INVISIBLE

        Log.d(TAG, "Entered into onCreate()")

        // set binding variable to get data from edit text fields
        val fName = binding.txtFirstName
        val lName = binding.txtLastName
        val pName = binding.txtPrefName

        val question = binding.txtQuestion
        val answer = binding.txtAnswer

        // set action for button press, to fetch random question
        binding.btnGetQuestion.setOnClickListener {
            Log.d(TAG, "Fetch Question button was pressed!")

            // set question textview to display fetched question (from firestore database)
//            question.text = questionBank?.random().toString()
        }
    }

    private fun getQuestionsFromFirebase() {
        Log.d(TAG, "Fetching questions from database...")
        TODO("Not yet implemented")
    }
}