package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.TimeOfDayAtmosphere
import java.util.Calendar

// Brand Munchyroll Accent Colors
val MunchyOrange = Color(0xFFFF640A)
val MunchyOrangeLight = Color(0xFFFF8B3D)
val MunchyOrangeDark = Color(0xFFCC4E00)
val MunchyGold = Color(0xFFFFB800)
val MunchyPurple = Color(0xFF7C4DFF)
val MunchySky = Color(0xFF00B0FF)

// Core Dark Surfaces
val SurfaceDark = Color(0xFF0F0F13)
val SurfaceCard = Color(0xFF181820)
val SurfaceCardHover = Color(0xFF22222E)
val SurfaceBorder = Color(0xFF2A2A38)
val TextPrimary = Color(0xFFF5F5F7)
val TextSecondary = Color(0xFFA1A1AA)
val TextTertiary = Color(0xFF71717A)

// Time Responsive Background Palettes
object AtmospherePalettes {

    // Morning (05:00 - 11:59): Warm Sunlit Amber & Golden Dawn
    val MorningGradient = listOf(
        Color(0xFF23180C),
        Color(0xFF17110A),
        Color(0xFF0E0B08)
    )
    val MorningAccent = Color(0xFFFFB300)
    val MorningTint = Color(0xFF38250A)

    // Afternoon (12:00 - 17:59): Radiant Sky & Azure Cobalt Midday
    val AfternoonGradient = listOf(
        Color(0xFF0D1B2A),
        Color(0xFF0A1420),
        Color(0xFF070B12)
    )
    val AfternoonAccent = Color(0xFF00A8E8)
    val AfternoonTint = Color(0xFF0C2B42)

    // Sunset / Evening (18:00 - 20:59): Faded Coral & Rose Dusk
    val SunsetGradient = listOf(
        Color(0xFF260D1A),
        Color(0xFF1B0A14),
        Color(0xFF10070D)
    )
    val SunsetAccent = Color(0xFFFF4D6D)
    val SunsetTint = Color(0xFF3B1226)

    // Night (21:00 - 04:59): Deep Starry Nebula & Midnight Amethyst
    val NightGradient = listOf(
        Color(0xFF0E0B19),
        Color(0xFF0A0713),
        Color(0xFF06050A)
    )
    val NightAccent = Color(0xFF9D4EDD)
    val NightTint = Color(0xFF1D1438)

    fun getAtmosphereBrush(atmosphere: TimeOfDayAtmosphere): Brush {
        val colors = when (resolveAtmosphere(atmosphere)) {
            TimeOfDayAtmosphere.MORNING -> MorningGradient
            TimeOfDayAtmosphere.AFTERNOON -> AfternoonGradient
            TimeOfDayAtmosphere.SUNSET -> SunsetGradient
            TimeOfDayAtmosphere.NIGHT -> NightGradient
            TimeOfDayAtmosphere.AUTO -> getDeviceAtmosphereColors()
        }
        return Brush.verticalGradient(colors)
    }

    fun getAtmosphereAccent(atmosphere: TimeOfDayAtmosphere): Color {
        return when (resolveAtmosphere(atmosphere)) {
            TimeOfDayAtmosphere.MORNING -> MorningAccent
            TimeOfDayAtmosphere.AFTERNOON -> AfternoonAccent
            TimeOfDayAtmosphere.SUNSET -> SunsetAccent
            TimeOfDayAtmosphere.NIGHT -> NightAccent
            TimeOfDayAtmosphere.AUTO -> MorningAccent
        }
    }

    fun resolveAtmosphere(selected: TimeOfDayAtmosphere): TimeOfDayAtmosphere {
        if (selected != TimeOfDayAtmosphere.AUTO) return selected
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> TimeOfDayAtmosphere.MORNING
            in 12..17 -> TimeOfDayAtmosphere.AFTERNOON
            in 18..20 -> TimeOfDayAtmosphere.SUNSET
            else -> TimeOfDayAtmosphere.NIGHT
        }
    }

    private fun getDeviceAtmosphereColors(): List<Color> {
        return when (resolveAtmosphere(TimeOfDayAtmosphere.AUTO)) {
            TimeOfDayAtmosphere.MORNING -> MorningGradient
            TimeOfDayAtmosphere.AFTERNOON -> AfternoonGradient
            TimeOfDayAtmosphere.SUNSET -> SunsetGradient
            else -> NightGradient
        }
    }
}
