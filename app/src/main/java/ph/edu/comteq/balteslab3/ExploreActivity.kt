package ph.edu.comteq.balteslab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme

class ExploreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaltesLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Explore(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Explore(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                horizontal = 25.dp, // Adds padding on the left and right.
                vertical = 90.dp // Adds a good amount of padding on the top and bottom.
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Explore" title and divider
        Text(
            text = "Explore",
            color = Color(0xFFFFE4B5),
            fontFamily = playfairdisplayregular,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.White, thickness = 1.dp)

        // Upcoming Event Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Event",
                color = Color.White,
                fontFamily = optima,
                fontSize = 25.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val intent  = Intent(context, TicketingActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = "Tickets",
                        color = Color.White,
                        fontFamily = optima,
                        fontSize = 18.sp
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_right),
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main event card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 30.dp, bottomEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
        ) {
            Column {
                // Image with curved top corners
                Image(
                    painter = painterResource(id = R.drawable.renaissance),
                    contentDescription = "The School of Athens",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                // Row for date and details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "10",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontFamily = optima,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "OCT",
                            color = Color.White,
                            fontFamily = optima,
                            fontSize = 20.sp
                        )
                    }

                    // Details column
                    Column {
                        Text(
                            text = "Renaissance Exhibition",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = optima,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "9:00 AM - 6:00 PM",
                            color = Color.LightGray,
                            fontFamily = optima,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0XFFD4AF37),
                                        fontSize = 14.sp,
                                        fontFamily = optima,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("Indulge in the rich tapestry\nof Renaissance art")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontFamily = optima,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("+33 (0)1 23 45 67 89")
                                }
                            }
                        )
                    }
                }
            }

            // "Visit Gallery" button
            Button(
                onClick = { /* TODO: Handle button click */ },
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 30.dp, bottomEnd = 30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFD4AF37)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) {
                Text(
                    text = "Visit Gallery",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = playfairdisplayregular
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExplorePreview() {
    BaltesLab3Theme {
        Explore()
    }
}