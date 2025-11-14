DELETE FROM movie_actors;
DELETE FROM movie_genres;
DELETE FROM actors;
DELETE FROM genres;
DELETE FROM movies;

-- Insert Genres WITHOUT explicit IDs
INSERT INTO genres (name) VALUES ('Action');
INSERT INTO genres (name) VALUES ('Drama');
INSERT INTO genres (name) VALUES ('Comedy');
INSERT INTO genres (name) VALUES ('Sci-Fi');
INSERT INTO genres (name) VALUES ('Thriller');
INSERT INTO genres (name) VALUES ('Horror');
INSERT INTO genres (name) VALUES ('Romance');
INSERT INTO genres (name) VALUES ('Adventure');
INSERT INTO genres (name) VALUES ('Fantasy');
INSERT INTO genres (name) VALUES ('Crime');
INSERT INTO genres (name) VALUES ('Mystery');
INSERT INTO genres (name) VALUES ('Animation');
INSERT INTO genres (name) VALUES ('Family');
INSERT INTO genres (name) VALUES ('Biography');
INSERT INTO genres (name) VALUES ('History');
INSERT INTO genres (name) VALUES ('War');
INSERT INTO genres (name) VALUES ('Musical');
INSERT INTO genres (name) VALUES ('Western');

-- Insert Actors WITHOUT explicit IDs
INSERT INTO actors (name, birth_date) VALUES ('Tom Hanks', '1956-07-09');
INSERT INTO actors (name, birth_date) VALUES ('Meryl Streep', '1949-06-22');
INSERT INTO actors (name, birth_date) VALUES ('Leonardo DiCaprio', '1974-11-11');
INSERT INTO actors (name, birth_date) VALUES ('Jennifer Lawrence', '1990-08-15');
INSERT INTO actors (name, birth_date) VALUES ('Denzel Washington', '1954-12-28');
INSERT INTO actors (name, birth_date) VALUES ('Emma Watson', '1990-04-15');
INSERT INTO actors (name, birth_date) VALUES ('Robert Downey Jr.', '1965-04-04');
INSERT INTO actors (name, birth_date) VALUES ('Scarlett Johansson', '1984-11-22');
INSERT INTO actors (name, birth_date) VALUES ('Brad Pitt', '1963-12-18');
INSERT INTO actors (name, birth_date) VALUES ('Angelina Jolie', '1975-06-04');
INSERT INTO actors (name, birth_date) VALUES ('Morgan Freeman', '1937-06-01');
INSERT INTO actors (name, birth_date) VALUES ('Samuel L. Jackson', '1948-12-21');
INSERT INTO actors (name, birth_date) VALUES ('Natalie Portman', '1981-06-09');
INSERT INTO actors (name, birth_date) VALUES ('Chris Hemsworth', '1983-08-11');
INSERT INTO actors (name, birth_date) VALUES ('Chris Evans', '1981-06-13');
INSERT INTO actors (name, birth_date) VALUES ('Matt Damon', '1970-10-08');
INSERT INTO actors (name, birth_date) VALUES ('Christian Bale', '1974-01-30');
INSERT INTO actors (name, birth_date) VALUES ('Anne Hathaway', '1982-11-12');
INSERT INTO actors (name, birth_date) VALUES ('Ryan Gosling', '1980-11-12');
INSERT INTO actors (name, birth_date) VALUES ('Emma Stone', '1988-11-06');
INSERT INTO actors (name, birth_date) VALUES ('Daniel Day-Lewis', '1957-04-29');
INSERT INTO actors (name, birth_date) VALUES ('Cate Blanchett', '1969-05-14');
INSERT INTO actors (name, birth_date) VALUES ('Johnny Depp', '1963-06-09');
INSERT INTO actors (name, birth_date) VALUES ('Will Smith', '1968-09-25');
INSERT INTO actors (name, birth_date) VALUES ('Tom Hardy', '1977-09-15');
INSERT INTO actors (name, birth_date) VALUES ('Margot Robbie', '1990-07-02');
INSERT INTO actors (name, birth_date) VALUES ('Michael B. Jordan', '1987-02-09');
INSERT INTO actors (name, birth_date) VALUES ('Zendaya', '1996-09-01');
INSERT INTO actors (name, birth_date) VALUES ('Timothée Chalamet', '1995-12-27');
INSERT INTO actors (name, birth_date) VALUES ('Florence Pugh', '1996-01-03');

