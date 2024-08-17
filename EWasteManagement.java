import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class EDevice {
    private String name;
    private LocalDate purchaseDate;
    private int expectedLife;
    private boolean isRecycled;

    public EDevice(String name, LocalDate purchaseDate, int expectedLife) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.expectedLife = expectedLife;
        this.isRecycled = false;
    }

    public String getName() {
        return name;
    }

    public boolean needsReplacement(LocalDate currentDate) {
        // A recycled device should no longer be in use or need monitoring
        return !isRecycled && purchaseDate.plusYears(expectedLife).isBefore(currentDate);
    }

    public void recycle() {
        isRecycled = true;
        System.out.println(name + " has been recycled and is now considered unsafe for use.");
    }

    public boolean isRecycled() {
        return isRecycled;
    }

    @Override
    public String toString() {
        return name + " (Purchased: " + purchaseDate + ", Expected Life: " + expectedLife + " years, Recycled: " + isRecycled + ")";
    }
}

class EWasteMonitoringSystem {
    private List<EDevice> devices = new ArrayList<>();

    public void addDevice(EDevice device) {
        devices.add(device);
    }

    public void monitorDevices(LocalDate currentDate) {
        System.out.println("Monitoring Devices as of " + currentDate + ":");
        for (EDevice device : devices) {
            if (device.isRecycled()) {
                System.out.println(device.getName() + " has already been recycled and is unsafe to use.");
            } else if (device.needsReplacement(currentDate)) {
                System.out.println(device.getName() + " needs replacement.");
            } else {
                System.out.println(device.getName() + " is within safe usage period.");
            }
        }
    }

    public void recycleDevice(String deviceName) {
        for (EDevice device : devices) {
            if (device.getName().equalsIgnoreCase(deviceName)) {
                if (device.isRecycled()) {
                    System.out.println("Device " + deviceName + " has already been recycled.");
                } else {
                    device.recycle();
                }
                return;
            }
        }
        System.out.println("Device " + deviceName + " not found.");
    }

    public void showStatistics() {
        int totalDevices = devices.size();
        long recycledDevices = devices.stream().filter(EDevice::isRecycled).count();
        System.out.println("Total Devices: " + totalDevices);
        System.out.println("Recycled Devices: " + recycledDevices);
        System.out.println("Devices in Use: " + (totalDevices - recycledDevices));
    }
}

public class EWasteManagement {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EWasteMonitoringSystem monitoringSystem = new EWasteMonitoringSystem();

        System.out.println("Welcome to the E-Waste Monitoring System!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Device");
            System.out.println("2. Monitor Devices");
            System.out.println("3. Recycle Device");
            System.out.println("4. Show Statistics");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addDevice(scanner, monitoringSystem);
                    break;
                case 2:
                    monitorDevices(scanner, monitoringSystem);
                    break;
                case 3:
                    recycleDevice(scanner, monitoringSystem);
                    break;
                case 4:
                    monitoringSystem.showStatistics();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid Input\nPlease Try Again");
            }
        }
    }

    private static void addDevice(Scanner scanner, EWasteMonitoringSystem monitoringSystem) {
        System.out.print("Enter device name: ");
        String name = scanner.nextLine();

        LocalDate purchaseDate = null;
        while (purchaseDate == null) {
            System.out.print("Enter purchase date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            try {
                purchaseDate = LocalDate.parse(dateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        System.out.print("Enter expected lifespan (in years): ");
        int lifespan = scanner.nextInt();
        scanner.nextLine(); // consume newline

        EDevice device = new EDevice(name, purchaseDate, lifespan);
        monitoringSystem.addDevice(device);
        System.out.println("Device added successfully.");
    }

    private static void monitorDevices(Scanner scanner, EWasteMonitoringSystem monitoringSystem) {
        LocalDate currentDate = LocalDate.now();
        monitoringSystem.monitorDevices(currentDate);
    }

    private static void recycleDevice(Scanner scanner, EWasteMonitoringSystem monitoringSystem) {
        System.out.print("Enter device name to recycle: ");
        String deviceName = scanner.nextLine();
        monitoringSystem.recycleDevice(deviceName);
    }
}
