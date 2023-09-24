package com.demo.rbabu_demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.demo.rbabu_demoapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val TAG = "IceBreakerAndroidF23Tag"

    private val db = Firebase.firestore             // database variable which is connected to our Firestore database
    private var questionBank: MutableList<Questions>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fetch questions from firestore database (doing ahead of time to avoid async access issues with read/write later on)
        getQuestionsFromFirebase()

        Log.d(TAG, "Entered into onCreate()")

        // set binding variable to get data from edit text fields
//        val fName = binding.txtFirstName
//        val lName = binding.txtLastName
//        val pName = binding.txtPrefName
//
//        val question = binding.txtQuestion
//        val answer = binding.txtAnswer

        // by default, set visibility of question text, answer field and submit button to be invisible (toggle visibility when random question button is pressed)
//        question.visibility = View.INVISIBLE
//        answer.visibility = View.INVISIBLE
//        binding.btnSubmit.visibility = View.INVISIBLE

        // set action for button press, to fetch random question
        binding.btnGetQuestion.setOnClickListener {
            Log.d(TAG, "Fetch Question button was pressed!")
            Toast.makeText(this@MainActivity, questionBank?.random().toString(), Toast.LENGTH_SHORT).show()

            // set question textview to display fetched question (from firestore database)
            binding.txtQuestion.text = this.questionBank?.random()?.questionText
        }
    }

    private fun getQuestionsFromFirebase() {
        Log.d(TAG, "Fetching questions from database...")

        db.collection("questions")
            .get()
            .addOnSuccessListener { documents ->
                questionBank = mutableListOf()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val question = document.toObject(Questions::class.java)
                    questionBank!!.add(question) ?: "Empty database!"
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error fetching documents: =>", exception)
            }
    }
}