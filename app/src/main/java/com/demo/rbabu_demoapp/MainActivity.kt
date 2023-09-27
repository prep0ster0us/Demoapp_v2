package com.demo.rbabu_demoapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.demo.rbabu_demoapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val TAG = "IceBreakerAndroidF23Tag"

    private val db =
        Firebase.firestore             // database variable which is connected to our Firestore database

    private val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        position = Position.Relative(0.5, 0.5),
        emitter = Emitter(duration = 50, TimeUnit.MILLISECONDS).max(50)
    )
    private var questionBank: MutableList<Questions>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fetch questions from firestore database (doing ahead of time to avoid async access issues with read/write later on)
        getQuestionsFromFirebase()

        Log.d(TAG, "Entered into onCreate()")

        // set binding variable to get data from edit text fields
        val fName = binding.txtFirstName
        val lName = binding.txtLastName
        val pName = binding.txtPrefName

        val question = binding.txtQuestion
        val answer = binding.txtAnswer

        // by default, set visibility of question text, answer field and submit button to be invisible (toggle visibility when random question button is pressed)
        question.visibility = View.INVISIBLE
        answer.visibility = View.INVISIBLE
        binding.btnSubmit.visibility = View.INVISIBLE

        // set action for button press, to fetch random question
        binding.btnGetQuestion.setOnClickListener {
            Log.d(TAG, "Fetch Question button was pressed!")
            // unhide views related to questions once user requests to receive a random question
            question.visibility = View.VISIBLE
            answer.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.VISIBLE

            // set question textview to display fetched question (from firestore database)
            question.text = this.questionBank?.random()?.questionText

            // visual log
            Toast.makeText(this@MainActivity, question.text, Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnSubmit.setOnClickListener {
            Log.d(TAG, "Write user input to database...")

            if (fName.text.isEmpty() or lName.text.isEmpty() or pName.text.isEmpty() or answer.text.isEmpty()) {
                Log.d(TAG, "One of the input fields is empty!")
                Toast.makeText(
                    this@MainActivity,
                    "Please fill in all the details before submitting!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener       // exit from button click
            }

            writeAnswersToFirebase(
                fName.text.toString(),
                lName.text.toString(),
                pName.text.toString(),
                answer.text.toString(),
                binding.konfettiView
            )
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
                    questionBank!!.add(question)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error fetching documents: =>", exception)
            }
    }

    private fun writeAnswersToFirebase(
        firstName: String,
        lastName: String,
        prefName: String,
        userAnswer: String,
        konfetti: KonfettiView
    ) {
        val student = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "preferredName" to prefName,
            "answer" to userAnswer
        )

        db.collection("students")
            .add(student)
            .addOnSuccessListener { document ->
                Log.d(TAG, "Document successfully written with ID ${document.id}")
                Toast.makeText(
                    this@MainActivity,
                    "You're in, $firstName! \n Document ID = ${document.id}",
                    Toast.LENGTH_SHORT
                )
                    .show()

                // show dialog box for successful database entry
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.submit_dialog)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                // show confetti animation (after dialog)
                konfetti.start(party)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding document to database: ", exception)
            }
    }
}
