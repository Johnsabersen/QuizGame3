package com.example.quizgame.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.database.DatabaseHelper
import com.example.quizgame.databinding.ActivityQuizBinding
import com.example.quizgame.model.Question

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var dbHelper: DatabaseHelper
    private var questions = listOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0
    private var playerName = "Player" // ✅ Default name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // ✅ Ambil nama player dari Intent
        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"

        loadQuestions()
        displayQuestion()
        setupRadioGroupListener()

        binding.btnNext.isEnabled = false
        binding.btnNext.alpha = 0.5f

        binding.btnNext.setOnClickListener {
            checkAnswerAndProceed()
        }
    }

    private fun loadQuestions() {
        questions = dbHelper.getRandomQuestions(10)

        if (questions.isEmpty()) {
            Toast.makeText(this, "Tidak ada soal tersedia!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            binding.tvQuestionNumber.text = "Pertanyaan ${currentQuestionIndex + 1}/${questions.size}"
            binding.tvScore.text = "Skor: $score"
            binding.tvQuestion.text = question.question
            binding.radioA.text = question.optionA
            binding.radioB.text = question.optionB
            binding.radioC.text = question.optionC
            binding.radioD.text = question.optionD

            binding.radioGroup.clearCheck()
            binding.btnNext.isEnabled = false
            binding.btnNext.alpha = 0.5f
        }
    }

    private fun setupRadioGroupListener() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val isSelected = checkedId != -1
            binding.btnNext.isEnabled = isSelected
            binding.btnNext.alpha = if (isSelected) 1.0f else 0.5f
        }
    }

    private fun checkAnswerAndProceed() {
        val selectedId = binding.radioGroup.checkedRadioButtonId

        if (selectedId == -1) {
            Toast.makeText(this, "Pilih jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRadio = findViewById<RadioButton>(selectedId)
        val selectedAnswer = selectedRadio.text.toString()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (selectedAnswer == correctAnswer) {
            score++
            Toast.makeText(this, "✓ Benar!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "✗ Salah! Jawaban: $correctAnswer", Toast.LENGTH_SHORT).show()
        }

        currentQuestionIndex++

        if (currentQuestionIndex < questions.size) {
            displayQuestion()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("PLAYER_NAME", playerName) // ✅ Kirim nama ke ResultActivity
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL", questions.size)
        startActivity(intent)
        finish()
    }

    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher")
    override fun onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Keluar dari Kuis?")
            .setMessage("Progress akan hilang jika keluar sekarang")
            .setPositiveButton("Ya") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}