package com.example.quizgame.database

import android.provider.BaseColumns  // ✅ IMPORT INI WAJIB

object QuizContract {

    // ✅ Tambahkan ": BaseColumns" untuk inherit _ID
    object QuestionsTable : BaseColumns {
        const val TABLE_NAME = "questions"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_OPTION_A = "option_a"
        const val COLUMN_OPTION_B = "option_b"
        const val COLUMN_OPTION_C = "option_c"
        const val COLUMN_OPTION_D = "option_d"
        const val COLUMN_CORRECT_ANSWER = "correct_answer"
        const val COLUMN_CATEGORY = "category"
        // _ID otomatis tersedia dari BaseColumns
    }

    // ✅ Tambahkan ": BaseColumns" untuk inherit _ID
    object ScoresTable : BaseColumns {
        const val TABLE_NAME = "scores"
        const val COLUMN_PLAYER_NAME = "player_name"
        const val COLUMN_SCORE = "score"
        const val COLUMN_DATE = "date"
        // _ID otomatis tersedia dari BaseColumns
    }
}