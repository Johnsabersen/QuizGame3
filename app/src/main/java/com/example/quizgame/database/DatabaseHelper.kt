package com.example.quizgame.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quizgame.model.Question
import com.example.quizgame.model.Score

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1

        // ✅ Ganti ${QuizContract.QuestionsTable._ID} dengan "_id"
        private val SQL_CREATE_QUESTIONS = """
            CREATE TABLE ${QuizContract.QuestionsTable.TABLE_NAME} (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                ${QuizContract.QuestionsTable.COLUMN_QUESTION} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_OPTION_A} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_OPTION_B} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_OPTION_C} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_OPTION_D} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_CORRECT_ANSWER} TEXT NOT NULL,
                ${QuizContract.QuestionsTable.COLUMN_CATEGORY} TEXT NOT NULL
            )
        """.trimIndent()

        // ✅ Ganti ${QuizContract.ScoresTable._ID} dengan "_id"
        private val SQL_CREATE_SCORES = """
            CREATE TABLE ${QuizContract.ScoresTable.TABLE_NAME} (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                ${QuizContract.ScoresTable.COLUMN_PLAYER_NAME} TEXT NOT NULL,
                ${QuizContract.ScoresTable.COLUMN_SCORE} INTEGER NOT NULL,
                ${QuizContract.ScoresTable.COLUMN_DATE} TEXT NOT NULL
            )
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_QUESTIONS)
        db?.execSQL(SQL_CREATE_SCORES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${QuizContract.QuestionsTable.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${QuizContract.ScoresTable.TABLE_NAME}")
        onCreate(db)
    }

    // ==================== QUESTIONS METHODS ====================

    fun insertQuestion(question: Question): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.question)
            put(QuizContract.QuestionsTable.COLUMN_OPTION_A, question.optionA)
            put(QuizContract.QuestionsTable.COLUMN_OPTION_B, question.optionB)
            put(QuizContract.QuestionsTable.COLUMN_OPTION_C, question.optionC)
            put(QuizContract.QuestionsTable.COLUMN_OPTION_D, question.optionD)
            put(QuizContract.QuestionsTable.COLUMN_CORRECT_ANSWER, question.correctAnswer)
            put(QuizContract.QuestionsTable.COLUMN_CATEGORY, question.category)
        }
        return db.insert(QuizContract.QuestionsTable.TABLE_NAME, null, values)
    }

    fun getAllQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            QuizContract.QuestionsTable.TABLE_NAME,
            null, null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val question = Question(
                    // ✅ Ganti dengan "_id" langsung
                    id = getInt(getColumnIndexOrThrow("_id")),
                    question = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_QUESTION)),
                    optionA = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_A)),
                    optionB = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_B)),
                    optionC = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_C)),
                    optionD = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_D)),
                    correctAnswer = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_CORRECT_ANSWER)),
                    category = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_CATEGORY))
                )
                questions.add(question)
            }
        }
        cursor.close()
        return questions
    }

    fun getRandomQuestions(limit: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${QuizContract.QuestionsTable.TABLE_NAME} ORDER BY RANDOM() LIMIT ?",
            arrayOf(limit.toString())
        )

        with(cursor) {
            while (moveToNext()) {
                val question = Question(
                    // ✅ Ganti dengan "_id" langsung
                    id = getInt(getColumnIndexOrThrow("_id")),
                    question = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_QUESTION)),
                    optionA = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_A)),
                    optionB = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_B)),
                    optionC = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_C)),
                    optionD = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_OPTION_D)),
                    correctAnswer = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_CORRECT_ANSWER)),
                    category = getString(getColumnIndexOrThrow(QuizContract.QuestionsTable.COLUMN_CATEGORY))
                )
                questions.add(question)
            }
        }
        cursor.close()
        return questions
    }

    // ==================== SCORES METHODS ====================

    fun insertScore(score: Score): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(QuizContract.ScoresTable.COLUMN_PLAYER_NAME, score.playerName)
            put(QuizContract.ScoresTable.COLUMN_SCORE, score.score)
            put(QuizContract.ScoresTable.COLUMN_DATE, score.date)
        }
        return db.insert(QuizContract.ScoresTable.TABLE_NAME, null, values)
    }

    fun getTopScores(limit: Int = 10): List<Score> {
        val scores = mutableListOf<Score>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${QuizContract.ScoresTable.TABLE_NAME} ORDER BY ${QuizContract.ScoresTable.COLUMN_SCORE} DESC LIMIT ?",
            arrayOf(limit.toString())
        )

        with(cursor) {
            while (moveToNext()) {
                val score = Score(
                    // ✅ Ganti dengan "_id" langsung
                    id = getInt(getColumnIndexOrThrow("_id")),
                    playerName = getString(getColumnIndexOrThrow(QuizContract.ScoresTable.COLUMN_PLAYER_NAME)),
                    score = getInt(getColumnIndexOrThrow(QuizContract.ScoresTable.COLUMN_SCORE)),
                    date = getString(getColumnIndexOrThrow(QuizContract.ScoresTable.COLUMN_DATE))
                )
                scores.add(score)
            }
        }
        cursor.close()
        return scores
    }
}