package com.kakaanime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Bg = Color(0xFF121113)
private val SurfaceDark = Color(0xFF1C1A1D)
private val Purple = Color(0xFFD69AFF)
private val Pink = Color(0xFFFFB4C0)

private data class Anime(
    val id: String,
    val title: String,
    val score: String,
    val status: String,
    val episodes: Int,
    val genres: String,
    val synopsis: String,
    val poster: String,
    val latest: Int
)

private val animeList = listOf(
    Anime("one-piece", "One Piece", "9.0", "ONGOING", 1140, "Action • Adventure • Fantasy", "Monkey D. Luffy and his crew sail the Grand Line in search of the legendary One Piece.", "https://cdn.myanimelist.net/images/anime/1244/138851.jpg", 1140),
    Anime("solo-leveling", "Solo Leveling", "8.8", "FINISHED", 25, "Action • Fantasy • Adventure", "Sung Jin-Woo starts as the weakest hunter and gains a mysterious system that lets him grow without limit.", "https://cdn.myanimelist.net/images/anime/1708/139457.jpg", 25),
    Anime("jujutsu-kaisen", "Jujutsu Kaisen", "8.6", "ONGOING", 48, "Action • Supernatural", "Yuji Itadori is drawn into the world of cursed spirits after swallowing a dangerous cursed object.", "https://cdn.myanimelist.net/images/anime/1171/109222.jpg", 48),
    Anime("demon-slayer", "Demon Slayer", "8.6", "ONGOING", 63, "Action • Historical • Fantasy", "Tanjiro joins the Demon Slayer Corps after a tragedy changes his family forever.", "https://cdn.myanimelist.net/images/anime/1286/99889.jpg", 63)
)

private enum class Destination { HOME, CALENDAR, SOCIAL, LIBRARY, PROFILE }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KakaAnimeApp() }
    }
}

@Composable
private fun KakaAnimeApp() {
    var destination by remember { mutableStateOf(Destination.HOME) }
    var selectedAnime by remember { mutableStateOf<Anime?>(null) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Bg) {
            if (selectedAnime != null) {
                AnimeDetailScreen(anime = selectedAnime!!, onBack = { selectedAnime = null })
            } else {
                Scaffold(
                    containerColor = Bg,
                    bottomBar = {
                        KakaBottomBar(destination = destination, onDestination = { destination = it })
                    }
                ) { padding ->
                    when (destination) {
                        Destination.HOME -> HomeScreen(modifier = Modifier.padding(padding), onAnimeClick = { selectedAnime = it })
                        else -> PlaceholderScreen(
                            modifier = Modifier.padding(padding),
                            title = when (destination) {
                                Destination.CALENDAR -> "Calendar"
                                Destination.SOCIAL -> "Social"
                                Destination.LIBRARY -> "Library"
                                Destination.PROFILE -> "Profile"
                                Destination.HOME -> "Home"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier, onAnimeClick: (Anime) -> Unit) {
    val featured = animeList.first()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item { HomeTopBar() }
        item { FeaturedSection(anime = featured, onClick = { onAnimeClick(featured) }) }
        item { SectionHeader("Continue Watching", "Lihat semua") }
        item { ContinueCard(anime = animeList[1], episode = 23, progress = 0.68f, onClick = { onAnimeClick(animeList[1]) }) }
        item { SectionHeader("New Episode", "Lihat semua") }
        item { AnimeRow(items = animeList.take(3), onAnimeClick = onAnimeClick) }
        item { SectionHeader("Ongoing", "Lihat semua") }
        item { AnimeRow(items = animeList.filter { it.status == "ONGOING" }, onAnimeClick = onAnimeClick) }
        item { SectionHeader("Anime Tamat", "Lihat semua") }
        item { AnimeRow(items = animeList.filter { it.status == "FINISHED" }, onAnimeClick = onAnimeClick) }
    }
}

@Composable
private fun HomeTopBar() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text("KakaAnime", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Text("Temukan anime favoritmu", color = Color.White.copy(alpha = .58f), fontSize = 13.sp)
        }
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications", tint = Color.White)
        }
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
        }
    }
}

@Composable
private fun FeaturedSection(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(285.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(model = anime.poster, contentDescription = null, modifier = Modifier.fillMaxSize().alpha(.32f), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = .25f), Color.Black.copy(alpha = .58f), Bg.copy(alpha = .98f)))))
            Row(modifier = Modifier.fillMaxSize().padding(18.dp), verticalAlignment = Alignment.Bottom) {
                AsyncImage(model = anime.poster, contentDescription = anime.title, modifier = Modifier.width(132.dp).height(190.dp).clip(RoundedCornerShape(18.dp)), contentScale = ContentScale.Crop)
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f).padding(bottom = 4.dp)) {
                    Text("FEATURED", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Text(anime.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(6.dp))
                    Text(anime.status, color = Pink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Purple, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(anime.score, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(anime.genres, color = Color.White.copy(alpha = .72f), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
        Text(action, color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ContinueCard(anime: Anime, episode: Int, progress: Float, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(width = 116.dp, height = 74.dp).clip(RoundedCornerShape(14.dp))) {
                AsyncImage(model = anime.poster, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = .28f)), contentAlignment = Alignment.Center) {
                    Surface(shape = RoundedCornerShape(50), color = Purple.copy(alpha = .92f), modifier = Modifier.size(38.dp)) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.Black, modifier = Modifier.padding(8.dp))
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(anime.title, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Episode $episode", color = Color.White.copy(alpha = .6f), fontSize = 12.sp)
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = .12f))) {
                    Box(modifier = Modifier.fillMaxWidth(progress).height(4.dp).clip(RoundedCornerShape(8.dp)).background(Pink))
                }
            }
        }
    }
}

