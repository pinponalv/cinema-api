-- Crear permisos
INSERT IGNORE INTO permissions (id, permission_name) VALUES (1, 'CREATE');
INSERT IGNORE INTO permissions (id, permission_name) VALUES (2, 'READ');
INSERT IGNORE INTO permissions (id, permission_name) VALUES (3, 'UPDATE');
INSERT IGNORE INTO permissions (id, permission_name) VALUES (4, 'DELETE');

--CREAR Roles
INSERT IGNORE INTO roles (id, role) VALUES (1, 'ADMIN');
INSERT IGNORE INTO roles (id, role) VALUES (2, 'CONTENT_MANAGER');

--Asigno permisos a roles
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (1, 1);
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (1, 2);
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (1, 3);
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (1, 4);
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (2, 1);
INSERT IGNORE INTO roles_permission (role_id, permission_id) VALUES (2, 3);

-- CREO USUARIO ADMIN POR DEFECTO (password: admin123 encriptado con BCrypt)
-- $2a$10$hWnIHKErmO.90jIEdYeffOldIbJmAZV3ceoPkjgfQSg3Z8Fbn2HyG = hash de admin123
INSERT IGNORE INTO users (id, email, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES (1, 'admin@cinema.com', 'camila', '$2a$10$hWnIHKErmO.90jIEdYeffOldIbJmAZV3ceoPkjgfQSg3Z8Fbn2HyG', true, true, true, true);

--Asigno rol admin al usuario
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Crear peliculas
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (1, 'Spiderman Brand New Day', 'Las exigencias sobre Spider-Man se intensifican y desencadenan una sorprendente evolucion que pone en peligro su existencia, mientras enfrenta una de las amenazas mas poderosas que ha conocido.', 'accion');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (2, 'The Godfather', 'La cronica de la familia Corleone bajo el patriarca Vito Corleone.', 'drama');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (3, 'The Dark Knight', 'Batman enfrenta al Joker, un criminal que quiere sumir a Gotham en el caos.', 'accion');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (4, 'Pulp Fiction', 'Las vidas de dos sicarios, un boxeador y una pareja de ladrones se entrelazan en Los Angeles.', 'crimen');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (5, 'Interstellar', 'Un grupo de exploradores viaja a traves de un agujero de gusano en busca de un nuevo hogar para la humanidad.', 'ciencia ficcion');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (6, 'The Conjuring', 'Investigadores paranormales ayudan a una familia aterrorizada por una presencia oscura en su granja.', 'terror');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (7, 'The Hangover', 'Tres amigos se despiertan sin recordar nada tras una despedida de soltero en Las Vegas.', 'comedia');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (8, 'Titanic', 'Un romance nace entre dos jovenes de distintas clases sociales a bordo del Titanic.', 'romance');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (9, 'The Lord of the Rings: The Fellowship of the Ring', 'Un hobbit emprende un viaje para destruir un anillo con el poder de dominar el mundo.', 'aventura');
INSERT IGNORE INTO movies (id, title, description, film_genre) VALUES (10, 'John Wick', 'Un ex asesino a sueldo busca venganza contra quienes le quitaron todo.', 'accion');
