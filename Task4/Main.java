import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Room {
    int id;
    String category;
    double price;
    boolean available;

    public Room(int id, String category, double price, boolean available) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    @Override
    public String toString() {
        return "Room ID: " + id + " | Category: " + category + " | Price: $" + price + " | Available: " + available;
    }
}

class Reservation {
    int reservationId;
    String customerName;
    Room room;
    boolean paid;

    public Reservation(int reservationId, String customerName, Room room, boolean paid) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.room = room;
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + " | Customer: " + customerName + " | Room: " + room.category
                + " (ID: " + room.id + ") | Paid: " + paid;
    }
}

class Hotel {
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public ArrayList<Room> searchAvailableRooms(String category) {
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.available) {
                availableRooms.add(r);
            }
        }
        return availableRooms;
    }

    public Reservation makeReservation(String customerName, int roomId) {
        for (Room r : rooms) {
            if (r.id == roomId && r.available) {
                r.available = false;
                Reservation res = new Reservation(reservations.size() + 1, customerName, r, false);
                reservations.add(res);
                return res;
            }
        }
        return null;
    }

    public boolean cancelReservation(int reservationId) {
        for (Reservation res : reservations) {
            if (res.reservationId == reservationId) {
                res.room.available = true;
                reservations.remove(res);
                return true;
            }
        }
        return false;
    }

    public boolean payReservation(int reservationId) {
        for (Reservation res : reservations) {
            if (res.reservationId == reservationId) {
                res.paid = true;
                return true;
            }
        }
        return false;
    }

    public void viewReservationDetails(int reservationId) {
        for (Reservation res : reservations) {
            if (res.reservationId == reservationId) {
                System.out.println("Reservation ID: " + res.reservationId);
                System.out.println("Customer: " + res.customerName);
                System.out.println("Room: " + res.room.category + " (ID: " + res.room.id + ")");
                System.out.println("Price: $" + res.room.price);
                System.out.println("Paid: " + res.paid);
            }
        }
    }

    public void loadRoomsFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(";");
                int id = Integer.parseInt(parts[0]);
                String category = parts[1];
                double price = Double.parseDouble(parts[2]);
                boolean available = Boolean.parseBoolean(parts[3]);
                rooms.add(new Room(id, category, price, available));
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading " + filename);
        }
    }

    public void loadReservationsFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(";");
                int id = Integer.parseInt(parts[0]);
                String customer = parts[1];
                int roomId = Integer.parseInt(parts[2]);
                boolean paid = Boolean.parseBoolean(parts[3]);
                Room room = null;
                for (Room r : rooms) {
                    if (r.id == roomId) {
                        room = r;
                        r.available = false;
                        break;
                    }
                }
                if (room != null) {
                    reservations.add(new Reservation(id, customer, room, paid));
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading " + filename);
        }
    }

    public void saveReservationsToFile(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (Reservation res : reservations) {
                writer.write(res.reservationId + ";" + res.customerName + ";" + res.room.id + ";" + res.paid);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to " + filename);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        File roomsFile = new File("rooms.txt");
        if (!roomsFile.exists()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(roomsFile));
                writer.write("1;Standard;50.0;true\n");
                writer.write("2;Standard;55.0;true\n");
                writer.write("3;Deluxe;80.0;true\n");
                writer.write("4;Deluxe;85.0;true\n");
                writer.write("5;Suite;150.0;true\n");
                writer.write("6;Suite;180.0;true\n");
                writer.close();
                System.out.println("rooms.txt created with default rooms.");
            } catch (IOException e) {
                System.out.println("Error creating rooms.txt");
            }
        }

        hotel.loadRoomsFromFile("rooms.txt");
        hotel.loadReservationsFromFile("reservations.txt");

        while (true) {
            System.out.println("\n=== HOTEL RESERVATION SYSTEM ===");
            System.out.println("1. Search available rooms");
            System.out.println("2. Make a reservation");
            System.out.println("3. Cancel a reservation");
            System.out.println("4. Pay for a reservation");
            System.out.println("5. View reservation details");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String cat = scanner.nextLine();
                    ArrayList<Room> available = hotel.searchAvailableRooms(cat);
                    if (available.isEmpty()) {
                        System.out.println("No rooms available in this category.");
                    } else {
                        for (Room r : available) System.out.println(r);
                    }
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Room ID to book: ");
                    int roomId = scanner.nextInt();
                    Reservation res = hotel.makeReservation(name, roomId);
                    if (res != null) {
                        System.out.println("Reservation successful! ID: " + res.reservationId);
                        hotel.saveReservationsToFile("reservations.txt");
                    } else System.out.println("Reservation failed. Room may be unavailable.");
                    break;
                case 3:
                    System.out.print("Enter reservation ID to cancel: ");
                    int cancelId = scanner.nextInt();
                    if (hotel.cancelReservation(cancelId)) {
                        System.out.println("Reservation cancelled.");
                        hotel.saveReservationsToFile("reservations.txt");
                    } else System.out.println("Reservation not found.");
                    break;
                case 4:
                    System.out.print("Enter reservation ID to pay: ");
                    int payId = scanner.nextInt();
                    if (!hotel.payReservation(payId)) {
                        System.out.println("Reservation not found.");
                    } else {
                        hotel.saveReservationsToFile("reservations.txt");
                        System.out.println("Payment successful! Your reservation is now paid.");
                    }
                    break;
                case 5:
                    System.out.print("Enter reservation ID to view details: ");
                    int viewId = scanner.nextInt();
                    hotel.viewReservationDetails(viewId);
                    break;
                case 6:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}                   
Add Task4
