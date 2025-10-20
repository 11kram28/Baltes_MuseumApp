package ph.edu.comteq.balteslab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme

class ArtistsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaltesLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RenaissanceArtExplorer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Artist(
    val id: Int,
    val name: String,
    val birthDeath: String,
    val avatarRes: Int,
    val artworks: List<Int>
)

@Composable
fun RenaissanceArtExplorer(modifier: Modifier = Modifier) {
    val artists = listOf(
        Artist(
            id = 1,
            name = "Leonardo da Vinci",
            birthDeath = "1452-1519",
            avatarRes = R.drawable.leonardo_da_vinci,
            artworks = listOf(
                R.drawable.mona_lisa,
                R.drawable.lady_ermine,
                R.drawable.litta_madonna
            )
        ),
        Artist(
            id = 2,
            name = "Michelangelo",
            birthDeath = "1475-1564",
            avatarRes = R.drawable.michelangelo,
            artworks = listOf(
                R.drawable.david,
                R.drawable.torment_of_saint_anthony,
                R.drawable.delphic_sibyl
            )
        ),
        Artist(
            id = 3,
            name = "Gustav Klimt",
            birthDeath = "1862-1918",
            avatarRes = R.drawable.gustav_klimt,
            artworks = listOf(
                R.drawable.the_kiss,
                R.drawable.lady_with_fan,
                R.drawable.adele_bloch_bauer
            )
        )
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Artists", "Artworks")

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Renaissance Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title 1
            Text(
                text = "Explore the art of ",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = playfairdisplayregular,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 0.dp)
            )
            // Title 2
            Text(
                text = "Renaissance",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = playfairdisplayregular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 45.sp,
                    color = Color(0XFFD4AF37)
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Search Box
            SearchBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Tabs - Simple with line
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTabIndex = index }
                        ) {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) Color(0xFFD4AF37) else Color.Black,
                                fontFamily = optima,
                                fontSize = 25.sp,
                                modifier = Modifier.padding(16.dp)
                            )

                            // Line under each tab - gold for selected, Black for others
                            Box(
                                modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    if (selectedTabIndex == index) Color(0xFFD4AF37)
                                    else Color.Black
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> ArtistsTab(artists = artists)
                1 -> ArtworksTab(artists = artists)
            }
        }
    }
}

@Composable
fun SearchBox(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.border(2.dp, Color.Black),
        color = Color.Gray.copy(alpha = 0.1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Icon
            Image(
                painter = painterResource(id = android.R.drawable.ic_search_category_default),
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Search Text Field
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Type to search...",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = optima
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Scan Icon
            Image(
                painter = painterResource(id = R.drawable.scan_removebg_preview),
                contentDescription = "Scan",
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )
        }
    }
}

@Composable
fun ArtistsTab(artists: List<Artist>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(artists) { artist ->
            ArtistItem(artist = artist)
        }
    }
}

@Composable
fun ArtistItem(artist: Artist, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isMichelangelo = artist.name == "Michelangelo"

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Artist Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isMichelangelo) Arrangement.End else Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            if (!isMichelangelo) {
                // Normal (Left-aligned)
                Image(
                    painter = painterResource(id = artist.avatarRes),
                    contentDescription = "${artist.name} avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            // Text Info (Name + Date)
            Column(
                horizontalAlignment = if (isMichelangelo) Alignment.End else Alignment.Start
            ) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = optima
                )
                Text(
                    text = artist.birthDeath,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontFamily = optima
                )
            }

            if (isMichelangelo) {
                Spacer(modifier = Modifier.width(16.dp))
                // Avatar in right
                Image(
                    painter = painterResource(id = artist.avatarRes),
                    contentDescription = "${artist.name} avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Artworks Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = isMichelangelo // Reverse artworks if Michelangelo
        ) {
            items(artist.artworks) { artworkRes ->
                val isLeonardo = artist.name == "Leonardo da Vinci"

                Image(
                    painter = painterResource(id = artworkRes),
                    contentDescription = "Artwork by ${artist.name}",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (isLeonardo) Modifier.clickable {
                                // Open ExhibitActivity and pass data
                                val intent = Intent(context, ExhibitActivity::class.java).apply {
                                    putExtra("artworkRes", artworkRes)
                                    putExtra("artistName", artist.name)
                                }
                                context.startActivity(intent)
                            } else Modifier
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}




@Composable
fun ArtworksTab(artists: List<Artist>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        artists.forEach { artist ->
            artist.artworks.forEach { artworkRes ->
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = artworkRes),
                            contentDescription = "Artwork",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = artist.name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RenaissanceArtExplorerPreview() {
    BaltesLab3Theme {
        RenaissanceArtExplorer()
    }
}