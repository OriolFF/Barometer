package com.uriolus.barometer.features.realtime.presentation.digital

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.uriolus.barometer.R

val fontName = GoogleFont("Press Start 2P")

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = googleFontProvider
    )
)
