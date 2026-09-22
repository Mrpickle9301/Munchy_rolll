package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimeOfDayAtmosphere
import com.example.ui.theme.AtmospherePalettes
import com.example.ui.theme.MunchyOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun AtmosphereSelectorBar(
    selectedAtmosphere: TimeOfDayAtmosphere,
    onSelectAtmosphere: (TimeOfDayAtmosphere) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val resolved = AtmospherePalettes.resolveAtmosphere(selectedAtmosphere)
    val accentColor = AtmospherePalettes.getAtmosphereAccent(selectedAtmosphere)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ATMOSPHERE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextSecondary
                )
            }

            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val timeString = String.format("%02d:00 Local", currentHour)
            Text(
                text = "${resolved.displayName} • $timeString",
                style = MaterialTheme.typography.bodySmall,
                color = accentColor,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeOfDayAtmosphere.values().forEach { atmosphere ->
                val isSelected = selectedAtmosphere == atmosphere
                val icon = when (atmosphere) {
                    TimeOfDayAtmosphere.AUTO -> Icons.Default.Schedule
                    TimeOfDayAtmosphere.MORNING -> Icons.Default.Brightness5
                    TimeOfDayAtmosphere.AFTERNOON -> Icons.Default.LightMode
                    TimeOfDayAtmosphere.SUNSET -> Icons.Default.WbTwilight
                    TimeOfDayAtmosphere.NIGHT -> Icons.Default.Bedtime
                }

                val itemAccent = when (atmosphere) {
                    TimeOfDayAtmosphere.AUTO -> MunchyOrange
                    TimeOfDayAtmosphere.MORNING -> AtmospherePalettes.MorningAccent
                    TimeOfDayAtmosphere.AFTERNOON -> AtmospherePalettes.AfternoonAccent
                    TimeOfDayAtmosphere.SUNSET -> AtmospherePalettes.SunsetAccent
                    TimeOfDayAtmosphere.NIGHT -> AtmospherePalettes.NightAccent
                }

                Row(
                    modifier = Modifier
                        .testTag("atmosphere_chip_${atmosphere.name.lowercase()}")
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) itemAccent.copy(alpha = 0.2f) else SurfaceCard.copy(alpha = 0.6f))
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) itemAccent else SurfaceBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onSelectAtmosphere(atmosphere) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = atmosphere.displayName,
                        tint = if (isSelected) itemAccent else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = atmosphere.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
