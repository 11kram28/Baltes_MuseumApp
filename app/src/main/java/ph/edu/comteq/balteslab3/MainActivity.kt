package ph.edu.comteq.balteslab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.disableHotReloadMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme


val playfairdisplayregular = FontFamily(
    Font(R.font.playfairdisplayregular, FontWeight.Normal)
)


val optima = FontFamily(
    Font(R.font.optima, FontWeight.Normal)
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaltesLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Homepage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                horizontal = 5.dp, // Adds padding on the left and right.
                vertical = 60.dp // Adds a good amount of padding on the top and bottom.
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Logo
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Gallery Logo",
            tint = Color(0xFFFFC107),
            modifier = Modifier
                .size(75.dp)
        )


        Spacer(modifier = Modifier.height(12.dp))

        // Image with overlay text
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .weight(2f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.louvre),
                contentDescription = "Louvre",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Experience Art",
                color = Color(0xFFFFE4B5),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = playfairdisplayregular,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }


        Spacer(modifier = Modifier.height(19.dp))

        // Subtitle
        Text(
            text = "We are thrilled to invite you to join us for\n" +
                    "an extraordinary event that will immerse\n" +
                    "you in the world of art.",
            color = Color.LightGray,
            fontSize = 13.sp,
            fontFamily = optima,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        // Button
        Button(
            onClick = {
                val intent  = Intent(context, ExploreActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFD4AF37)),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text(
                text = "Explore Now",
                fontFamily = playfairdisplayregular,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    BaltesLab3Theme {
        Homepage()
    }
}

