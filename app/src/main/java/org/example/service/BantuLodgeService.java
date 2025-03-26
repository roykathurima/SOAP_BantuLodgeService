package org.example.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

// local imports
import org.example.dao.Database;
import org.example.models.Models.Client;
import org.example.models.Models.Room;
import org.example.models.Models.SingleReservation;
import org.example.models.Models.Reservation;
import org.example.models.Models.ReservedRoom;

@WebService
public class BantuLodgeService {

    // Service method to add client
    @WebMethod
    public Client addClient(String name, String email) {
        return Database.addClient(name, email);
    }

    @WebMethod
    public Room addRoom(Integer room_number, Double nightly_price) {
        return Database.addRoom(room_number, nightly_price);
    }

    @WebMethod
    public List<Room> getAvailableRooms() {
        return Database.getAvailableRooms();
    }

    @WebMethod
    public static Room bookRoom(Integer id) {
        return Database.bookRoom(id);
    }

    @WebMethod
    public static Reservation getBookedRoomsForUser(String email) {
        List<SingleReservation> reservations = Database.getBookedRoomsForUser(email);
        String name = null;
        double totalCost = 0;
        List<ReservedRoom> reservedRooms = new ArrayList<>();
        for (SingleReservation reservation : reservations) {
            totalCost += reservation.price_per_night();
            if (name == null) {
                name = reservation.name();
            }
            ReservedRoom reservedRoom = new ReservedRoom(reservation.room_number(), reservation.price_per_night());
            reservedRooms.add(reservedRoom);
        }
        assert name != null : "We should have a name by now";
        return new Reservation(name, email, totalCost, reservedRooms);
    }
}
