package com.boosthail;

import com.boosthail.model.enums.OffloadingOrder;
import com.boosthail.model.enums.Quality;
import com.boosthail.model.interfaces.LogisticPackage;
import com.boosthail.model.records.CartonPackage;
import com.boosthail.model.records.LoosePackage;
import com.boosthail.services.LogisticService;
import com.boosthail.seviceImp.LogisticServiceImpl;
import com.boosthail.util.InputUtil;

public class Main {
    private static final LogisticService service = new LogisticServiceImpl();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = InputUtil.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> loadPackageMenu();
                case 2 -> offloadPackageMenu();
                case 3 -> discardPackageMenu();
                case 4 -> changeOffloadingOrderMenu();
                case 5 -> showHistoryMenu();
                case 6 -> showSnapshotMenu();
                case 7 -> searchMenu();
                case 8 -> exit = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Exiting application. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n--- Company X Logistics Management ---");
        System.out.println("1. Load Package");
        System.out.println("2. Offload Package");
        System.out.println("3. Discard Package");
        System.out.println("4. Change Offloading Order");
        System.out.println("5. Show Package History");
        System.out.println("6. Show Warehouse Snapshot");
        System.out.println("7. Search Package or Rack by Serial");
        System.out.println("8. Exit");
    }

    private static void loadPackageMenu() {
        System.out.println("\n-- Load Package --");
        String type = InputUtil.readLine("Enter package type (loose/carton): ").trim().toLowerCase();
        String serial = InputUtil.readLine("Enter package serial number: ");
        double mass = InputUtil.readDouble("Enter package mass: ");

        int qualityChoice = InputUtil.readInt("Enter quality (1: STANDARD, 2: PREMIUM, 3: SPECIAL): ");
        Quality quality;
        switch (qualityChoice) {
            case 1 -> quality = Quality.STANDARD;
            case 2 -> quality = Quality.PREMIUM;
            case 3 -> quality = Quality.SPECIAL;
            default -> {
                System.out.println("Invalid choice. Defaulting to STANDARD.");
                quality = Quality.STANDARD;
            }
        }

        long timestamp = System.currentTimeMillis();

        LogisticPackage pkg;
        if (type.equals("loose")) {
            pkg = new LoosePackage(serial, mass, quality, timestamp);
        } else {
            pkg = new CartonPackage(serial, mass, quality, timestamp);
        }

        String warehouse = InputUtil.readLine("Enter warehouse name: ");
        String rackSerial = InputUtil.readLine("Enter rack serial: ");
        String lineId = InputUtil.readLine("Enter line id: ");
        String palletSerial = "";
        if (pkg instanceof LoosePackage) {
            palletSerial = InputUtil.readLine("Enter pallet serial (for loose packages): ");
        }
        service.loadPackage(pkg, warehouse, rackSerial, lineId, palletSerial);
    }

    private static void offloadPackageMenu() {
        System.out.println("\n-- Offload Package --");
        String serial = InputUtil.readLine("Enter package serial to offload: ");
        service.offloadPackage(serial, OffloadingOrder.OLDEST_FIRST); // Using current order for simplicity
    }

    private static void discardPackageMenu() {
        System.out.println("\n-- Discard Package --");
        String serial = InputUtil.readLine("Enter package serial to discard: ");
        service.discardPackage(serial);
    }

    private static void changeOffloadingOrderMenu() {
        System.out.println("\n-- Change Offloading Order --");
        int orderChoice = InputUtil.readInt("Enter offloading order (1: OLDEST_FIRST, 2: NEWEST_FIRST): ");
        OffloadingOrder order;
        switch(orderChoice) {
            case 1 -> order = OffloadingOrder.OLDEST_FIRST;
            case 2 -> order = OffloadingOrder.NEWEST_FIRST;
            default -> {
                System.out.println("Invalid choice. No changes made.");
                return;
            }
        }
        service.changeOffloadingOrder(order);
    }

    private static void showHistoryMenu() {
        System.out.println("\n-- Show Package History --");
        String option = InputUtil.readLine("View history by (1) Rack, (2) Line, (3) Warehouse: ");
        switch (option) {
            case "1" -> {
                String rackSerial = InputUtil.readLine("Enter rack serial: ");
                var history = service.getPackageHistoryByRack(rackSerial);
                System.out.println("Package history for rack " + rackSerial + ":");
                history.forEach(pkg -> System.out.println(pkg.getSerialNumber() + " - " + pkg.getMass() + " kg"));
            }
            case "2" -> {
                String warehouse = InputUtil.readLine("Enter warehouse name: ");
                String rackSerial = InputUtil.readLine("Enter rack serial: ");
                String lineId = InputUtil.readLine("Enter line id: ");
                var history = service.getPackageHistoryByLine(warehouse, rackSerial, lineId);
                System.out.println("Package history for line " + lineId + ":");
                history.forEach(pkg -> System.out.println(pkg.getSerialNumber() + " - " + pkg.getMass() + " kg"));
            }
            case "3" -> {
                String warehouse = InputUtil.readLine("Enter warehouse name: ");
                var history = service.getPackageHistoryByWarehouse(warehouse);
                System.out.println("Package history for warehouse " + warehouse + ":");
                history.forEach(pkg -> System.out.println(pkg.getSerialNumber() + " - " + pkg.getMass() + " kg"));
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private static void showSnapshotMenu() {
        System.out.println("\n-- Show Warehouse Snapshot --");
        String warehouse = InputUtil.readLine("Enter warehouse name: ");
        service.showWarehouseSnapshot(warehouse);
    }

    private static void searchMenu() {
        System.out.println("\n-- Search Package or Rack --");
        String serial = InputUtil.readLine("Enter serial number: ");
        var result = service.searchBySerial(serial);
        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("No matching package or rack found.");
        }
    }
}
