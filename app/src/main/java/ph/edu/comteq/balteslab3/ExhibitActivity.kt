package ph.edu.comteq.balteslab3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme
import java.io.BufferedReader
import java.io.InputStreamReader

data class ArtworkInfo(
    val title: String,
    val years: String,
    val born_at: String,
    val comment: String,
    val imageRes: Int
)

class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaltesLab3Theme {
                ExhibitScreen()
            }
        }
    }
}

// Read JSON in assets
suspend fun loadArtworksFromAssets(context: Context): List<ArtworkInfo> {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.assets.open("artworks.json")
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            List(jsonArray.length()) { i ->
                val obj = jsonArray.getJSONObject(i)
                val title = obj.getString("title")
                val imageRes = when (title) {
                    "Mona Lisa" -> R.drawable.mona_lisa
                    "Lady Ermine" -> R.drawable.lady_ermine
                    "Litta Madonna" -> R.drawable.litta_madonna
                    else -> R.drawable.mona_lisa
                }
                ArtworkInfo(
                    title = title,
                    years = obj.getString("years"),
                    born_at = obj.getString("born_at"),
                    comment = obj.getString("comment"),
                    imageRes = imageRes
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

// Main Content with Looping Carousel
@Composable
fun ExhibitScreen() {
    val context = LocalContext.current
    var artworks by remember { mutableStateOf<List<ArtworkInfo>?>(null) }

    // Load JSON
    LaunchedEffect(Unit) {
        artworks = loadArtworksFromAssets(context)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
    ) {
        artworks?.let { artworkList ->
            LoopingCarouselScreen(artworks = artworkList)
        } ?: run {
            // Show loading
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD4AF37))
            }
        }
    }
}

@Composable
fun LoopingCarouselScreen(artworks: List<ArtworkInfo>) {
    var currentIndex by remember { mutableStateOf(0) }
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // Convert Dp to pixels using LocalDensity
    val density = LocalDensity.current
    val cardWidthPx = remember(density) { with(density) { 320.dp.toPx() } }
    val cardWidth = 320.dp
    val cardHeight = 420.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 18.dp, end = 18.dp, bottom = 18.dp)
    ) {
        // Carousel Area with LOOPING
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -80f) {
                                    // Swiped LEFT - go to NEXT (LOOPING)
                                    offsetX.animateTo(-cardWidthPx, tween(200))
                                    currentIndex = (currentIndex + 1) % artworks.size // LOOP
                                    offsetX.snapTo(cardWidthPx)
                                    offsetX.animateTo(0f, tween(300))
                                } else if (offsetX.value > 80f) {
                                    // Swiped RIGHT - go to PREVIOUS (LOOPING)
                                    offsetX.animateTo(cardWidthPx, tween(200))
                                    currentIndex = (currentIndex - 1 + artworks.size) % artworks.size // LOOP
                                    offsetX.snapTo(-cardWidthPx)
                                    offsetX.animateTo(0f, tween(300))
                                } else {
                                    offsetX.animateTo(0f, tween(200))
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Calculate indices for LOOPING carousel
            val prevIndex = (currentIndex - 1 + artworks.size) % artworks.size
            val nextIndex = (currentIndex + 1) % artworks.size

            // Previous item (left side)
            ArtworkCard(
                artwork = artworks[prevIndex],
                offsetX = -cardWidthPx * 0.6f + offsetX.value,
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                isSideItem = true,
                isLeft = true,
                isCurrent = false
            )

            // Current item (center)
            ArtworkCard(
                artwork = artworks[currentIndex],
                offsetX = offsetX.value,
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                isSideItem = false,
                isLeft = false,
                isCurrent = true
            )

            // Next item (right side)
            ArtworkCard(
                artwork = artworks[nextIndex],
                offsetX = cardWidthPx * 0.6f + offsetX.value,
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                isSideItem = true,
                isLeft = false,
                isCurrent = false
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Artwork Info
        val currentArtwork = artworks[currentIndex]
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = currentArtwork.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${currentArtwork.years}, ${currentArtwork.born_at}",
                color = Color(0xFFD4AF37),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Comment section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            // Quote Image
            Image(
                painter = painterResource(id = R.drawable.quote),
                contentDescription = "Comment",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(50.dp),
                alpha = 0.2f
            )

            // Comment Quote
            Text(
                text = currentArtwork.comment,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 35.dp, top = 40.dp, end = 20.dp)
            )
        }
    }
}

