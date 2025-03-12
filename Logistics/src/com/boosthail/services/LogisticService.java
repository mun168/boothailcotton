package com.boosthail.services;



import com.boosthail.model.enums.OffloadingOrder;
import com.boosthail.model.interfaces.LogisticPackage;

import java.util.List;
import java.util.Optional;

public interface LogisticService {
    void loadPackage(LogisticPackage pkg, String warehouseName, String rackSerial, String lineId, String palletSerial);
    void offloadPackage(String packageSerial, OffloadingOrder order);
    void discardPackage(String packageSerial);
    void changeOffloadingOrder(OffloadingOrder order);
    List<LogisticPackage> getPackageHistoryByRack(String rackSerial);
    List<LogisticPackage> getPackageHistoryByLine(String warehouseName, String rackSerial, String lineId);
    List<LogisticPackage> getPackageHistoryByWarehouse(String warehouseName);
    void showWarehouseSnapshot(String warehouseName);
    Optional<String> searchBySerial(String serial); // Returns a location description if found
}
