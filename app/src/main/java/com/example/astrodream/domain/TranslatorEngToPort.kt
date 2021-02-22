package com.example.astrodream.domain

import com.example.astrodream.utils.TranslationEnglishToPortuguese

object TranslatorEngToPort: TranslationEnglishToPortuguese() {
    init {
        modelDownload()
    }
}