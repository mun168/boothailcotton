package com.boosthail.model;


import com.boosthail.model.enums.LineType;
import com.boosthail.model.interfaces.LogisticPackage;
import com.boosthail.model.records.CartonPackage;
import com.boosthail.model.records.LoosePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a warehouse.
 */
public class Warehouse {
    private final String name;
    private final double maxCapacity; // in kgs
    private final List<Rack> racks;
    private final List<LogisticPackage> packageHistory; // Record of all packages ever loaded in this warehouse

    public Warehouse(String name, double maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.racks = new ArrayList<>();
        this.packageHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public List<Rack> getRacks() {
        return racks;
    }

    public List<LogisticPackage> getPackageHistory() {
        return packageHistory;
    }

    public void addRack(Rack rack) {
        racks.add(rack);
    }

    public void addToHistory(LogisticPackage pkg) {
        packageHistory.add(pkg);
    }

    // Simple snapshot of current warehouse state
    public void printSnapshot() {
        System.out.println("Warehouse: " + name);
        System.out.println("Max Capacity: " + maxCapacity + " kgs");
        for (Rack rack : racks) {
            System.out.println("  Rack: " + rack.getSerialNumber());
            for (Line line : rack.getLines()) {
                System.out.println("    Line: " + line.getId() + " Type: " + line.getType());
                if (line.getType() == LineType.CARTON) {
                    for (CartonPackage cp : line.getCartonPackages()) {
                        System.out.println("      Carton Package: " + cp.serialNumber() + " Mass: " + cp.mass());
                    }
                } else {
                    for (Pallet pallet : line.getPallets()) {
                        System.out.println("      Pallet: " + pallet.getSerialNumber() + " Quality: " + pallet.getQuality());
                        for (LoosePackage lp : pallet.getPackages()) {
                            System.out.println("        Loose Package: " + lp.serialNumber() + " Mass: " + lp.mass());
                        }
                    }
                }
            }
        }
    }
}

