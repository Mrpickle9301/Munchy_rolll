package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MunchyGold
import com.example.ui.theme.MunchyOrange
import com.example.ui.theme.MunchyPurple
import com.example.ui.theme.MunchySky

data class AvatarDescriptor(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val gradientColors: List<Color>
)

object AvatarRegistry {
    val avatars = listOf(
        AvatarDescriptor(
            id = "avatar_1",
            name = "Shinobi",
            icon = Icons.Default.LocalFireDepartment,
            gradientColors = listOf(MunchyOrange, Color(0xFFFF8C00))
        ),
        AvatarDescriptor(
            id = "avatar_2",
            name = "Sorcerer",
            icon = Icons.Default.Psychology,
            gradientColors = listOf(MunchyPurple, Color(0xFF4A148C))
        ),
        AvatarDescriptor(
            id = "avatar_3",
            name = "Mecha Pilot",
            icon = Icons.Default.Bolt,
            gradientColors = listOf(MunchySky, Color(0xFF0055FF))
        ),
        AvatarDescriptor(
            id = "avatar_4",
            name = "Titan Hunter",
            icon = Icons.Default.Shield,
            gradientColors = listOf(Color(0xFF00B0FF), MunchyGold)
        ),
        AvatarDescriptor(
            id = "avatar_5",
            name = "Alchemist",
            icon = Icons.Default.AutoAwesome,
            gradientColors = listOf(Color(0xFFFF3366), Color(0xFFFF9900))
        ),
        AvatarDescriptor(
            id = "avatar_6",
            name = "Spirit Beast",
            icon = Icons.Default.Pets,
            gradientColors = listOf(Color(0xFF00E676), Color(0xFF00B0FF))
        )
    )

    fun getAvatar(id: String): AvatarDescriptor {
        return avatars.firstOrNull { it.id == id } ?: avatars.first()
    }
}

@Composable
fun UserAvatarView(
    avatarId: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 26.dp,
    showBorder: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val descriptor = AvatarRegistry.getAvatar(avatarId)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(descriptor.gradientColors))
            .then(
                if (showBorder) {
                    Modifier.border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                } else Modifier
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = descriptor.icon,
            contentDescription = descriptor.name,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}
