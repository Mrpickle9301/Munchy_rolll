package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.local.UserEntity
import com.example.data.local.WatchlistEntity
import com.example.data.model.Anime
import com.example.data.model.Episode
import com.example.data.model.TimeOfDayAtmosphere
import com.example.ui.components.AnimeCard
import com.example.ui.components.AtmosphereSelectorBar
import com.example.ui.components.HeroSpotlightCarousel
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.MunchyOrange
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHover
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    trendingAnime: List<Anime>,
    seasonalAnime: List<Anime>,
    topAnime: List<Anime>,
    watchlist: List<WatchlistEntity>,
    timeAtmosphere: TimeOfDayAtmosphere,
    onSelectAtmosphere: (TimeOfDayAtmosphere) -> Unit,
    onSelectAnime: (Anime) -> Unit,
    onQuickPlay: (Anime) -> Unit,
    onResumeWatchlist: (WatchlistEntity) -> Unit,
    onToggleWatchlist: (Anime) -> Unit,
    isInWatchlist: (Int) -> Boolean,
    onRefresh: () -> Unit,
    currentUser: UserEntity? = null,
    onUserAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // App Top Bar: Munchyroll Brand & Atmosphere Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MunchyOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Munchyroll",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "munchyroll",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MunchyOrange
                        )
                        Text(
                            text = "Ad-free anime experience",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .testTag("refresh_button")
                            .clip(CircleShape)
                            .background(SurfaceCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = TextPrimary
                        )
                    }

                    if (currentUser != null) {
                        UserAvatarView(
                            avatarId = currentUser.avatarId,
                            size = 36.dp,
                            iconSize = 18.dp,
                            onClick = onUserAvatarClick,
                            modifier = Modifier.testTag("top_user_avatar")
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .testTag("top_signin_button")
                                .clip(RoundedCornerShape(18.dp))
                                .background(MunchyOrange.copy(alpha = 0.2f))
                                .border(1.dp, MunchyOrange.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                                .clickable { onUserAvatarClick() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MunchyOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sign In",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MunchyOrange
                            )
                        }
                    }
                }
            }
        }

        // Time-responsive Atmosphere Bar
        item {
            AtmosphereSelectorBar(
                selectedAtmosphere = timeAtmosphere,
                onSelectAtmosphere = onSelectAtmosphere
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Spotlight Hero Carousel
        item {
            HeroSpotlightCarousel(
                featuredList = trendingAnime.take(5),
                onSelectAnime = onSelectAnime,
                onQuickPlay = onQuickPlay,
                onToggleWatchlist = onToggleWatchlist,
                isInWatchlist = isInWatchlist,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Continue Watching (Room DB Integration)
        val watchingEntries = watchlist.filter { it.status == "WATCHING" }
        if (watchingEntries.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONTINUE WATCHING",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = TextPrimary
                        )
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(watchingEntries, key = { it.animeId }) { entry ->
                            ContinueWatchingCard(
                                entry = entry,
                                onResume = { onResumeWatchlist(entry) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Section: Airing This Season
        item {
            SectionHeader(title = "CURRENTLY AIRING", subtitle = "Latest episodes & simulcasts")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(seasonalAnime, key = { it.id }) { anime ->
                    AnimeCard(anime = anime, onClick = { onSelectAnime(anime) })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Trending & Popular
        item {
            SectionHeader(title = "TRENDING NOW", subtitle = "Most watched in community")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trendingAnime, key = { it.id }) { anime ->
                    AnimeCard(anime = anime, onClick = { onSelectAnime(anime) })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Top Rated Masterpieces
        item {
            SectionHeader(title = "TOP RATED MASTERPIECES", subtitle = "Highest scores on MyAnimeList")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(topAnime, key = { it.id }) { anime ->
                    AnimeCard(anime = anime, onClick = { onSelectAnime(anime) })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = TextPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun ContinueWatchingCard(
    entry: WatchlistEntity,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressPercent = if (entry.totalDurationMs > 0) {
        (entry.progressMs.toFloat() / entry.totalDurationMs.toFloat()).coerceIn(0.1f, 1f)
    } else 0.45f

    Card(
        modifier = modifier
            .width(220.dp)
            .testTag("continue_watching_${entry.animeId}")
            .clip(RoundedCornerShape(12.dp))
            .clickable { onResume() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(SurfaceCardHover)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(entry.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = entry.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MunchyOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Resume",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Progress Bar at bottom of thumbnail
                LinearProgressIndicator(
                    progress = { progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(3.dp),
                    color = MunchyOrange,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    ),
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Episode ${entry.currentEpisode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MunchyOrange,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
