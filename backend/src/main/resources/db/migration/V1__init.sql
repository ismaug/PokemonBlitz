CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS scores (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    time_seconds INT NOT NULL, -- tiempo en segundos que tomó completar
    correct_pokemon_count INT NOT NULL, -- cantidad de pokémon acertados
    stars INT NOT NULL, -- estrellas obtenidas
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
