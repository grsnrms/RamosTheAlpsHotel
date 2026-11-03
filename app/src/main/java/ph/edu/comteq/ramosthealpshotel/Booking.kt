package ph.edu.comteq.ramosthealpshotel

data class Booking(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val hotelName: String,
    val checkIn: String,
    val checkOut: String,
    val adults: Int,
    val children: Int,
    val rooms: Int,
    val bookingType: String,
    val payment: String,
    val price: Int
)