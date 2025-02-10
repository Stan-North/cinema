--создание сущности фильм
CREATE TABLE movie (
	id SERIAL PRIMARY KEY ,
	name VARCHAR(30) NOT NULL,
	description VARCHAR(250)
);

--создание сущности место
CREATE TABLE seat (
	id SERIAL PRIMARY KEY,
	title VARCHAR(5) NOT NULL
);

--создание сущности сеанс
CREATE TABLE "session" (
	id SERIAL PRIMARY KEY,
	movie_id INT NOT NULL,
	date TIMESTAMP NOT NULL,
	price NUMERIC(10,2) NOT NULL,
	FOREIGN KEY (movie_id) REFERENCES movie (id)
);

--создание сущности билет
CREATE TABLE ticket (
	id SERIAL PRIMARY KEY,
	seat_id INT NOT NULL,
	session_id INT NOT NULL,
	is_bought BOOLEAN NOT NULL DEFAULT FALSE,
	FOREIGN KEY(seat_id) REFERENCES seat (id),
	FOREIGN KEY(session_id) REFERENCES "session" (id)
);