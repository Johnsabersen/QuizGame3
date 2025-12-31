📖 Tentang Project
Quiz Game adalah aplikasi kuis Android berbasis Kotlin dan SQLite.
Aplikasi ini dirancang untuk pembelajaran interaktif dengan sistem skor, feedback visual, dan leaderboard.
Cocok untuk:
- Project kuliah
- Portofolio Android Developer
- Latihan SQLite & UI Android

✨ Fitur Utama
🎯 Gameplay
- 10 soal pilihan ganda per sesi
- Soal diacak dari database
- Feedback visual (🟢 benar, 🔴 salah)
- Skor realtime & transisi halus
👤 Player
- Input nama (sekali saja)
- Riwayat skor tersimpan
- Bisa main ulang tanpa input ulang
🏆 Leaderboard
- Top 10 skor tertinggi
- Medali 🥇🥈🥉 untuk 3 besar
- Reset skor dengan konfirmasi
🎨 UI/UX
- Material Design 3
- Splash Screen
- Layout responsif

🛠️ Teknologi
- Language: Kotlin
- Database: SQLite
- UI: Material Design 3, ViewBinding
- Architecture: MVC
- Min SDK: 26 (Android 8.0)

📂 Struktur Project
app/
 ├── activity/
 │   ├── SplashActivity.kt
 │   ├── MainActivity.kt
 │   ├── QuizActivity.kt
 │   └── ResultActivity.kt
 ├── database/
 │   ├── DatabaseHelper.kt
 │   └── QuizContract.kt
 ├── model/
 │   ├── Question.kt
 │   └── Score.kt
 └── res/
     ├── layout/
     ├── drawable/
     └── values/

🗄️ Database
Table: questions
- question
- option_a, option_b, option_c, option_d
- correct_answer
- category
Table: scores
- player_name
- score
- date

🎮 Cara Bermain
- Jalankan aplikasi
- Masukkan nama
- Jawab 10 soal
- Lihat skor akhir
- Cek leaderboard

🔧 Rencana Pengembangan
- Timer soal
- Pilih kategori & tingkat kesulitan
- Dark Mode
- Online leaderboard (Firebase)
- Achievement & badge
