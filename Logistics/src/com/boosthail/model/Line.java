package com.boosthail.model;

import com.boosthail.model.enums.LineType;
import com.boosthail.model.records.CartonPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line in a rack.
 * It can hold either cartons or pallets of loose packages.
 */
public class Line {
    private final String id;
    private final LineType type;
    private final double capacity; // For LOOSE/MIXED: weight capacity (kg) or for CARTON: number of cartons allowed
    private final List<Pallet> pallets;         // For loose packages
    private final List<CartonPackage> cartonPackages; // For carton packages

    public Line(String id, LineType type, double capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.pallets = new ArrayList<>();
        this.cartonPackages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public LineType getType() {
        return type;
    }

    public double getCapacity() {
        return capacity;
    }

    public List<Pallet> getPallets() {
        return pallets;
    }

    public List<CartonPackage> getCartonPackages() {
        return cartonPackages;
    }

    // Add a pallet to this line (for loose packages)
    public boolean addPallet(Pallet pallet) {
        pallets.add(pallet);
        return true;
    }

    // Add a carton package directly to this line
    public boolean addCartonPackage(CartonPackage pkg) {
        if (cartonPackages.size() >= capacity) {
            System.out.println("Line carton capacity reached.");
            return false;
        }
        cartonPackages.add(pkg);
        return true;
    }

    public boolean removeCartonPackage(String packageSerial) {
        return cartonPackages.removeIf(p -> p.serialNumber().equals(packageSerial));
    }

    // Remove a loose package from pallets
    public boolean removeLoosePackage(String packageSerial) {
        for (Pallet p : pallets) {
            if (p.removePackage(packageSerial)) {
                return true;
            }
        }
        return false;
    }
}
