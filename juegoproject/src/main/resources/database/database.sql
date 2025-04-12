-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nickname TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    racha INTEGER DEFAULT 0,
    puntos INTEGER DEFAULT 0
);

-- Tabla de preguntas
CREATE TABLE IF NOT EXISTS preguntas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    enunciado TEXT NOT NULL,
    opcionA TEXT NOT NULL,
    opcionB TEXT NOT NULL,
    opcionC TEXT NOT NULL,
    opcionD TEXT NOT NULL,
    respuesta_correcta TEXT NOT NULL, 
    dificultad INTEGER NOT NULL 
);

-- Tabla de partidas jugadas por cada usuario
CREATE TABLE IF NOT EXISTS partidas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    puntuacion INTEGER NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
