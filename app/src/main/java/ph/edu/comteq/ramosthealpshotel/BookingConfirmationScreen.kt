package ph.edu.comteq.ramosthealpshotel

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun formatDate(millis: Long): String {
    val format = SimpleDateFormat("EEE, MMM d, yyyy", Locale.US)
    return format.format(Date(millis))
}


fun getDay(plusDays: Int): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.add(Calendar.DAY_OF_YEAR, plusDays)
    return cal.timeInMillis
}


fun pickDate(context: Context, currentMillis: Long, onPicked: (Long) -> Unit) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = currentMillis

    DatePickerDialog(
        context,
        { _, year, month, day ->
            val picked = Calendar.getInstance()
            picked.set(year, month, day, 0, 0, 0)
            picked.set(Calendar.MILLISECOND, 0)
            onPicked(picked.timeInMillis)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Composable
fun BookingConfirmScreen(
    hotelName: String,
    room: Room,
    onBackClick: () -> Unit,
    onBooked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var checkIn by remember { mutableLongStateOf(getDay(0)) }
    var checkOut by remember { mutableLongStateOf(getDay(1)) }
    var adults by remember { mutableStateOf("2") }
    var children by remember { mutableStateOf("0") }
    var bookingType by remember { mutableStateOf("For sightseeing") }
    var payment by remember { mutableStateOf("Cash") }


    val adultCount = adults.toIntOrNull() ?: 0
    val childCount = children.toIntOrNull() ?: 0
    val nights = Math.round((checkOut - checkIn) / 86400000.0).toInt()
    val guests = adultCount + childCount


    var rooms = 1
    if (guests > 0) {
        rooms = (guests + room.room_total_number_of_guests - 1) / room.room_total_number_of_guests
    }


    var total = 0
    if (nights > 0) {
        total = room.room_price_for_one_night * nights * rooms
    }
    if (bookingType == "For business with a meeting room") {
        total += 150
    }

    val formOk = firstName.isNotBlank() && lastName.isNotBlank() && adultCount >= 1 && nights >= 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(title = "Booking Confirm", onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
                    .padding(16.dp)
            ) {
                Text(
                    text = "You are going to reserve:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = hotelName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Text(
                        text = room.room_type,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Bed: ${room.room_bed_type}   Guests: ${room.room_total_number_of_guests}")
                        Text("€ ${room.room_price_for_one_night}", fontWeight = FontWeight.Bold)
                    }
                }
            }


            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Form",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }


                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .clickable { pickDate(context, checkIn) { checkIn = it } }
                            .padding(12.dp)
                    ) {
                        Text("Check-in date", fontSize = 14.sp)
                        Text(formatDate(checkIn), fontWeight = FontWeight.Bold)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .clickable { pickDate(context, checkOut) { checkOut = it } }
                            .padding(12.dp)
                    ) {
                        Text("Check-out date", fontSize = 14.sp)
                        Text(formatDate(checkOut), fontWeight = FontWeight.Bold)
                    }
                }
                if (nights < 1) {
                    Text(
                        text = "Check-out date must be after the check-in date",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }


                Text(
                    text = room.room_type,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = adults,
                        onValueChange = { adults = it.filter { c -> c.isDigit() } },
                        label = { Text("Adults") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = children,
                        onValueChange = { children = it.filter { c -> c.isDigit() } },
                        label = { Text("Children") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Room", fontSize = 14.sp)
                        Text("$rooms", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(12.dp)
                ) {
                    Text("Travel for business?")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = bookingType == "For sightseeing",
                            onClick = { bookingType = "For sightseeing" }
                        )
                        Text("For sightseeing")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = bookingType == "For business with a meeting room",
                            onClick = { bookingType = "For business with a meeting room" }
                        )
                        Column {
                            Text("+ €150", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("For business with a meeting room")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(12.dp)
                ) {
                    Text("Which way to pay?")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = payment == "Cash",
                            onClick = { payment = "Cash" }
                        )
                        Text("Cash", fontSize = 14.sp)
                        RadioButton(
                            selected = payment == "Credit card",
                            onClick = { payment = "Credit card" }
                        )
                        Text("Credit card", fontSize = 14.sp)
                        RadioButton(
                            selected = payment == "E-pay",
                            onClick = { payment = "E-pay" }
                        )
                        Text("E-pay", fontSize = 14.sp)
                    }
                }


                Text(
                    text = "€ $total",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val booking = Booking(
                            id = 0,
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            hotelName = hotelName,
                            checkIn = formatDate(checkIn),
                            checkOut = formatDate(checkOut),
                            adults = adultCount,
                            children = childCount,
                            rooms = rooms,
                            bookingType = bookingType,
                            payment = payment,
                            price = total
                        )

                        val db = BookingDatabase(context)
                        db.addBooking(booking)
                        onBooked()
                    },
                    enabled = formOk,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book now", fontSize = 18.sp)
                }
            }
        }
    }
}