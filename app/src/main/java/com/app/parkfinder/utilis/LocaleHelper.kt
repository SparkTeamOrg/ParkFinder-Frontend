package com.app.parkfinder.utilis

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    // Set the language of the app
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        context.resources.apply {
            val config = Configuration(configuration)

            context.createConfigurationContext(config)
            Locale.setDefault(locale)
            config.setLocale(locale)

            @Suppress("DEPRECATION")
            updateConfiguration(config, displayMetrics)

            return context
        }
    }

}