@Composable
private fun AnimeRow(items: List<Anime>, onAnimeClick: (Anime) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { anime -> AnimeCard(anime = anime, onClick = { onAnimeClick(anime) }) }
    }
}

@Composable
private fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    Column(modifier = Modifier.width(124.dp).clickable(onClick = onClick)) {
        Box(modifier = Modifier.fillMaxWidth().height(176.dp).clip(RoundedCornerShape(16.dp))) {
            AsyncImage(model = anime.poster, contentDescription = anime.title, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Surface(color = Purple, shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp), modifier = Modifier.align(Alignment.BottomEnd)) {
                Text("★ ${anime.score}", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(anime.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("Ep ${anime.latest}", color = Color.White.copy(alpha = .55f), fontSize = 11.sp)
    }
}

@Composable
private fun AnimeDetailScreen(anime: Anime, onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 30.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(390.dp)) {
                AsyncImage(model = anime.poster, contentDescription = null, modifier = Modifier.fillMaxSize().alpha(.4f), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Bg.copy(alpha = .25f), Bg))))
                IconButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp, start = 10.dp)) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 10.dp)) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = "More", tint = Color.White)
                }
                AsyncImage(model = anime.poster, contentDescription = anime.title, modifier = Modifier.width(150.dp).height(218.dp).align(Alignment.BottomStart).padding(start = 20.dp, bottom = 12.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop)
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(anime.title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(anime.status, color = Pink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("  •  ", color = Color.White.copy(alpha = .4f))
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Purple, modifier = Modifier.size(15.dp))
                    Text(" ${anime.score}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AssistChip(onClick = {}, label = { Text("Favorite") }, leadingIcon = { Icon(Icons.Outlined.BookmarkBorder, null) })
                    AssistChip(onClick = {}, label = { Text("Play") }, leadingIcon = { Icon(Icons.Filled.PlayArrow, null) })
                }
                Spacer(Modifier.height(18.dp))
                Text("Synopsis", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(7.dp))
                Text(anime.synopsis, color = Color.White.copy(alpha = .7f), lineHeight = 21.sp, fontSize = 14.sp)
                Spacer(Modifier.height(18.dp))
                Text("Genres", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Text(anime.genres, color = Purple, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(20.dp))
                Text("Episodes", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
            }
        }
        items((1..minOf(anime.episodes, 12)).toList()) { episode ->
            EpisodeRow(episode = episode, watched = episode < 4)
        }
    }
}

@Composable
private fun EpisodeRow(episode: Int, watched: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp).clip(RoundedCornerShape(15.dp)).background(SurfaceDark).padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(10.dp), color = if (watched) Purple.copy(alpha = .2f) else Color.White.copy(alpha = .08f), modifier = Modifier.size(42.dp)) {
            Box(contentAlignment = Alignment.Center) { Text("$episode", color = if (watched) Purple else Color.White, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Episode $episode", color = Color.White, fontWeight = FontWeight.Bold)
            Text(if (watched) "Sudah ditonton" else "Belum ditonton", color = Color.White.copy(alpha = .5f), fontSize = 11.sp)
        }
        Icon(Icons.Filled.PlayArrow, contentDescription = "Play episode", tint = Purple)
    }
}

@Composable
private fun KakaBottomBar(destination: Destination, onDestination: (Destination) -> Unit) {
    NavigationBar(containerColor = Color(0xFF1B191C), modifier = Modifier.navigationBarsPadding()) {
        val items = listOf(
            Triple(Destination.HOME, Icons.Filled.Home, "Home"),
            Triple(Destination.CALENDAR, Icons.Filled.CalendarMonth, "Calendar"),
            Triple(Destination.SOCIAL, Icons.Filled.FavoriteBorder, "Social"),
            Triple(Destination.LIBRARY, Icons.Filled.LibraryBooks, "Library"),
            Triple(Destination.PROFILE, Icons.Filled.Person, "Profile")
        )
        items.forEach { (dest, icon, label) ->
            NavigationBarItem(selected = destination == dest, onClick = { onDestination(dest) }, icon = { Icon(icon, contentDescription = label) }, label = { Text(label, fontSize = 10.sp) })
        }
    }
}

@Composable
private fun PlaceholderScreen(modifier: Modifier, title: String) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Text("Screen berikutnya akan dimigrasikan setelah Home + Detail.", color = Color.White.copy(alpha = .55f), fontSize = 13.sp)
        }
    }
}