@Composable
fun ArtworkCard(
    artwork: ArtworkInfo,
    offsetX: Float,
    cardWidth: Dp,
    cardHeight: Dp,
    isSideItem: Boolean = false,
    isLeft: Boolean = false,
    isCurrent: Boolean = false
) {
    val density = LocalDensity.current
    val cardWidthPx = with(density) { cardWidth.toPx() }

    // Check if this is Mona Lisa or Litta Madonna (needs REVERSED design)
    val isReversedDesign = artwork.title == "Mona Lisa" || artwork.title == "Litta Madonna"
    // Check if this is Lady Ermine (needs ORIGINAL design)
    val isLadyErmine = artwork.title == "Lady Ermine"

    // Calculate scale and alpha
    val scale = if (isCurrent) 1f else 0.85f
    val alpha = if (isCurrent) 1f else 0.7f

    // Side offset for half-visible items
    val sideOffset = if (isSideItem) {
        if (isLeft) -cardWidthPx * 0.4f else cardWidthPx * 0.4f
    } else 0f

    // Determine card shape based on artwork
    val cardShape = when {
        isReversedDesign && isCurrent -> {
            // REVERSED: Mona Lisa & Litta Madonna (curve at BOTTOM)
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 200.dp,
                bottomEnd = 200.dp
            )
        }
        isLadyErmine && isCurrent -> {
            // ORIGINAL: Lady Ermine (curve at TOP)
            RoundedCornerShape(
                topStart = 200.dp,
                topEnd = 200.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        }
        else -> {
            // For side items or non-current, use default
            RoundedCornerShape(
                topStart = 200.dp,
                topEnd = 200.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        }
    }

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .graphicsLayer {
                translationX = sideOffset + offsetX
                scaleX = scale
                scaleY = scale
                this.alpha = alpha

                // Add depth with rotation for side items
                if (isSideItem) {
                    rotationY = if (isLeft) 10f else -10f
                }
            },
        shape = cardShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrent) 12.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isReversedDesign && isCurrent) {
                // REVERSED DESIGN for MONA LISA & LITTA MADONNA
                // 1. Yellow info bar on TOP (flat)
                YellowInfoBar(artwork = artwork, isTop = true)

                // 2. Artwork image BELOW with CURVE AT BOTTOM
                Image(
                    painter = painterResource(id = artwork.imageRes),
                    contentDescription = artwork.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 200.dp,
                                bottomEnd = 200.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            } else if (isLadyErmine && isCurrent) {
                // ORIGINAL DESIGN for LADY ERMINE
                // 1. Artwork image on TOP with CURVE AT TOP
                Image(
                    painter = painterResource(id = artwork.imageRes),
                    contentDescription = artwork.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 200.dp,
                                topEnd = 200.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                // 2. Yellow info bar at BOTTOM (flat)
                YellowInfoBar(artwork = artwork, isTop = false)
            } else {
                // For side items or non-current artworks, use original design
                // 1. Artwork image on TOP with curve
                Image(
                    painter = painterResource(id = artwork.imageRes),
                    contentDescription = artwork.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 200.dp,
                                topEnd = 200.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                // 2. Yellow info bar at BOTTOM
                YellowInfoBar(artwork = artwork, isTop = false)
            }
        }
    }
}

@Composable
fun YellowInfoBar(artwork: ArtworkInfo, isTop: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFFD4AF37))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = artwork.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = "${artwork.years}, ${artwork.born_at}",
                fontSize = 14.sp,
                color = Color(0xFF3D3D3D),
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Info Icon",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExhibitPreview() {
    BaltesLab3Theme {
        val sampleArtworks = listOf(
            ArtworkInfo(
                title = "Mona Lisa",
                years = "1503-1506",
                born_at = "Louvre Museum, Paris",
                comment = "The Mona Lisa is a half-length portrait painting by Italian artist Leonardo da Vinci. Considered an archetypal masterpiece of the Italian Renaissance, it has been described as 'the best known, the most visited, the most written about, the most sung about, the most parodied work of art in the world'.",
                imageRes = R.drawable.mona_lisa
            ),
            ArtworkInfo(
                title = "Lady Ermine",
                years = "1489-1490",
                born_at = "Czartoryski Museum, Kraków",
                comment = "Lady with an Ermine is a portrait painting widely attributed to the Italian Renaissance artist Leonardo da Vinci. Dated to c. 1489–1491, the work is painted in oils on a panel of walnut wood.",
                imageRes = R.drawable.lady_ermine
            ),
            ArtworkInfo(
                title = "Litta Madonna",
                years = "1490",
                born_at = "Hermitage Museum, Saint Petersburg",
                comment = "The Madonna Litta is a late 15th-century painting, traditionally attributed to Leonardo da Vinci, in the Hermitage Museum, Saint Petersburg. It depicts the Virgin Mary breastfeeding the Christ child.",
                imageRes = R.drawable.litta_madonna
            )
        )

        LoopingCarouselScreen(artworks = sampleArtworks)
    }
}