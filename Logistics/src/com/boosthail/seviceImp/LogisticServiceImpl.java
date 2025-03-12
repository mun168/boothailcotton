package com.boosthail.seviceImp;


import com.boosthail.model.Line;
import com.boosthail.model.Pallet;
import com.boosthail.model.Rack;
import com.boosthail.model.Warehouse;
import com.boosthail.model.enums.LineType;
import com.boosthail.model.enums.OffloadingOrder;
import com.boosthail.model.interfaces.LogisticPackage;
import com.boosthail.model.records.CartonPackage;
import com.boosthail.model.records.LoosePackage;
import com.boosthail.services.LogisticService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogisticServiceImpl implements LogisticService {
    private final List<Warehouse> warehouses;
    private OffloadingOrder currentOrder = OffloadingOrder.OLDEST_FIRST;

    public LogisticServiceImpl() {
        this.warehouses = new ArrayList<>();
        // For demonstration, create a sample warehouse with one rack and one line
        Warehouse wh = new Warehouse("MainWarehouse", 10000);
        Rack rack = new Rack("RACK1");
        Line line = new Line("LINE1", LineType.LOOSE, 5000);
        rack.addLine(line);
        wh.addRack(rack);
        warehouses.add(wh);
    }

    @Override
    public void loadPackage(LogisticPackage pkg, String warehouseName, String rackSerial, String lineId, String palletSerial) {
        // Create warehouse if it doesn't exist
        Warehouse warehouse = findWarehouse(warehouseName);
        if (warehouse == null) {
            warehouse = new Warehouse(warehouseName, 10000); // Default capacity of 10,000 kg
            warehouses.add(warehouse);
            System.out.println("Created new warehouse: " + warehouseName);
        }
        // Create rack if it doesn't exist
        Rack rack = findRack(warehouse, rackSerial);
        if (rack == null) {
            rack = new Rack(rackSerial);
            warehouse.addRack(rack);
            System.out.println("Created new rack: " + rackSerial);
        }
        // Create line if it doesn't exist; type and capacity based on package type
        Line line = findLine(rack, lineId);
        if (line == null) {
            LineType type = (pkg instanceof LoosePackage) ? LineType.LOOSE : LineType.CARTON;
            double capacity = (pkg instanceof LoosePackage) ? 5000 : 100; // Defaults: 5000 kg for loose, 100 units for cartons
            line = new Line(lineId, type, capacity);
            rack.addLine(line);
            System.out.println("Created new line: " + lineId + " with type: " + type);
        }
        // Process package loading based on its type
        if (pkg instanceof LoosePackage) {
            Pallet pallet = findPallet(line, palletSerial);
            if (pallet == null) {
                pallet = new Pallet(palletSerial, pkg.getQuality(), 10); // Default pallet capacity of 10 packages
                line.addPallet(pallet);
                System.out.println("Created new pallet: " + palletSerial);
            }
            if (pallet.addPackage((LoosePackage) pkg)) {
                System.out.println("Loose package loaded onto pallet: " + palletSerial);
            }
        } else if (pkg instanceof CartonPackage) {
            if (line.addCartonPackage((CartonPackage) pkg)) {
                System.out.println("Carton package loaded directly onto line.");
            }
        }
        // Record the package in history
        rack.addToHistory(pkg);
        warehouse.addToHistory(pkg);
    }


    @Override
    public void offloadPackage(String packageSerial, OffloadingOrder order) {
        // For simplicity, search all warehouses and remove the first matching package.
        for (Warehouse warehouse : warehouses) {
            for (Rack rack : warehouse.getRacks()) {
                for (Line line : rack.getLines()) {
                    boolean removed = false;
                    if (line.getType() == LineType.CARTON) {
                        removed = line.removeCartonPackage(packageSerial);
                    } else {
                        removed = line.removeLoosePackage(packageSerial);
                    }
                    if (removed) {
                        System.out.println("Package " + packageSerial + " offloaded.");
                        return;
                    }
                }
            }
        }
        System.out.println("Package not found.");
    }

    @Override
    public void discardPackage(String packageSerial) {
        // For simplicity, discard by offloading
        offloadPackage(packageSerial, currentOrder);
        System.out.println("Package " + packageSerial + " discarded.");
    }

    @Override
    public void changeOffloadingOrder(OffloadingOrder order) {
        this.currentOrder = order;
        System.out.println("Offloading order changed to: " + order);
    }

    @Override
    public List<LogisticPackage> getPackageHistoryByRack(String rackSerial) {
        for (Warehouse warehouse : warehouses) {
            for (Rack rack : warehouse.getRacks()) {
                if (rack.getSerialNumber().equals(rackSerial)) {
                    return rack.getPackageHistory();
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<LogisticPackage> getPackageHistoryByLine(String warehouseName, String rackSerial, String lineId) {
        Warehouse warehouse = findWarehouse(warehouseName);
        if (warehouse != null) {
            Rack rack = findRack(warehouse, rackSerial);
            if (rack != null) {
                Line line = findLine(rack, lineId);
                if (line != null) {
                    List<LogisticPackage> history = new ArrayList<>();
                    if (line.getType() == LineType.CARTON) {
                        history.addAll(line.getCartonPackages());
                    } else {
                        for (Pallet p : line.getPallets()) {
                            history.addAll(p.getPackages());
                        }
                    }
                    return history;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<LogisticPackage> getPackageHistoryByWarehouse(String warehouseName) {
        Warehouse warehouse = findWarehouse(warehouseName);
        if (warehouse != null) {
            return warehouse.getPackageHistory();
        }
        return new ArrayList<>();
    }

    @Override
    public void showWarehouseSnapshot(String warehouseName) {
        Warehouse warehouse = findWarehouse(warehouseName);
        if (warehouse != null) {
            warehouse.printSnapshot();
        } else {
            System.out.println("Warehouse not found.");
        }
    }

    @Override
    public Optional<String> searchBySerial(String serial) {
        // Search for package or rack by serial
        for (Warehouse warehouse : warehouses) {
            for (Rack rack : warehouse.getRacks()) {
                if (rack.getSerialNumber().equals(serial)) {
                    return Optional.of("Rack " + serial + " found in Warehouse " + warehouse.getName());
                }
                for (Line line : rack.getLines()) {
                    // Search in carton packages
                    for (CartonPackage cp : line.getCartonPackages()) {
                        if (cp.serialNumber().equals(serial)) {
                            return Optional.of("Package " + serial + " found in Rack " + rack.getSerialNumber()
                                    + ", Line " + line.getId() + " in Warehouse " + warehouse.getName());
                        }
                    }
                    // Search in loose packages from pallets
                    for (Pallet p : line.getPallets()) {
                        if (p.getSerialNumber().equals(serial)) {
                            return Optional.of("Pallet " + serial + " found in Rack " + rack.getSerialNumber()
                                    + ", Line " + line.getId() + " in Warehouse " + warehouse.getName());
                        }
                        for (LoosePackage lp : p.getPackages()) {
                            if (lp.serialNumber().equals(serial)) {
                                return Optional.of("Package " + serial + " found in Pallet " + p.getSerialNumber()
                                        + ", Rack " + rack.getSerialNumber() + ", Line " + line.getId()
                                        + " in Warehouse " + warehouse.getName());
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    // Helper methods to locate entities

    private Warehouse findWarehouse(String warehouseName) {
        for (Warehouse wh : warehouses) {
            if (wh.getName().equalsIgnoreCase(warehouseName)) {
                return wh;
            }
        }
        return null;
    }

    private Rack findRack(Warehouse warehouse, String rackSerial) {
        for (Rack rack : warehouse.getRacks()) {
            if (rack.getSerialNumber().equalsIgnoreCase(rackSerial)) {
                return rack;
            }
        }
        return null;
    }

    private Line findLine(Rack rack, String lineId) {
        for (Line line : rack.getLines()) {
            if (line.getId().equalsIgnoreCase(lineId)) {
                return line;
            }
        }
        return null;
    }

    private Pallet findPallet(Line line, String palletSerial) {
        for (Pallet p : line.getPallets()) {
            if (p.getSerialNumber().equalsIgnoreCase(palletSerial)) {
                return p;
            }
        }
        return null;
    }
}
