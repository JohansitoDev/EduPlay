package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SoundManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    var isSoundEnabled = true

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Configurar idioma español para todos los sonidos y lectura de voz
                tts?.language = Locale.forLanguageTag("es-ES")
                tts?.setPitch(1.1f) // Tono amigable para niños
                tts?.setSpeechRate(0.9f) // Ritmo claro y pausado
                isInitialized = true
            }
        }
    }

    fun speak(text: String) {
        if (!isSoundEnabled || !isInitialized) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "EduPlaySpeechEs")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
