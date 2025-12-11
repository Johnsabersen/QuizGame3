package com.example.quizgame.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.database.DatabaseHelper
import com.example.quizgame.databinding.ActivityResultBinding
import com.example.quizgame.model.Score
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var dbHelper: DatabaseHelper
    private var playerName: String = "Player" // ✅ Simpan nama player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // ✅ Ambil data dari Intent
        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"
        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 10)
        val percentage = (score.toFloat() / total * 100).toInt()

        binding.tvFinalScore.text = "$score/$total"
        binding.tvPercentage.text = "$percentage%"

        // ✅ Simpan skor dengan nama player
        saveScore(playerName, score)

        // ✅ Tombol "Main Lagi" - Langsung ke QuizActivity dengan nama yang sama
        binding.btnPlayAgain.setOnClickListener {
            playAgainWithSameName()
        }

        // ✅ Tombol "Kembali ke Menu" - Ke MainActivity
        binding.btnHome.setOnClickListener {
            backToHome()
        }
    }

    // ✅ Main lagi dengan nama yang sama (tidak perlu input nama lagi)
    private fun playAgainWithSameName() {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("PLAYER_NAME", playerName) // ✅ Kirim nama yang sama
        startActivity(intent)
        finish() // Tutup ResultActivity
    }

    // ✅ Kembali ke menu utama (MainActivity)
    private fun backToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    // ✅ Simpan skor dengan nama player
    private fun saveScore(playerName: String, score: Int) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val scoreData = Score(
            playerName = playerName,
            score = score,
            date = currentDate
        )

        dbHelper.insertScore(scoreData)
    }

    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher")
    override fun onBackPressed() {
        // Ketika tekan back, kembali ke MainActivity
        backToHome()
    }
}