-- Insert Movies WITHOUT explicit IDs
-- Classic Movies
INSERT INTO movies (title, release_year, duration) VALUES ('The Shawshank Redemption', 1994, 142);
INSERT INTO movies (title, release_year, duration) VALUES ('The Godfather', 1972, 175);
INSERT INTO movies (title, release_year, duration) VALUES ('The Godfather: Part II', 1974, 202);
INSERT INTO movies (title, release_year, duration) VALUES ('The Dark Knight', 2008, 152);
INSERT INTO movies (title, release_year, duration) VALUES ('Pulp Fiction', 1994, 154);
INSERT INTO movies (title, release_year, duration) VALUES ('Forrest Gump', 1994, 142);
INSERT INTO movies (title, release_year, duration) VALUES ('Inception', 2010, 148);
INSERT INTO movies (title, release_year, duration) VALUES ('The Matrix', 1999, 136);
INSERT INTO movies (title, release_year, duration) VALUES ('Goodfellas', 1990, 146);
INSERT INTO movies (title, release_year, duration) VALUES ('The Silence of the Lambs', 1991, 118);

-- 2000s Movies
INSERT INTO movies (title, release_year, duration) VALUES ('Saving Private Ryan', 1998, 169);
INSERT INTO movies (title, release_year, duration) VALUES ('Gladiator', 2000, 155);
INSERT INTO movies (title, release_year, duration) VALUES ('The Lord of the Rings: The Fellowship of the Ring', 2001, 178);
INSERT INTO movies (title, release_year, duration) VALUES ('The Lord of the Rings: The Two Towers', 2002, 179);
INSERT INTO movies (title, release_year, duration) VALUES ('The Lord of the Rings: The Return of the King', 2003, 201);
INSERT INTO movies (title, release_year, duration) VALUES ('The Departed', 2006, 151);
INSERT INTO movies (title, release_year, duration) VALUES ('The Prestige', 2006, 130);
INSERT INTO movies (title, release_year, duration) VALUES ('No Country for Old Men', 2007, 122);
INSERT INTO movies (title, release_year, duration) VALUES ('There Will Be Blood', 2007, 158);

-- 2010s Movies
INSERT INTO movies (title, release_year, duration) VALUES ('The Social Network', 2010, 120);
INSERT INTO movies (title, release_year, duration) VALUES ('Black Swan', 2010, 108);
INSERT INTO movies (title, release_year, duration) VALUES ('The King''s Speech', 2010, 118);
INSERT INTO movies (title, release_year, duration) VALUES ('Drive', 2011, 100);
INSERT INTO movies (title, release_year, duration) VALUES ('The Avengers', 2012, 143);
INSERT INTO movies (title, release_year, duration) VALUES ('Django Unchained', 2012, 165);
INSERT INTO movies (title, release_year, duration) VALUES ('Silver Linings Playbook', 2012, 122);
INSERT INTO movies (title, release_year, duration) VALUES ('Gravity', 2013, 91);
INSERT INTO movies (title, release_year, duration) VALUES ('12 Years a Slave', 2013, 134);
INSERT INTO movies (title, release_year, duration) VALUES ('The Wolf of Wall Street', 2013, 180);
INSERT INTO movies (title, release_year, duration) VALUES ('Interstellar', 2014, 169);
INSERT INTO movies (title, release_year, duration) VALUES ('The Grand Budapest Hotel', 2014, 99);
INSERT INTO movies (title, release_year, duration) VALUES ('Mad Max: Fury Road', 2015, 120);
INSERT INTO movies (title, release_year, duration) VALUES ('The Revenant', 2015, 156);
INSERT INTO movies (title, release_year, duration) VALUES ('Spotlight', 2015, 128);
INSERT INTO movies (title, release_year, duration) VALUES ('La La Land', 2016, 128);
INSERT INTO movies (title, release_year, duration) VALUES ('Arrival', 2016, 116);
INSERT INTO movies (title, release_year, duration) VALUES ('Get Out', 2017, 104);
INSERT INTO movies (title, release_year, duration) VALUES ('Dunkirk', 2017, 106);
INSERT INTO movies (title, release_year, duration) VALUES ('Black Panther', 2018, 134);
INSERT INTO movies (title, release_year, duration) VALUES ('A Star is Born', 2018, 136);
INSERT INTO movies (title, release_year, duration) VALUES ('Parasite', 2019, 132);
INSERT INTO movies (title, release_year, duration) VALUES ('Joker', 2019, 122);
INSERT INTO movies (title, release_year, duration) VALUES ('Once Upon a Time in Hollywood', 2019, 161);

-- 2020s Movies
INSERT INTO movies (title, release_year, duration) VALUES ('Tenet', 2020, 150);
INSERT INTO movies (title, release_year, duration) VALUES ('Dune', 2021, 155);
INSERT INTO movies (title, release_year, duration) VALUES ('Spider-Man: No Way Home', 2021, 148);
INSERT INTO movies (title, release_year, duration) VALUES ('The Batman', 2022, 176);
INSERT INTO movies (title, release_year, duration) VALUES ('Top Gun: Maverick', 2022, 130);
INSERT INTO movies (title, release_year, duration) VALUES ('Everything Everywhere All at Once', 2022, 139);
INSERT INTO movies (title, release_year, duration) VALUES ('Oppenheimer', 2023, 180);

