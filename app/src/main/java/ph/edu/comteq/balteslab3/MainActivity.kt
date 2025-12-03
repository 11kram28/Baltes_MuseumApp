package ph.edu.comteq.balteslab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme

// FONT FAMILIES
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

    var titleText by remember { mutableStateOf("") }
    var introText by remember { mutableStateOf("") }
    var showButton by remember { mutableStateOf(false) }
    var showOverlay by remember { mutableStateOf(true) } // Control overlay visibility

    // Animation values for overlay
    val overlayAlpha by animateFloatAsState(
        targetValue = if (showOverlay) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "overlayAlpha"
    )

    val overlayOffset by animateFloatAsState(
        targetValue = if (showOverlay) 0f else 100f,
        animationSpec = tween(durationMillis = 600),
        label = "overlayOffset"
    )

    val fullTitle = "Experience Art"
    val fullIntro =
        "We are thrilled to invite you to join us for\n" +
                "an extraordinary event that will immerse\n" +
                "you in the world of art."

    // REVERSED ANIMATION SEQUENCE: Overlay first, then typing
    LaunchedEffect(Unit) {
        // FIRST: Fade out the overlay
        delay(1000) // Initial delay
        showOverlay = false // Start overlay fade out

        // Wait for overlay animation to complete
        delay(1000)

        // THEN: Start typing animation
        fullTitle.forEach {
            titleText += it
            delay(60)
        }
        delay(300)
        fullIntro.forEach {
            introText += it
            delay(20)
        }

        // Show button after typing
        delay(500)
        showButton = true
    }

    // Set arch height in Dp for spacing
    val archHeightDp = 250.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(75.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ARCHWAY WITH OVERLAY
        Box(
            modifier = Modifier
                .height(archHeightDp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ArchwayImage()

            // Animated Overlay
            if (showOverlay || overlayAlpha > 0f) {
                ArchwayOverlay(
                    alpha = overlayAlpha,
                    offsetY = overlayOffset
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TITLE & INTRO TEXT BACKGROUND
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Show title only if overlay animation is complete or text has started
                if (!showOverlay || titleText.isNotEmpty()) {
                    Text(
                        text = titleText,
                        color = Color(0xFFFFE4B5),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = playfairdisplayregular,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = introText,
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        fontFamily = optima,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ANIMATED BUTTON ENTRANCE
        AnimatedVisibility(
            visible = showButton,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 600)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, ExploreActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFD4AF37))
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
}


// Design like Archway door with Image
@Composable
fun ArchwayImage() {

    val context = LocalContext.current
    val imageBitmap = ImageBitmap.imageResource(context.resources, R.drawable.louvre)

    Canvas(modifier = Modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height

        val archTop = h * 0.25f
        val archHeight = h * 0.9f
        val sideInset = w * 0.1f

        // ARCH PATH
        val archPath = Path().apply {
            moveTo(sideInset, archTop + archHeight)
            lineTo(sideInset, archTop)

            quadraticBezierTo(
                w / 2f, archTop - archHeight * 0.5f,
                w - sideInset, archTop
            )

            lineTo(w - sideInset, archTop + archHeight)
            close()
        }

        // DRAW IMAGE INSIDE ARCH
        clipPath(archPath) {
            drawImage(
                image = imageBitmap,
                dstSize = IntSize((w - 2 * sideInset).toInt(), archHeight.toInt()),
                dstOffset = IntOffset(sideInset.toInt(), archTop.toInt())
            )
        }

        // BLACK OUTSIDE ARCH
        val fullRect = Path().apply {
            addRect(Rect(0f, 0f, w, h))
        }
        val outsidePath = Path().apply {
            op(fullRect, archPath, PathOperation.Difference)
        }
        drawPath(
            path = outsidePath,
            color = Color.Black
        )

        // GOLD BORDER
        drawPath(
            path = archPath,
            color = Color(0xFFFFD27F),
            style = Stroke(width = 6f)
        )
    }
}


// Archway door Overlay
@Composable
fun ArchwayOverlay(alpha: Float, offsetY: Float) {
    val random = remember { Random(System.currentTimeMillis().toInt()) }

    Canvas(modifier = Modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height

        val archTop = h * 0.25f
        val archHeight = h * 0.9f
        val sideInset = w * 0.1f

        // Apply slide down animation
        translate(top = offsetY) {
            // Draw a semi-transparent overlay over the archway
            val overlayPath = Path().apply {
                moveTo(sideInset, archTop + archHeight)
                lineTo(sideInset, archTop)
                quadraticBezierTo(
                    w / 2f, archTop - archHeight * 0.5f,
                    w - sideInset, archTop
                )
                lineTo(w - sideInset, archTop + archHeight)
                close()
            }

            // Draw dark overlay with animation alpha
            drawPath(
                path = overlayPath,
                color = Color(0x80000000).copy(alpha = alpha) // Semi-transparent black
            )

            // Draw overlay border
            drawPath(
                path = overlayPath,
                color = Color(0xFFFFC107).copy(alpha = alpha),
                style = Stroke(width = 4f)
            )

            // Draw text overlay
            if (alpha > 0.5f) {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.argb(
                        (alpha * 255).toInt(),
                        255,
                        215,
                        0
                    )
                    textSize = 32f
                    isFakeBoldText = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                drawContext.canvas.nativeCanvas.drawText(
                    "",
                    w / 2f,
                    archTop + archHeight / 2f,
                    paint
                )
            }
        }

        // Draw falling particles during animation
        if (alpha < 0.8f && alpha > 0.1f) {
            val particleCount = 8
            repeat(particleCount) {
                val particleX = sideInset + random.nextFloat() * (w - 2 * sideInset)
                val particleY = archTop + (it * 10f) + (offsetY * 0.5f)

                drawCircle(
                    color = Color(0xFFFFC107).copy(alpha = alpha * 0.7f),
                    radius = 4f * (1 - alpha),
                    center = Offset(particleX, particleY)
                )
            }
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