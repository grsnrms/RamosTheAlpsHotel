package ph.edu.comteq.ramosthealpshotel

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookingDatabase(context: Context) : SQLiteOpenHelper(context, "bookings.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "hotel_name TEXT, " +
                    "check_in TEXT, " +
                    "check_out TEXT, " +
                    "adults INTEGER, " +
                    "children INTEGER, " +
                    "rooms INTEGER, " +
                    "booking_type TEXT, " +
                    "payment TEXT, " +
                    "price INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS bookings")
        onCreate(db)
    }


    fun addBooking(booking: Booking) {
        val values = ContentValues()
        values.put("first_name", booking.firstName)
        values.put("last_name", booking.lastName)
        values.put("hotel_name", booking.hotelName)
        values.put("check_in", booking.checkIn)
        values.put("check_out", booking.checkOut)
        values.put("adults", booking.adults)
        values.put("children", booking.children)
        values.put("rooms", booking.rooms)
        values.put("booking_type", booking.bookingType)
        values.put("payment", booking.payment)
        values.put("price", booking.price)

        val db = writableDatabase
        db.insert("bookings", null, values)
        db.close()
    }


    fun getAllBookings(): List<Booking> {
        val list = mutableListOf<Booking>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM bookings ORDER BY id", null)

        while (cursor.moveToNext()) {
            val booking = Booking(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                hotelName = cursor.getString(cursor.getColumnIndexOrThrow("hotel_name")),
                checkIn = cursor.getString(cursor.getColumnIndexOrThrow("check_in")),
                checkOut = cursor.getString(cursor.getColumnIndexOrThrow("check_out")),
                adults = cursor.getInt(cursor.getColumnIndexOrThrow("adults")),
                children = cursor.getInt(cursor.getColumnIndexOrThrow("children")),
                rooms = cursor.getInt(cursor.getColumnIndexOrThrow("rooms")),
                bookingType = cursor.getString(cursor.getColumnIndexOrThrow("booking_type")),
                payment = cursor.getString(cursor.getColumnIndexOrThrow("payment")),
                price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
            )
            list.add(booking)
        }

        cursor.close()
        db.close()
        return list
    }
}