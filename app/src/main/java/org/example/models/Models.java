package org.example.models;

import java.util.List;

public class Models {
    public record Client(int id, String name, String email) {
    }

    public record Room(int id, int room_number, double price_per_night, boolean booked) {
    }

    public record SingleReservation(int id, String name, String email, int room_number, double price_per_night) {
    }

    public record ReservedRoom(int room_number, double price_per_night) {
    }

    public record Reservation(String name, String email, double total_price, List<ReservedRoom> rooms) {
    }
}
