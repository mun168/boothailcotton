# boothailcotton


Company X Logistics Management Application
This command-line Java application automates logistics operations for Company X using modern Java best practices such as interfaces, records, enums, and a modular package structure. The application allows you to load packages, offload or discard them, view history and snapshots, and search by serial number—all via a simple text-based menu.


--- Company X Logistics Management ---
1. Load Package
2. Offload Package
3. Discard Package
4. Change Offloading Order
5. Show Package History
6. Show Warehouse Snapshot
7. Search Package or Rack by Serial
8. Exit
Detailed Steps
Load Package

Step 1: Choose option 1.
Step 2: Enter the package type (loose or carton).
Step 3: Provide the package serial number and mass.
Step 4: Quality Selection: When prompted, enter a number:
1 for STANDARD
2 for PREMIUM
3 for SPECIAL
An invalid number defaults to STANDARD.
Step 5: Enter the warehouse name, rack serial, and line id.
Step 6: For loose packages, provide the pallet serial number.
Note: If the warehouse, rack, or line does not exist, the application automatically creates them using default settings.
Offload Package

Choose option 2 and enter the package serial number to offload it.
Packages are offloaded using the current offloading order (default is OLDEST_FIRST).
Discard Package

Choose option 3 to discard a package by entering its serial number.
Change Offloading Order

Choose option 4.
Offloading Order Selection: Enter 1 for OLDEST_FIRST or 2 for NEWEST_FIRST.
An invalid input will result in no changes.
Show Package History

Choose option 5.
View history by selecting:
1 for Rack history
2 for Line history
3 for Warehouse history
Show Warehouse Snapshot

Choose option 6 and enter the warehouse name to view its snapshot, including package details and capacity utilization.
Search Package or Rack

Choose option 7 and enter a serial number.
The application will display the location (warehouse, rack, line, or pallet) if a match is found.
Exit

Choose option 8 to exit the application.
