package org.example.models;

import java.util.List;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class Models {

    @XmlRootElement
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {
        private Integer id;
        private String name;
        private String email;
    }

    @XmlRootElement
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Room {
        private Integer id;
        private Integer room_number;
        private double price_per_night;
        private boolean booked;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleReservation {
        private int id;
        private String name;
        private String email;
        private int room_number;
        private double price_per_night;
    }

    public record ReservedRoom(int room_number, double price_per_night) {
    }

    @XmlRootElement
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reservation {
        private String name;
        private String email;
        private double total_price;
        private List<ReservedRoom> rooms;
    }
}
