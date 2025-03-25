CREATE TABLE Client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE Room (
    id SERIAL PRIMARY KEY,
    room_number INT UNIQUE,
    price_per_night DECIMAL(10,2),
    booked BOOLEAN DEFAULT false  -- false = Available, true = Booked
);

CREATE TABLE ReservedRoom (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES Client(id) NOT NULL,
    room_id INT REFERENCES Room(id) NOT NULL,
);
