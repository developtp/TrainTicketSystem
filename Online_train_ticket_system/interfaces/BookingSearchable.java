package interfaces;
import model.Booking;

public interface BookingSearchable {
    Booking searchBookingById(int bookingId);
}