-- Additional Classics
INSERT INTO movies (title, release_year, duration) VALUES ('Jurassic Park', 1993, 127);
INSERT INTO movies (title, release_year, duration) VALUES ('Titanic', 1997, 195);
INSERT INTO movies (title, release_year, duration) VALUES ('Fight Club', 1999, 139);
INSERT INTO movies (title, release_year, duration) VALUES ('The Lion King', 1994, 88);
INSERT INTO movies (title, release_year, duration) VALUES ('Spirited Away', 2001, 125);

-- Insert Movie-Genre relationships (FIXED: No duplicates)
-- Drama/Crime Classics
INSERT INTO movie_genres (movie_id, genre_id) VALUES (1, 2);  -- Shawshank: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (1, 10); -- Shawshank: Crime
INSERT INTO movie_genres (movie_id, genre_id) VALUES (2, 2);  -- Godfather: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (2, 10); -- Godfather: Crime
INSERT INTO movie_genres (movie_id, genre_id) VALUES (3, 2);  -- Godfather II: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (3, 10); -- Godfather II: Crime
INSERT INTO movie_genres (movie_id, genre_id) VALUES (5, 2);  -- Pulp Fiction: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (5, 10); -- Pulp Fiction: Crime
INSERT INTO movie_genres (movie_id, genre_id) VALUES (9, 2);  -- Goodfellas: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (9, 10); -- Goodfellas: Crime

-- Action/Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (4, 1);  -- Dark Knight: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (4, 2);  -- Dark Knight: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (4, 10); -- Dark Knight: Crime
INSERT INTO movie_genres (movie_id, genre_id) VALUES (7, 1);  -- Inception: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (7, 4);  -- Inception: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (8, 1);  -- Matrix: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (8, 4);  -- Matrix: Sci-Fi

-- Thriller/Horror
INSERT INTO movie_genres (movie_id, genre_id) VALUES (10, 5); -- Silence: Thriller
INSERT INTO movie_genres (movie_id, genre_id) VALUES (10, 11); -- Silence: Mystery

-- War/History
INSERT INTO movie_genres (movie_id, genre_id) VALUES (11, 1); -- Saving Ryan: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (11, 2); -- Saving Ryan: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (11, 16); -- Saving Ryan: War
INSERT INTO movie_genres (movie_id, genre_id) VALUES (12, 1); -- Gladiator: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (12, 2); -- Gladiator: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (12, 15); -- Gladiator: History

-- Fantasy/Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (13, 8); -- LOTR1: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (13, 9); -- LOTR1: Fantasy
INSERT INTO movie_genres (movie_id, genre_id) VALUES (14, 8); -- LOTR2: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (14, 9); -- LOTR2: Fantasy
INSERT INTO movie_genres (movie_id, genre_id) VALUES (15, 8); -- LOTR3: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (15, 9); -- LOTR3: Fantasy

-- More 2010s Movies
INSERT INTO movie_genres (movie_id, genre_id) VALUES (20, 2); -- Social Network: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (20, 14); -- Social Network: Biography
INSERT INTO movie_genres (movie_id, genre_id) VALUES (21, 2); -- Black Swan: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (21, 5); -- Black Swan: Thriller
INSERT INTO movie_genres (movie_id, genre_id) VALUES (24, 1); -- Drive: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (24, 2); -- Drive: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (24, 5); -- Drive: Thriller
INSERT INTO movie_genres (movie_id, genre_id) VALUES (25, 1); -- Avengers: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (25, 4); -- Avengers: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (25, 8); -- Avengers: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (26, 2); -- Django: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (26, 8); -- Django: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (27, 2); -- Silver Linings: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (27, 3); -- Silver Linings: Comedy
INSERT INTO movie_genres (movie_id, genre_id) VALUES (27, 7); -- Silver Linings: Romance
INSERT INTO movie_genres (movie_id, genre_id) VALUES (31, 4); -- Interstellar: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (31, 2); -- Interstellar: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (31, 8); -- Interstellar: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (32, 3); -- Grand Budapest: Comedy
INSERT INTO movie_genres (movie_id, genre_id) VALUES (32, 2); -- Grand Budapest: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (33, 1); -- Mad Max: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (33, 4); -- Mad Max: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (34, 2); -- Revenant: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (34, 8); -- Revenant: Adventure
INSERT INTO movie_genres (movie_id, genre_id) VALUES (36, 2); -- La La Land: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (36, 7); -- La La Land: Romance
INSERT INTO movie_genres (movie_id, genre_id) VALUES (36, 17); -- La La Land: Musical
INSERT INTO movie_genres (movie_id, genre_id) VALUES (37, 4); -- Arrival: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (37, 2); -- Arrival: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (38, 5); -- Get Out: Thriller
INSERT INTO movie_genres (movie_id, genre_id) VALUES (38, 6); -- Get Out: Horror
INSERT INTO movie_genres (movie_id, genre_id) VALUES (39, 1); -- Dunkirk: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (39, 2); -- Dunkirk: Drama
INSERT INTO movie_genres (movie_id, genre_id) VALUES (39, 16); -- Dunkirk: War
INSERT INTO movie_genres (movie_id, genre_id) VALUES (40, 1); -- Black Panther: Action
INSERT INTO movie_genres (movie_id, genre_id) VALUES (40, 4); -- Black Panther: Sci-Fi
INSERT INTO movie_genres (movie_id, genre_id) VALUES (40, 8); -- Black Panther: Adventure

