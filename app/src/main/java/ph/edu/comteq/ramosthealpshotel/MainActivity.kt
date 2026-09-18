package ph.edu.comteq.ramosthealpshotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import ph.edu.comteq.ramosthealpshotel.ui.theme.RamosTheAlpsHotelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RamosTheAlpsHotelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Homepage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Homepage(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hotels by remember { mutableStateOf(value = emptyList<Hotel>()) }

    // load json data
    LaunchedEffect(key1 = Unit) {
        val json = context.assets.open("hotels.json")
            .bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val hotelsArray = gson.fromJson(json, Array<Hotel>::class.java)
        hotels = hotelsArray.toList()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "The Alps Hotels",
                    style = MaterialTheme.typography.titleLarge
                )
                Image(
                    painter = painterResource(id = R.drawable.france_national_flag),
                    contentDescription = "France flag",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "Profile",
                modifier = Modifier.size(28.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            items(hotels) { hotel ->
                Text(hotel.hotel_name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    RamosTheAlpsHotelTheme {
        Homepage("Android")
    }
}