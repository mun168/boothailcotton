package com.boosthail.model.records;

import com.boosthail.model.enums.Quality;
import com.boosthail.model.interfaces.LogisticPackage;

/**
 * Immutable record for a loose package.
 */
public record LoosePackage(String serialNumber, double mass, Quality quality, long timestamp) implements LogisticPackage {
    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public Quality getQuality() {
        return quality;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
