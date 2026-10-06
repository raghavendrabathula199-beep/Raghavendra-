package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = DarkTerracottaPrimary,
  onPrimary = TerracottaOnContainer,
  primaryContainer = DarkTerracottaContainer,
  onPrimaryContainer = TerracottaContainer,
  secondary = DarkSlateSecondary,
  onSecondary = SlateOnContainer,
  secondaryContainer = DarkSlateContainer,
  onSecondaryContainer = SlateContainer,
  tertiary = DarkAmberTertiary,
  onTertiary = AmberOnContainer,
  tertiaryContainer = DarkAmberContainer,
  onTertiaryContainer = AmberContainer,
  background = DarkConstructionBackground,
  onBackground = DarkConstructionOnSurface,
  surface = DarkConstructionSurface,
  onSurface = DarkConstructionOnSurface,
  surfaceVariant = DarkConstructionSurfaceVariant,
  onSurfaceVariant = DarkConstructionOnSurface,
  outline = DarkConstructionOutline
)

private val LightColorScheme = lightColorScheme(
  primary = TerracottaPrimary,
  onPrimary = TerracottaOnPrimary,
  primaryContainer = TerracottaContainer,
  onPrimaryContainer = TerracottaOnContainer,
  secondary = SlateSecondary,
  onSecondary = SlateOnSecondary,
  secondaryContainer = SlateContainer,
  onSecondaryContainer = SlateOnContainer,
  tertiary = AmberTertiary,
  onTertiary = AmberOnTertiary,
  tertiaryContainer = AmberContainer,
  onTertiaryContainer = AmberOnContainer,
  background = ConstructionBackground,
  onBackground = ConstructionOnSurface,
  surface = ConstructionSurface,
  onSurface = ConstructionOnSurface,
  surfaceVariant = ConstructionSurfaceVariant,
  onSurfaceVariant = ConstructionOnSurfaceVariant,
  outline = ConstructionOutline
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep tailored construction theme dominant
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
