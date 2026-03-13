import java.util.Scanner;
import java.util.ArrayList;

/**
 * CLASS - InvalidBookingException
 * Custom exception representing invalid booking scenarios.
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * CLASS - RoomInventory
 * Simple representation of the room inventory system.
 */
class RoomInventory {
    // Inventory management logic would go here
}

/**
 * CLASS - BookingRequestQueue
 * Placeholder for the queue mentioned in the main method.
 */
class BookingRequestQueue {
    private ArrayList<String> requests = new ArrayList<>();
    public void enqueue(String request) { requests.add(request); }
}

/**
 * CLASS - ReservationValidator
 * Centralized logic for validating booking requests.
 */
class ReservationValidator {
    /**
     * Validates booking input provided by the user.
     * @param guestName name of the guest
     * @param roomType requested room type
     * @param inventory centralized inventory
     * @throws InvalidBookingException if validation fails
     */
    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        // Check for empty name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Room type validation (Case-Sensitive: Single, Double, Suite)
        if (!(roomType.equals("Single") || roomType.equals("Double") || roomType.equals("Suite"))) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

/**
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 */
public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Prompt user for input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Perform centralized validation
            validator.validate(guestName, roomType, inventory);

            // If validation passes, add to queue
            bookingQueue.enqueue(guestName + " - " + roomType);
            System.out.println("Booking validated successfully.");

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            // Ensure resources are closed
            scanner.close();
        }
    }
}