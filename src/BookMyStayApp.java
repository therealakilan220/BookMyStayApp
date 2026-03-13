import java.util.ArrayList;
import java.util.List;

/**
 * Combined implementation for Use Case 8: Booking History & Reporting
 */
public class BookMyStayApp {

    // --- CLASS: Reservation ---
    // Represents a confirmed booking with guest name and room type
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    // --- CLASS: BookingHistory ---
    // Maintains a record of confirmed reservations
    static class BookingHistory {
        private List<Reservation> confirmedReservations;

        public BookingHistory() {
            this.confirmedReservations = new ArrayList<>();
        }

        /** Adds a confirmed reservation to history */
        public void addReservation(Reservation reservation) {
            confirmedReservations.add(reservation);
        }

        /** Returns all confirmed reservations */
        public List<Reservation> getConfirmedReservations() {
            return confirmedReservations;
        }
    }

    // --- CLASS: BookingReportService ---
    // Generates reports from booking history data
    static class BookingReportService {
        /** Displays a summary report of all confirmed bookings */
        public void generateReport(BookingHistory history) {
            System.out.println("Booking History Report");
            for (Reservation res : history.getConfirmedReservations()) {
                System.out.println("Guest: " + res.getGuestName() + ", Room Type: " + res.getRoomType());
            }
        }
    }

    // --- MAIN CLASS logic ---
    public static void main(String[] args) {
        // Initialize history and report service
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Add bookings to match the requirement snapshot
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Header for the output
        System.out.println("Booking History and Reporting\n");

        // Generate the report
        reportService.generateReport(history);
    }
}