package com.boosthail.model.interfaces;

import com.boosthail.model.enums.Quality;

/**
 * Interface for a logistic package.
 */
public interface LogisticPackage {
    String getSerialNumber();
    double getMass();
    Quality getQuality();
    long getTimestamp(); // Timestamp when the package was loaded
}