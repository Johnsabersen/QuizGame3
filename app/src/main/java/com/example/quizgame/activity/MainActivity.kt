package com.example.quizgame.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.database.DatabaseHelper
import com.example.quizgame.databinding.ActivityMainBinding
import com.example.quizgame.databinding.DialogInputNameBinding
import com.example.quizgame.model.Question

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Isi database dengan soal sample
        insertSampleQuestions()

        // ✅ Tombol Start - Tampilkan dialog input nama
        binding.btnStart.setOnClickListener {
            showInputNameDialog()
        }

        // ✅ Tombol Scores - Tampilkan top scores dengan tombol reset
        binding.btnScores.setOnClickListener {
            showTopScoresDialog()
        }
    }

    // ✅ Dialog Input Nama Player - PERBAIKAN
    private fun showInputNameDialog() {
        val dialogBinding = DialogInputNameBinding.inflate(LayoutInflater.from(this))

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Mulai") { dialog, _ ->
                val playerName = dialogBinding.etPlayerName.text.toString().trim()

                when {
                    playerName.isEmpty() -> {
                        Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        showInputNameDialog() // Tampilkan lagi
                    }
                    playerName.length < 3 -> {
                        Toast.makeText(this, "Nama minimal 3 karakter!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        showInputNameDialog() // Tampilkan lagi
                    }
                    else -> {
                        startQuiz(playerName)
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    // ✅ Mulai Quiz dengan nama player
    private fun startQuiz(playerName: String) {
        val questions = dbHelper.getAllQuestions()
        if (questions.isEmpty()) {
            Toast.makeText(this, "Belum ada soal tersedia!", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("PLAYER_NAME", playerName)
            startActivity(intent)
        }
    }

    // ✅ Dialog Top Scores dengan tombol Reset
    private fun showTopScoresDialog() {
        val scores = dbHelper.getTopScores(10)

        if (scores.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("🏆 Skor Tertinggi")
                .setMessage("Belum ada skor tersimpan.\n\nMainkan kuis untuk mencetak skor pertama!")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        // Format data untuk ditampilkan
        val message = scores.mapIndexed { index, score ->
            val medal = when (index) {
                0 -> "🥇"
                1 -> "🥈"
                2 -> "🥉"
                else -> "${index + 1}."
            }
            "$medal ${score.playerName} - ${score.score} poin"
        }.joinToString("\n\n")

        // Dialog dengan tombol Reset
        AlertDialog.Builder(this)
            .setTitle("🏆 Top 10 Skor Tertinggi")
            .setMessage(message)
            .setPositiveButton("Tutup", null)
            .setNeutralButton("Reset Semua") { _, _ ->
                confirmResetScores()
            }
            .show()
    }

    // ✅ Konfirmasi sebelum reset scores
    private fun confirmResetScores() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Reset Skor")
            .setMessage("Apakah Anda yakin ingin menghapus semua skor?\n\n⚠️ Tindakan ini tidak dapat dibatalkan!")
            .setPositiveButton("Ya, Hapus Semua") { _, _ ->
                resetAllScores()
            }
            .setNegativeButton("Batal", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    // ✅ Reset semua scores
    private fun resetAllScores() {
        val result = dbHelper.deleteAllScores()

        if (result > 0) {
            Toast.makeText(this, "✓ Berhasil menghapus $result skor", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Tidak ada skor untuk dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertSampleQuestions() {
        val existingQuestions = dbHelper.getAllQuestions()

        if (existingQuestions.isEmpty()) {
            val questions = listOf(
                Question(
                    question = "Ibukota Indonesia adalah?",
                    optionA = "Bandung",
                    optionB = "Jakarta",
                    optionC = "Surabaya",
                    optionD = "Medan",
                    correctAnswer = "Jakarta",
                    category = "Umum"
                ),
                Question(
                    question = "Berapa hasil dari 15 + 25?",
                    optionA = "35",
                    optionB = "40",
                    optionC = "45",
                    optionD = "50",
                    correctAnswer = "40",
                    category = "Matematika"
                ),
                Question(
                    question = "Planet terbesar di tata surya adalah?",
                    optionA = "Mars",
                    optionB = "Venus",
                    optionC = "Jupiter",
                    optionD = "Saturnus",
                    correctAnswer = "Jupiter",
                    category = "Sains"
                ),
                Question(
                    question = "Siapa presiden pertama Indonesia?",
                    optionA = "Soeharto",
                    optionB = "Soekarno",
                    optionC = "BJ Habibie",
                    optionD = "Megawati",
                    correctAnswer = "Soekarno",
                    category = "Sejarah"
                ),
                Question(
                    question = "Bahasa pemrograman untuk Android adalah?",
                    optionA = "Python",
                    optionB = "Kotlin",
                    optionC = "C++",
                    optionD = "Ruby",
                    correctAnswer = "Kotlin",
                    category = "Teknologi"
                ),
                Question(
                    question = "Berapa jumlah provinsi di Indonesia?",
                    optionA = "34",
                    optionB = "36",
                    optionC = "38",
                    optionD = "40",
                    correctAnswer = "38",
                    category = "Umum"
                ),
                Question(
                    question = "Warna bendera Indonesia adalah?",
                    optionA = "Merah Putih",
                    optionB = "Biru Putih",
                    optionC = "Hijau Putih",
                    optionD = "Kuning Putih",
                    correctAnswer = "Merah Putih",
                    category = "Umum"
                ),
                Question(
                    question = "Satuan mata uang Indonesia adalah?",
                    optionA = "Ringgit",
                    optionB = "Rupiah",
                    optionC = "Baht",
                    optionD = "Dollar",
                    correctAnswer = "Rupiah",
                    category = "Umum"
                ),
                Question(
                    question = "Candi Borobudur terletak di provinsi?",
                    optionA = "Jawa Barat",
                    optionB = "Jawa Tengah",
                    optionC = "Jawa Timur",
                    optionD = "DI Yogyakarta",
                    correctAnswer = "Jawa Tengah",
                    category = "Sejarah"
                ),
                Question(
                    question = "H2O adalah rumus kimia dari?",
                    optionA = "Air",
                    optionB = "Garam",
                    optionC = "Gula",
                    optionD = "Oksigen",
                    correctAnswer = "Air",
                    category = "Sains"
                ),
                Question(
                    question = "Berapa hasil dari 7 x 8?",
                    optionA = "54",
                    optionB = "56",
                    optionC = "58",
                    optionD = "60",
                    correctAnswer = "56",
                    category = "Matematika"
                ),
                Question(
                    question = "Gunung tertinggi di Indonesia adalah?",
                    optionA = "Gunung Merapi",
                    optionB = "Gunung Semeru",
                    optionC = "Gunung Rinjani",
                    optionD = "Puncak Jaya",
                    correctAnswer = "Puncak Jaya",
                    category = "Umum"
                ),
                Question(
                    question = "Hari kemerdekaan Indonesia adalah?",
                    optionA = "17 Agustus",
                    optionB = "1 Juni",
                    optionC = "28 Oktober",
                    optionD = "10 November",
                    correctAnswer = "17 Agustus",
                    category = "Sejarah"
                ),
                Question(
                    question = "Ibu kota Jawa Barat adalah?",
                    optionA = "Semarang",
                    optionB = "Surabaya",
                    optionC = "Bandung",
                    optionD = "Yogyakarta",
                    correctAnswer = "Bandung",
                    category = "Umum"
                ),
                Question(
                    question = "Penemu lampu pijar adalah?",
                    optionA = "Albert Einstein",
                    optionB = "Thomas Edison",
                    optionC = "Nikola Tesla",
                    optionD = "Isaac Newton",
                    correctAnswer = "Thomas Edison",
                    category = "Sejarah"
                )
            )

            questions.forEach { question ->
                dbHelper.insertQuestion(question)
            }

            Toast.makeText(this, "✓ Database diisi dengan ${questions.size} soal", Toast.LENGTH_SHORT).show()
        }
    }
}