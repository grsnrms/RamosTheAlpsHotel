package ph.edu.comteq.ramosthealpshotel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyBookingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookings = remember { BookingDatabase(context).getAllBookings() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(title = "My Bookings", onBackClick = onBackClick)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE))
                .padding(16.dp)
        ) {
            Text(
                text = "List of my bookings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (bookings.isEmpty()) {
            Text(
                text = "No bookings yet.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(bookings) { index, booking ->
                    BookingItem(number = index + 1, booking = booking)
                }
            }
        }
    }
}

@Composable
fun BookingItem(number: Int, booking: Booking) {
    var adultText = "Adults"
    if (booking.adults == 1) adultText = "Adult"
    var childText = "Children"
    if (booking.children == 1) childText = "Child"
    var roomText = "Rooms"
    if (booking.rooms == 1) roomText = "Room"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$number",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "${booking.firstName} ${booking.lastName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = booking.hotelName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text("${booking.checkIn} to ${booking.checkOut}")
                    Text("${booking.adults} $adultText, ${booking.children} $childText, ${booking.rooms} $roomText")
                    Text(
                        text = "${booking.bookingType}    Pay with ${booking.payment.lowercase()}",
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = "€ ${booking.price}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}