import java.io.*;
import java.util.*;

// --- Supporting Data Class ---
class RoomInventory {
    private Map<String, Integer> rooms = new LinkedHashMap<>();

    public void updateRoom(String type, int count) {
        rooms.put(type, count);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

// --- Persistence Service Class ---
class FilePersistenceService {
    /**
     * Saves room inventory state to a plain text file.
     * Format: roomType=availableCount
     */
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     */
    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    inventory.updateRoom(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }
}

// --- Main Application Class ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("System Recovery");

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();
        String storagePath = "inventory_data.txt";

        // 1. Attempt to load existing data (System Recovery)
        persistence.loadInventory(inventory, storagePath);

        // 2. If it's a fresh start, initialize with default values
        if (inventory.getRooms().isEmpty()) {
            inventory.updateRoom("Single", 5);
            inventory.updateRoom("Double", 3);
            inventory.updateRoom("Suite", 2);
        }

        // 3. Display Current Inventory (Matching your console snapshot)
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 4. Save state for next time
        persistence.saveInventory(inventory, storagePath);
    }
}