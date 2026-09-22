package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.TimeOfDayAtmosphere
import com.example.ui.theme.AtmospherePalettes

@Composable
fun AtmosphericBackground(
    atmosphere: TimeOfDayAtmosphere,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val resolved = AtmospherePalettes.resolveAtmosphere(atmosphere)
    val targetColors = when (resolved) {
        TimeOfDayAtmosphere.MORNING -> AtmospherePalettes.MorningGradient
        TimeOfDayAtmosphere.AFTERNOON -> AtmospherePalettes.AfternoonGradient
        TimeOfDayAtmosphere.SUNSET -> AtmospherePalettes.SunsetGradient
        TimeOfDayAtmosphere.NIGHT -> AtmospherePalettes.NightGradient
        TimeOfDayAtmosphere.AUTO -> AtmospherePalettes.MorningGradient
    }

    val colorTop by animateColorAsState(
        targetValue = targetColors[0],
        animationSpec = tween(durationMillis = 600),
        label = "AtmosphereTop"
    )
    val colorMid by animateColorAsState(
        targetValue = targetColors[1],
        animationSpec = tween(durationMillis = 600),
        label = "AtmosphereMid"
    )
    val colorBottom by animateColorAsState(
        targetValue = targetColors[2],
        animationSpec = tween(durationMillis = 600),
        label = "AtmosphereBottom"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorTop, colorMid, colorBottom)
                )
            )
    ) {
        content()
    }
}
