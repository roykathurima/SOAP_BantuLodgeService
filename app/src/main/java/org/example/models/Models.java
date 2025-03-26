
package org.example.models;

public class Models {
    public record Client(int id, String name, String email) {
    }

    public record Room(int id, int room_number, double price_per_night, boolean booked) {
    }

    public record Reservation(int id, int client_id, int room_id) {
    }
}
