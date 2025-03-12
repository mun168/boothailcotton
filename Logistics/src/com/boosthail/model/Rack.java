package com.boosthail.model;

import com.boosthail.model.interfaces.LogisticPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rack in a warehouse.
 */
public class Rack {
    private final String serialNumber;
    private final List<Line> lines;
    private final List<LogisticPackage> packageHistory; // Record of all packages ever loaded on this rack

    public Rack(String serialNumber) {
        this.serialNumber = serialNumber;
        this.lines = new ArrayList<>();
        this.packageHistory = new ArrayList<>();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<LogisticPackage> getPackageHistory() {
        return packageHistory;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void addToHistory(LogisticPackage pkg) {
        packageHistory.add(pkg);
    }
}

