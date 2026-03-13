import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Combined implementation for Use Case 7: Add-On Service Selection
 */
public class BookMyStayApp {

    // --- INNER CLASS: AddOnService ---
    static class AddOnService {
        private String serviceName;
        private double cost;

        public AddOnService(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() { return serviceName; }
        public double getCost() { return cost; }
    }

    // --- INNER CLASS: AddOnServiceManager ---
    static class AddOnServiceManager {
        private Map<String, List<AddOnService>> servicesByReservation;

        public AddOnServiceManager() {
            this.servicesByReservation = new HashMap<>();
        }

        public void addService(String reservationId, AddOnService service) {
            servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
            servicesByReservation.get(reservationId).add(service);
        }

        public double calculateTotalServiceCost(String reservationId) {
            List<AddOnService> services = servicesByReservation.get(reservationId);
            if (services == null) return 0.0;

            double total = 0.0;
            for (AddOnService service : services) {
                total += service.getCost();
            }
            return total;
        }
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        // 1. Initialize the manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // 2. Define the Reservation ID
        String reservationId = "Single-1";

        // 3. Create and attach services (Matching the 1500.0 total in your example)
        AddOnService breakfast = new AddOnService("Breakfast", 500.0);
        AddOnService spa = new AddOnService("Spa", 1000.0);

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, spa);

        // 4. Print Output to match the required console snapshot
        System.out.println("Add-On Service Selection");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + manager.calculateTotalServiceCost(reservationId));
    }
}