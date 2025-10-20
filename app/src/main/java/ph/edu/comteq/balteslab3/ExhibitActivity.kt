package ph.edu.comteq.balteslab3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme
import java.io.BufferedReader
import java.io.InputStreamReader

data class ArtworkInfo(
    val title: String,
    val years: String,
    val born_at: String,
    val comment: String
)

class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val artworkRes = intent.getIntExtra("artworkRes", R.drawable.mona_lisa)
        val artistName = intent.getStringExtra("artistName") ?: "Leonardo da Vinci"

        setContent {
            BaltesLab3Theme {
                ExhibitScreen(artworkRes = artworkRes, artistName = artistName)
            }
        }
    }
}

// Read JSON in assets
suspend fun loadArtworksFromAssets(context: Context): List<ArtworkInfo> {
    return withContext(Dispatchers.IO) {
        val inputStream = context.assets.open("artworks.json")
        val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        List(jsonArray.length()) { i ->
            val obj = jsonArray.getJSONObject(i)
            ArtworkInfo(
                title = obj.getString("title"),
                years = obj.getString("years"),
                born_at = obj.getString("born_at"),
                comment = obj.getString("comment")
            )
        }
    }
}

// Main Content
@Composable
fun ExhibitScreen(artworkRes: Int, artistName: String) {
    val context = LocalContext.current
    var artworkInfo by remember { mutableStateOf<ArtworkInfo?>(null) }

    // Load JSON
    LaunchedEffect(Unit) {
        val list = loadArtworksFromAssets(context)
        // Pick based on artworkRes
        artworkInfo = when (artworkRes) {
            R.drawable.mona_lisa -> list.find { it.title == "Mona Lisa" }
            R.drawable.lady_ermine -> list.find { it.title == "Lady Ermine" }
            R.drawable.litta_madonna -> list.find { it.title == "Litta Madonna" }
            else -> list.firstOrNull()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, start = 18.dp, end = 18.dp, bottom = 18.dp)
        ) {
            // Artwork image
            Image(
                painter = painterResource(id = artworkRes),
                contentDescription = "Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(topStart = 200.dp, topEnd = 200.dp)),
                contentScale = ContentScale.Crop
            )

            // Yellow Info bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD4AF37))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = artworkInfo?.title ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = optima
                    )
                    Text(
                        text = "${artworkInfo?.years ?: ""}, ${artworkInfo?.born_at ?: ""}",
                        fontSize = 15.sp,
                        color = Color(0xFF3D3D3D),
                        fontFamily = optima
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Image Arrow
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Info Icon",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quote
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                // Quote Image
                Image(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Comment",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(60.dp),
                    alpha = 0.2f
                )

                // Comment Quote
                Text(
                    text = artworkInfo?.comment ?: "",
                    color = Color.White,
                    fontFamily = optima,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 40.dp, top = 55.dp, end = 20.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//  BaltesLab3Theme {
//    Greeting("Android")
//  }
//}
