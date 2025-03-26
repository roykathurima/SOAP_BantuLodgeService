package org.example.dao;

import org.jdbi.v3.core.Jdbi;
import java.util.List;
import java.util.Optional;
import io.github.cdimascio.dotenv.Dotenv;

// local imports
import org.example.models.Models.Room;
import org.example.models.Models.Client;
import org.example.models.Models.SingleReservation;

// statuc makes the class act like a utility class
public class Database {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    private static final Jdbi jdbi = Jdbi.create(URL, USER, PASSWORD);

    // Add a user (client)
    public static Client addClient(String name, String email) {
        return jdbi.withHandle(handle -> {
            return handle.createUpdate("INSERT INTO client (name, email) VALUES (:name, :email) RETURNING *;")
                    .bind("name", name)
                    .bind("email", email)
                    .executeAndReturnGeneratedKeys()
                    .mapToBean(Client.class)
                    .one();
        });
    }

    // Add rooms as an admin possibly
    public static Room addRoom(Integer room_number, Double nightly_price) {
        return jdbi.withHandle(handle -> {
            return handle.createUpdate(
                    "INSERT INTO room (room_number, price_per_night) VALUES (:room_number, :nightly_price) RETURNING *;")
                    .bind("room_number", room_number)
                    .bind("nightly_price", nightly_price)
                    .executeAndReturnGeneratedKeys()
                    .mapToBean(Room.class)
                    .one();
        });
    }

    // return a list of available rooms
    public static List<Room> getAvailableRooms() {
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM room WHERE booked = FALSE;")
                    .mapToBean(Room.class)
                    .list();
        });
    }

    // Book room, we should also know which user booked the room; user to supply
    // email
    public static Room bookRoom(Integer id, String email) {
        return jdbi.withHandle(handle -> {
            Optional<Room> bookedRoomOpt = handle
                    .createQuery("UPDATE room SET booked = TRUE WHERE id = :id RETURNING *;")
                    .bind("id", id)
                    .mapToBean(Room.class)
                    .findOne();
            Optional<Integer> userIdOpt = handle.createQuery("SELECT id FROM client WHERE email = :email")
                    .bind("email", email)
                    .mapTo(Integer.class)
                    .findOne();
            if (bookedRoomOpt.isEmpty()) {
                throw new IllegalArgumentException("Room not found");
            }
            if (userIdOpt.isEmpty()) {
                throw new IllegalArgumentException("User not found with email: " + email);
            }

            Room bookedRoom = bookedRoomOpt.get();
            Integer userId = userIdOpt.get();

            handle.createUpdate("INSERT INTO reservedroom (client_id, room_id) VALUES (:client_id, :room_id);")
                    .bind("client_id", userId)
                    .bind("room_id", bookedRoom.getId())
                    .execute();

            return bookedRoom;

        });
    }

    // Get a list of booked rooms
    // the service that will call this class will handle the logic of adding up the
    // totals
    public static List<SingleReservation> getBookedRoomsForUser(String email) {
        System.out.println("We are going in....");
        List<SingleReservation> reservations = jdbi.withHandle(handle -> handle.createQuery(
                "SELECT r.id, c.email, c.name, rm.room_number, rm.price_per_night FROM reservedroom r INNER JOIN client c ON c.id = r.client_id INNER JOIN room rm ON rm.id = r.room_id WHERE c.email = :email;")
                .bind("email", email)
                .mapToBean(SingleReservation.class)
                .list());
        System.out.println("We went in and came back..");
        System.out.println(reservations);
        return reservations;
    }
}
