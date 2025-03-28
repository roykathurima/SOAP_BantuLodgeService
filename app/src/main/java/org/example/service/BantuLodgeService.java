package org.example.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;

// local imports
import org.example.dao.Database;
import org.example.models.Models;
import org.example.models.Models.Client;
import org.example.models.Models.Room;
import org.example.models.Models.SingleReservation;
import org.example.models.Models.Reservation;
import org.example.models.Models.ReservedRoom;

@WebService
public class BantuLodgeService {

    // Service method to add client
    @WebMethod
    @WebResult(name = "client")
    public Client addClient(@WebParam(name = "clientName") String name, @WebParam(name = "userEmail") String email) {
        return Database.addClient(name, email);
    }

    @WebMethod
    @WebResult(name = "room")
    public Room addRoom(@WebParam(name = "roomNumber") Integer room_number,
            @WebParam(name = "pricePerNight") Double nightly_price) {
        return Database.addRoom(room_number, nightly_price);
    }

    @WebMethod
    @WebResult(name = "rooms")
    public List<Room> getAvailableRooms() {
        return Database.getAvailableRooms();
    }

    @WebMethod
    @WebResult(name = "bookedRoom")
    public Room bookRoom(@WebParam(name = "roomId") Integer id, @WebParam(name = "userEmail") String email) {
        return Database.bookRoom(id, email);
    }

    @WebMethod
    @WebResult(name = "bookedRooms")
    public Reservation getBookedRoomsForUser(@WebParam(name = "userEmail") String email) {
        List<SingleReservation> reservations = Database.getBookedRoomsForUser(email);
        String name = null;
        double totalCost = 0;
        List<ReservedRoom> reservedRooms = new ArrayList<>();
        for (SingleReservation reservation : reservations) {
            totalCost += reservation.getPrice_per_night();
            if (name == null) {
                name = reservation.getName();
            }
            ReservedRoom reservedRoom = new Models.ReservedRoom(reservation.getRoom_number(),
                    reservation.getPrice_per_night());
            reservedRooms.add(reservedRoom);
        }
        assert name != null : "We should have a name by now";
        return new Models.Reservation(name, email, totalCost, reservedRooms);
    }
}
