package etbs;

import java.io.*;
import java.util.*;

public class BookingCSVManager {
    private String filePath;

    public BookingCSVManager(String filePath) {
        this.filePath = filePath;
    }

    public void addBooking(String bookingId, String userId, int eventId, int quantity, double price) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            pw.printf("%s,%s,%d,%d,%.2f%n", bookingId, userId, eventId, quantity, price);
        } catch (IOException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    public List<String> loadBookings() {
        List<String> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    bookings.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    public boolean removeBooking(String bookingId) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            if (header != null) lines.add(header);
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    if (line.startsWith(bookingId + ",")) {
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading bookings: " + e.getMessage());
        }

        if (found) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                for (String l : lines) {
                    pw.println(l);
                }
            } catch (IOException e) {
                System.err.println("Error writing bookings: " + e.getMessage());
            }
        }
        return found;
    }
}
