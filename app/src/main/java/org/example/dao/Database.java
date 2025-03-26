package org.example.dao;

import org.jdbi.v3.core.Jdbi;

// statuc makes the class act like a utility class
public class Database {
    private static final Jdbi jdbi = Jdbi.create("url", "db_name", "db_password");

    // Add a user (client)
    // TODO: Should return the created user
    public static void addClient(String name, String email) {
        jdbi.useHandle(handle -> {
            handle.execute("INSERT INTO clients (name, email) VALUES (?, ?);", name, email);
        });
    }

    // Add rooms as an admin possibly
    // TODO: should return the created room
    public static void addRoom(Integer room_number, Double nightly_price) {
        jdbi.useHandle(handle -> {
            handle.execute("INSERT INTO room (room_number, price_per_night) VALUES (?, ?);", room_number,
                    nightly_price);
        });
    }

    // return a list of available rooms
    // TODO: Does not return anything yet; add a record(data class)
    public static void getAvailableRooms() {
        jdbi.useHandle(handle -> {
            handle.execute("SELECT * FROM room WHERE booked = FALSE;");
        });
    }

    // Book room
    // TODO: Should return the booked room
    // Not sure if the id is an int or a string, guess well finf out soon
    public static void bookRoom(Integer id) {
        jdbi.useHandle(handle -> {
            handle.execute("UPDATE room SET booked = TRUE WHERE id = ?;", id);
        });
    }

    // Get a list of booked rooms
    // the service that will call this class will handle the logic of adding up the
    // totals
    // TODO: Should return a list of all the booked rooms for a given user (id user
    // by email)
    public static void getBookedRoomsForUser(String email) {
        jdbi.useHandle(handle -> {
            handle.execute("SELECT r.* FROM reservedroom r INNER JOIN client c ON c.id = r.client_id WHERE c.email = ?",
                    email);
        });
    }
}
