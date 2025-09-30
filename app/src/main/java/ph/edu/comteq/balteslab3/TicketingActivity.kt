package ph.edu.comteq.balteslab3

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.balteslab3.ui.theme.BaltesLab3Theme
import java.time.Duration
import java.time.Instant

class TicketingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaltesLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ticketing(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing(modifier: Modifier = Modifier) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object:SelectableDates{
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )
    Column(
        modifier = modifier.background(Color.Black)
    ){
        // Header
        Column (
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ){
            Box(
                modifier = Modifier.fillMaxWidth().height(230.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(R.drawable.header),
                    contentDescription = "Header Image",
                    modifier = Modifier.fillMaxWidth().height(230.dp),
                    contentScale = ContentScale.Crop
                )

                // Black overlay
                Box(
                    modifier = Modifier.fillMaxWidth().height(230.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                )
                Text(
                    text = "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
            // Inner Container for date and ticket types
            Column(
                modifier = Modifier.fillMaxWidth().padding(15.dp)
            ){
                DatePicker(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            text = "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(0xFFd29f1b),
                        headlineContentColor = Color(0xFFd29f1b),
                        weekdayContentColor = Color(0xFFd29f1b),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(0xFFd29f1b),
                        todayDateBorderColor = Color(0xFFd29f1b),
                        selectedDayContainerColor = Color(0xFFd29f1b),
                        selectedDayContentColor = Color.Black,
                        disabledDayContentColor = Color.Gray
                    )
                )
                // General Admission Ticket

                // Free Ticket
            }
        }
        // bottom bar for totals
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp)
                .background(Color(0xFFd29f1b))
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Total: P500",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Black
            )
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {/*TODO*/},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                )
            ) {
                Text(
                    text = "Checkout",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(0xFFd29f1b)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicktingPreview() {
    BaltesLab3Theme {
        Ticketing(modifier = Modifier)
    }
}