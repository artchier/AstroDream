package com.example.astrodream.utils

import android.util.Log
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslationEnglishToPortuguese {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.PORTUGUESE)
        .build()

    val englishPortugueseTranslator = Translation.getClient(options)

    fun modelDownload() {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishPortugueseTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.i("TranslationModel", "Model downloaded successfully. Okay to start translating.")
            }
            .addOnFailureListener { exception ->
                Log.i("TranslationModel", "Model couldn’t be downloaded or other internal error.")
            }
    }

    fun translateEnglishToPortuguese(text: String, view: TextView) {
        englishPortugueseTranslator.translate(text)
            .addOnSuccessListener { translatedText ->
                view.text = translatedText
            }
            .addOnFailureListener { exception ->
                Log.i("Translate", exception.toString())
                view.text = text
            }
    }
}