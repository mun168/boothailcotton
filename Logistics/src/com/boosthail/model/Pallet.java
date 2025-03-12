package com.boosthail.model;


import com.boosthail.model.enums.Quality;
import com.boosthail.model.records.LoosePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pallet that holds loose packages of the same quality.
 */
public class Pallet {
    private final String serialNumber;
    private final Quality quality;
    private final int capacity; // Maximum number of loose packages allowed
    private final List<LoosePackage> packages;

    public Pallet(String serialNumber, Quality quality, int capacity) {
        this.serialNumber = serialNumber;
        this.quality = quality;
        this.capacity = capacity;
        this.packages = new ArrayList<>();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Quality getQuality() {
        return quality;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<LoosePackage> getPackages() {
        return packages;
    }

    public boolean addPackage(LoosePackage pkg) {
        if (!pkg.quality().equals(quality)) {
            System.out.println("Package quality does not match pallet quality.");
            return false;
        }
        if (packages.size() >= capacity) {
            System.out.println("Pallet capacity reached.");
            return false;
        }
        packages.add(pkg);
        return true;
    }

    public boolean removePackage(String packageSerial) {
        return packages.removeIf(p -> p.serialNumber().equals(packageSerial));
    }
}
