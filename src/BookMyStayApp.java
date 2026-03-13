import java.util.*;

/**
 * CLASS - CancellationService
 * Handles booking cancellations and inventory rollback using a Stack.
 */
class CancellationService {
    // Stack stores recently released room IDs (LIFO order)
    private Stack<String> releasedRoomIds;
    // Maps reservation ID to room type for lookup during cancellation
    private Map<String, String> reservationRoomTypeMap;

    /**
     * Initializes tracking structures.
     */
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking data for later cancellation.
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and restores inventory.
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            String roomType = reservationRoomTypeMap.get(reservationId);

            // Logic to restore inventory would be called here
            inventory.restoreRoom(roomType);

            // Track the released ID in the stack for rollback history
            releasedRoomIds.push(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        } else {
            System.out.println("Error: Reservation ID not found.");
        }
    }

    /**
     * Displays recently cancelled reservations in rollback order.
     */
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No history available.");
            return;
        }

        // Peek at the most recent cancellation
        System.out.println("Released Reservation ID: " + releasedRoomIds.peek());
    }
}

/**
 * CLASS - RoomInventory
 * Manages the availability counts for room types.
 */
class RoomInventory {
    private int singleRooms = 5;

    public void restoreRoom(String type) {
        if (type.equals("Single")) {
            singleRooms++;
        }
    }

    public int getSingleRoomAvailability() {
        return singleRooms;
    }
}

/**
 * MAIN CLASS - UseCase10BookingCancellation
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Booking Cancellation");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Simulate a pre-existing booking
        String resId = "Single-1";
        cancellationService.registerBooking(resId, "Single");

        // Perform cancellation
        cancellationService.cancelBooking(resId, inventory);

        // Show history and updated inventory
        cancellationService.showRollbackHistory();
        System.out.println("Updated Single Room Availability: " + inventory.getSingleRoomAvailability());
    }
}