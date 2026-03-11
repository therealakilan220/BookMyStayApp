import java.util.*;

/**
 * ***************************************************************
 * EXISTING CLASSES (From previous Use Cases)
 * ***************************************************************
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class RoomInventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public RoomInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

/**
 * ***************************************************************
 * CLASS - RoomAllocationService
 * ***************************************************************
 */
class RoomAllocationService {
    // Stores all allocated room IDs to prevent duplicate assignments
    private Set<String> allocatedRoomIds;
    // Stores assigned room IDs by room type
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String type = reservation.getRoomType();
        int currentCount = inventory.getRoomAvailability().getOrDefault(type, 0);

        if (currentCount > 0) {
            // Generate ID and track it
            String roomId = generateRoomId(type);
            allocatedRoomIds.add(roomId);

            // Update inventory
            inventory.updateAvailability(type, currentCount - 1);

            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                    ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() +
                    ". No " + type + " rooms available.");
        }
    }

    private String generateRoomId(String roomType) {
        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
        int nextId = assignedRoomsByType.get(roomType).size() + 1;
        String roomId = roomType + "-" + nextId;
        assignedRoomsByType.get(roomType).add(roomId);
        return roomId;
    }
}

/**
 * ***************************************************************
 * MAIN CLASS - UseCase6RoomAllocation
 * ***************************************************************
 */
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Room Allocation Processing");

        // Initialize Services
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Create booking requests (FIFO order)
        List<Reservation> requests = new ArrayList<>();
        requests.add(new Reservation("Abhi", "Single"));
        requests.add(new Reservation("Subha", "Single"));
        requests.add(new Reservation("Vannathi", "Suite"));

        // Process requests
        for (Reservation res : requests) {
            allocationService.allocateRoom(res, inventory);
        }
    }
}