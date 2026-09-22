package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.Anime
import com.example.ui.theme.MunchyGold
import com.example.ui.theme.MunchyOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun HeroSpotlightCarousel(
    featuredList: List<Anime>,
    onSelectAnime: (Anime) -> Unit,
    onQuickPlay: (Anime) -> Unit,
    onToggleWatchlist: (Anime) -> Unit,
    isInWatchlist: (Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    if (featuredList.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    // Auto rotate every 7 seconds
    LaunchedEffect(featuredList) {
        while (true) {
            delay(7000)
            if (featuredList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % featuredList.size
            }
        }
    }

    val currentAnime = featuredList.getOrNull(currentIndex) ?: featuredList.first()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .clickable { onSelectAnime(currentAnime) }
    ) {
        AnimatedContent(
            targetState = currentAnime,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "HeroCarousel"
        ) { anime ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(anime.bannerUrl.ifEmpty { anime.imageUrl })
                    .crossfade(true)
                    .build(),
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Multi-stop gradient overlay for deep dark cinematic contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Hero Info & Actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Pill with Category / Studio
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentAnime.studio.isNotEmpty()) {
                    Text(
                        text = currentAnime.studio.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MunchyOrange,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MunchyOrange.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Score",
                        tint = MunchyGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.2f", currentAnime.score),
                        style = MaterialTheme.typography.labelMedium,
                        color = MunchyGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                currentAnime.year?.let { y ->
                    Text(
                        text = "$y",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = currentAnime.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (currentAnime.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = currentAnime.genres.joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onQuickPlay(currentAnime) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MunchyOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .testTag("hero_stream_now_button")
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Stream",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Stream Ep 1",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                val isSaved = isInWatchlist(currentAnime.id)
                OutlinedButton(
                    onClick = { onToggleWatchlist(currentAnime) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isSaved) MunchyOrange else TextPrimary
                    ),
                    modifier = Modifier
                        .testTag("hero_watchlist_button")
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Watchlist",
                        tint = if (isSaved) MunchyOrange else TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSaved) "Added" else "Watchlist",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel Indicator Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                featuredList.take(6).forEachIndexed { index, _ ->
                    val isCurrent = index == currentIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .width(if (isCurrent) 18.dp else 6.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isCurrent) MunchyOrange else Color.White.copy(alpha = 0.3f))
                            .clickable { currentIndex = index }
                    )
                }
            }
        }
    }
}
