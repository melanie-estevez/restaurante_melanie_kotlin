package com.ute.restauranteapp.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoffeeColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color.White,

    primaryContainer = Surface2,
    onPrimaryContainer = TextPrimary,

    secondary = AccentLight,
    onSecondary = TextPrimary,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = Surface2,
    onSurfaceVariant = TextSecondary,

    outline = Border,
    outlineVariant = BorderLight,

    error = Error,
    onError = Color.White
)

@Composable
fun ShopAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}