-- Insert Movie-Actor relationships
-- Tom Hanks movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (1, 1);   -- Shawshank
INSERT INTO movie_actors (movie_id, actor_id) VALUES (6, 1);   -- Forrest Gump
INSERT INTO movie_actors (movie_id, actor_id) VALUES (11, 1);  -- Saving Private Ryan

-- Leonardo DiCaprio movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (7, 3);   -- Inception
INSERT INTO movie_actors (movie_id, actor_id) VALUES (26, 3);  -- Django Unchained
INSERT INTO movie_actors (movie_id, actor_id) VALUES (30, 3);  -- Wolf of Wall Street
INSERT INTO movie_actors (movie_id, actor_id) VALUES (34, 3);  -- The Revenant
INSERT INTO movie_actors (movie_id, actor_id) VALUES (45, 3);  -- Once Upon a Time

-- Robert Downey Jr. movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (25, 7);  -- Avengers
INSERT INTO movie_actors (movie_id, actor_id) VALUES (48, 7);  -- Oppenheimer

-- Christian Bale movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (4, 17);  -- Dark Knight
INSERT INTO movie_actors (movie_id, actor_id) VALUES (17, 17); -- The Prestige

-- Jennifer Lawrence movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (27, 4);  -- Silver Linings
INSERT INTO movie_actors (movie_id, actor_id) VALUES (41, 4);  -- A Star is Born

-- Chris Hemsworth movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (25, 14); -- Avengers

-- Chris Evans movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (25, 15); -- Avengers

-- Scarlett Johansson movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (25, 8);  -- Avengers

-- Brad Pitt movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (5, 9);   -- Pulp Fiction
INSERT INTO movie_actors (movie_id, actor_id) VALUES (45, 9);  -- Once Upon a Time

-- Morgan Freeman movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (1, 11);  -- Shawshank
INSERT INTO movie_actors (movie_id, actor_id) VALUES (4, 11);  -- Dark Knight

-- Samuel L. Jackson movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (5, 12);  -- Pulp Fiction
INSERT INTO movie_actors (movie_id, actor_id) VALUES (25, 12); -- Avengers

-- Natalie Portman movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (21, 13); -- Black Swan

-- Ryan Gosling movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (24, 19); -- Drive
INSERT INTO movie_actors (movie_id, actor_id) VALUES (36, 19); -- La La Land

-- Emma Stone movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (36, 20); -- La La Land

-- Denzel Washington movies
INSERT INTO movie_actors (movie_id, actor_id) VALUES (40, 5);  -- Black Panther cameo

-- Newer Actors
INSERT INTO movie_actors (movie_id, actor_id) VALUES (40, 27); -- Black Panther: Michael B. Jordan
INSERT INTO movie_actors (movie_id, actor_id) VALUES (46, 28); -- Dune: Zendaya
INSERT INTO movie_actors (movie_id, actor_id) VALUES (46, 29); -- Dune: Timothée Chalamet
INSERT INTO movie_actors (movie_id, actor_id) VALUES (51, 30); -- Dune 2: Florence Pugh

-- Additional relationships for better recommendations
INSERT INTO movie_actors (movie_id, actor_id) VALUES (13, 22);  -- LOTR: Cate Blanchett
INSERT INTO movie_actors (movie_id, actor_id) VALUES (26, 23);  -- Django: Johnny Depp cameo
INSERT INTO movie_actors (movie_id, actor_id) VALUES (33, 25);  -- Mad Max: Tom Hardy
INSERT INTO movie_actors (movie_id, actor_id) VALUES (45, 26);  -- Once Upon a Time: Margot Robbie