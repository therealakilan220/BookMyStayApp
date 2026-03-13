import java.util.*;

/**
 * CLASS - Reservation
 * Represents a booking request to be processed.
 */
class Reservation {
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

/**
 * CLASS - BookingRequestQueue
 * Thread-safe wrapper or simple queue for reservations.
 */
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation res) { queue.add(res); }
    public Reservation getNextRequest() { return queue.poll(); }
    public boolean isEmpty() { return queue.isEmpty(); }
}

/**
 * CLASS - RoomInventory
 * Shared resource tracking room counts.
 */
class RoomInventory {
    Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public void deduct(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nRemaining Inventory:");
        inventory.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}

/**
 * CLASS - RoomAllocationService
 * Logic for allocating rooms to guests.
 */
class RoomAllocationService {
    public void allocateRoom(Reservation res, RoomInventory inventory) {
        inventory.deduct(res.roomType);
        System.out.println("Booking confirmed for Guest: " + res.guestName + ", Room ID: " + res.roomId);
    }
}

/**
 * CLASS - ConcurrentBookingProcessor
 * Runnable task that processes bookings from a shared queue.
 */
class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue,
                                      RoomInventory inventory,
                                      RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation = null;

            // Synchronize on the booking queue to ensure atomic retrieval
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                reservation = bookingQueue.getNextRequest();
            }

            if (reservation != null) {
                // Synchronize on inventory to ensure atomic allocation (prevent race conditions)
                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }

            // Small sleep to simulate processing time and thread interleaving
            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
        }
    }
}

/**
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation");

        // Initialize shared resources
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add dummy data to the queue
        bookingQueue.addRequest(new Reservation("Abhi", "Single", "Single-1"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double", "Double-1"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite", "Suite-1"));
        bookingQueue.addRequest(new Reservation("Subha", "Single", "Single-2"));

        // Create booking processor tasks (Threads)
        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        try {
            // Start concurrent processing
            t1.start();
            t2.start();

            // Wait for threads to finish
            t1.join();
            t2.join();

            // Final inventory display
            inventory.displayInventory();

        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }
    }
}