-- MovieDB enrichment layer
-- This file runs after data.sql and upgrades the dataset with richer database fields.

UPDATE movies SET director='Frank Darabont', language='English', country='United States', imdb_rating=9.3, mpaa_rating='R', overview='Two imprisoned men bond over years, finding dignity and quiet resistance inside a brutal prison system.' WHERE title='The Shawshank Redemption';
UPDATE movies SET director='Francis Ford Coppola', language='English', country='United States', imdb_rating=9.2, mpaa_rating='R', overview='A crime family transfers power from an aging patriarch to his reluctant son.' WHERE title='The Godfather';
UPDATE movies SET director='Francis Ford Coppola', language='English', country='United States', imdb_rating=9.0, mpaa_rating='R', overview='The Corleone family legacy expands through parallel stories of power, immigration, and moral decay.' WHERE title='The Godfather: Part II';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=9.0, mpaa_rating='PG-13', overview='Batman faces a criminal force that turns Gotham against itself.' WHERE title='The Dark Knight';
UPDATE movies SET director='Quentin Tarantino', language='English', country='United States', imdb_rating=8.9, mpaa_rating='R', overview='Intersecting crime stories collide through violence, wit, and fractured time.' WHERE title='Pulp Fiction';
UPDATE movies SET director='Robert Zemeckis', language='English', country='United States', imdb_rating=8.8, mpaa_rating='PG-13', overview='A gentle man moves through decades of American history while holding onto love and innocence.' WHERE title='Forrest Gump';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=8.8, mpaa_rating='PG-13', overview='A thief who steals secrets through dreams is offered a chance to erase his past.' WHERE title='Inception';
UPDATE movies SET director='Lana Wachowski, Lilly Wachowski', language='English', country='United States', imdb_rating=8.7, mpaa_rating='R', overview='A hacker discovers reality is a controlled simulation and joins a rebellion against its machines.' WHERE title='The Matrix';
UPDATE movies SET director='Martin Scorsese', language='English', country='United States', imdb_rating=8.7, mpaa_rating='R', overview='The rise and fall of a mob associate reveals the cost of loyalty, greed, and violence.' WHERE title='Goodfellas';
UPDATE movies SET director='Jonathan Demme', language='English', country='United States', imdb_rating=8.6, mpaa_rating='R', overview='A young FBI trainee seeks help from a jailed killer to catch another murderer.' WHERE title='The Silence of the Lambs';
UPDATE movies SET director='Steven Spielberg', language='English', country='United States', imdb_rating=8.6, mpaa_rating='R', overview='A World War II rescue mission tests duty, sacrifice, and the value of one life.' WHERE title='Saving Private Ryan';
UPDATE movies SET director='Ridley Scott', language='English', country='United States', imdb_rating=8.5, mpaa_rating='R', overview='A betrayed Roman general becomes a gladiator and challenges an empire.' WHERE title='Gladiator';
UPDATE movies SET director='Peter Jackson', language='English', country='New Zealand', imdb_rating=8.9, mpaa_rating='PG-13', overview='A fellowship forms to destroy a ring that could return darkness to Middle-earth.' WHERE title='The Lord of the Rings: The Fellowship of the Ring';
UPDATE movies SET director='Peter Jackson', language='English', country='New Zealand', imdb_rating=8.8, mpaa_rating='PG-13', overview='The fellowship scatters while the war for Middle-earth grows.' WHERE title='The Lord of the Rings: The Two Towers';
UPDATE movies SET director='Peter Jackson', language='English', country='New Zealand', imdb_rating=9.0, mpaa_rating='PG-13', overview='The final battle for Middle-earth decides the fate of the ring and its bearer.' WHERE title='The Lord of the Rings: The Return of the King';
UPDATE movies SET director='Martin Scorsese', language='English', country='United States', imdb_rating=8.5, mpaa_rating='R', overview='An undercover cop and a mole inside the police try to expose each other.' WHERE title='The Departed';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=8.5, mpaa_rating='PG-13', overview='Two rival magicians destroy their lives chasing the perfect illusion.' WHERE title='The Prestige';
UPDATE movies SET director='Joel Coen, Ethan Coen', language='English', country='United States', imdb_rating=8.2, mpaa_rating='R', overview='A hunter, a sheriff, and a killer cross paths after a drug deal goes wrong.' WHERE title='No Country for Old Men';
UPDATE movies SET director='Paul Thomas Anderson', language='English', country='United States', imdb_rating=8.2, mpaa_rating='R', overview='An oil prospector builds wealth while losing his soul in the American frontier.' WHERE title='There Will Be Blood';
UPDATE movies SET director='David Fincher', language='English', country='United States', imdb_rating=7.8, mpaa_rating='PG-13', overview='A college networking site becomes a global company and a legal battlefield.' WHERE title='The Social Network';
UPDATE movies SET director='Darren Aronofsky', language='English', country='United States', imdb_rating=8.0, mpaa_rating='R', overview='A ballerina spirals under pressure while preparing for a career-defining role.' WHERE title='Black Swan';
UPDATE movies SET director='Tom Hooper', language='English', country='United Kingdom', imdb_rating=8.0, mpaa_rating='R', overview='A reluctant king works to overcome a speech impediment as war approaches.' WHERE title='The King''s Speech';
UPDATE movies SET director='Nicolas Winding Refn', language='English', country='United States', imdb_rating=7.8, mpaa_rating='R', overview='A quiet stunt driver is pulled into violence after helping a neighbor.' WHERE title='Drive';
UPDATE movies SET director='Joss Whedon', language='English', country='United States', imdb_rating=8.0, mpaa_rating='PG-13', overview='Earth’s superheroes unite against an alien invasion.' WHERE title='The Avengers';
UPDATE movies SET director='Quentin Tarantino', language='English', country='United States', imdb_rating=8.5, mpaa_rating='R', overview='A freed slave joins a bounty hunter to rescue his wife from a plantation owner.' WHERE title='Django Unchained';
UPDATE movies SET director='David O. Russell', language='English', country='United States', imdb_rating=7.7, mpaa_rating='R', overview='Two damaged people form a bond while trying to rebuild their lives.' WHERE title='Silver Linings Playbook';
UPDATE movies SET director='Alfonso Cuarón', language='English', country='United States', imdb_rating=7.7, mpaa_rating='PG-13', overview='An astronaut fights to survive after disaster leaves her drifting in orbit.' WHERE title='Gravity';
UPDATE movies SET director='Steve McQueen', language='English', country='United States', imdb_rating=8.1, mpaa_rating='R', overview='A free Black man is kidnapped and sold into slavery in the antebellum South.' WHERE title='12 Years a Slave';
UPDATE movies SET director='Martin Scorsese', language='English', country='United States', imdb_rating=8.2, mpaa_rating='R', overview='A stockbroker builds an empire of fraud, excess, and collapse.' WHERE title='The Wolf of Wall Street';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=8.7, mpaa_rating='PG-13', overview='A team travels through a wormhole to find humanity a new home.' WHERE title='Interstellar';
UPDATE movies SET director='Wes Anderson', language='English', country='United States', imdb_rating=8.1, mpaa_rating='R', overview='A hotel concierge and a lobby boy become tangled in theft, murder, and war.' WHERE title='The Grand Budapest Hotel';
UPDATE movies SET director='George Miller', language='English', country='Australia', imdb_rating=8.1, mpaa_rating='R', overview='A road warrior and a rebel driver flee a tyrant across a wasteland.' WHERE title='Mad Max: Fury Road';
UPDATE movies SET director='Alejandro G. Iñárritu', language='English', country='United States', imdb_rating=8.0, mpaa_rating='R', overview='A frontiersman survives betrayal and wilderness while seeking justice.' WHERE title='The Revenant';
UPDATE movies SET director='Tom McCarthy', language='English', country='United States', imdb_rating=8.1, mpaa_rating='R', overview='Journalists investigate institutional abuse and a citywide cover-up.' WHERE title='Spotlight';
UPDATE movies SET director='Damien Chazelle', language='English', country='United States', imdb_rating=8.0, mpaa_rating='PG-13', overview='An actress and a jazz musician chase love and ambition in Los Angeles.' WHERE title='La La Land';
UPDATE movies SET director='Denis Villeneuve', language='English', country='United States', imdb_rating=7.9, mpaa_rating='PG-13', overview='A linguist is recruited to communicate with alien visitors before global panic turns to war.' WHERE title='Arrival';
UPDATE movies SET director='Jordan Peele', language='English', country='United States', imdb_rating=7.8, mpaa_rating='R', overview='A visit to a girlfriend’s family reveals a terrifying social trap.' WHERE title='Get Out';
UPDATE movies SET director='Christopher Nolan', language='English', country='United Kingdom', imdb_rating=7.8, mpaa_rating='PG-13', overview='Allied soldiers fight to survive evacuation from the beaches of Dunkirk.' WHERE title='Dunkirk';
UPDATE movies SET director='Ryan Coogler', language='English', country='United States', imdb_rating=7.3, mpaa_rating='PG-13', overview='A new king must defend Wakanda while deciding what the nation owes the wider world.' WHERE title='Black Panther';
UPDATE movies SET director='Bradley Cooper', language='English', country='United States', imdb_rating=7.6, mpaa_rating='R', overview='A musician helps a singer rise while his own life unravels.' WHERE title='A Star is Born';
UPDATE movies SET director='Bong Joon Ho', language='Korean', country='South Korea', imdb_rating=8.5, mpaa_rating='R', overview='A poor family infiltrates a wealthy household, exposing class tension with sharp consequences.' WHERE title='Parasite';
UPDATE movies SET director='Todd Phillips', language='English', country='United States', imdb_rating=8.4, mpaa_rating='R', overview='An isolated man descends into violence in a city that ignores him.' WHERE title='Joker';
UPDATE movies SET director='Quentin Tarantino', language='English', country='United States', imdb_rating=7.6, mpaa_rating='R', overview='An actor and his stunt double navigate a changing Hollywood in 1969.' WHERE title='Once Upon a Time in Hollywood';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=7.3, mpaa_rating='PG-13', overview='A secret agent manipulates time to prevent a future attack.' WHERE title='Tenet';
UPDATE movies SET director='Denis Villeneuve', language='English', country='United States', imdb_rating=8.0, mpaa_rating='PG-13', overview='A gifted heir enters a desert world at the center of politics, prophecy, and war.' WHERE title='Dune';
UPDATE movies SET director='Jon Watts', language='English', country='United States', imdb_rating=8.2, mpaa_rating='PG-13', overview='A spell fractures the multiverse and forces Spider-Man to face impossible consequences.' WHERE title='Spider-Man: No Way Home';
UPDATE movies SET director='Matt Reeves', language='English', country='United States', imdb_rating=7.8, mpaa_rating='PG-13', overview='A young Batman investigates corruption while facing a serial killer in Gotham.' WHERE title='The Batman';
UPDATE movies SET director='Joseph Kosinski', language='English', country='United States', imdb_rating=8.2, mpaa_rating='PG-13', overview='An aging pilot trains a new generation for a near-impossible mission.' WHERE title='Top Gun: Maverick';
UPDATE movies SET director='Daniel Kwan, Daniel Scheinert', language='English', country='United States', imdb_rating=7.8, mpaa_rating='R', overview='A laundromat owner is pulled through multiverses while trying to hold her family together.' WHERE title='Everything Everywhere All at Once';
UPDATE movies SET director='Christopher Nolan', language='English', country='United States', imdb_rating=8.3, mpaa_rating='R', overview='A physicist leads a wartime project that changes science, politics, and human history.' WHERE title='Oppenheimer';
UPDATE movies SET director='Steven Spielberg', language='English', country='United States', imdb_rating=8.2, mpaa_rating='PG-13', overview='Scientists and visitors fight to survive after cloned dinosaurs escape containment.' WHERE title='Jurassic Park';
UPDATE movies SET director='James Cameron', language='English', country='United States', imdb_rating=7.9, mpaa_rating='PG-13', overview='A romance unfolds aboard a doomed ocean liner.' WHERE title='Titanic';
UPDATE movies SET director='David Fincher', language='English', country='United States', imdb_rating=8.8, mpaa_rating='R', overview='An office worker and a soap maker form an underground club that becomes something darker.' WHERE title='Fight Club';
UPDATE movies SET director='Roger Allers, Rob Minkoff', language='English', country='United States', imdb_rating=8.5, mpaa_rating='G', overview='A young lion prince must reclaim his kingdom after betrayal and exile.' WHERE title='The Lion King';
UPDATE movies SET director='Hayao Miyazaki', language='Japanese', country='Japan', imdb_rating=8.6, mpaa_rating='PG', overview='A girl enters a spirit world and must find courage to save her parents.' WHERE title='Spirited Away';

INSERT INTO movies (title, release_year, duration, director, language, country, imdb_rating, mpaa_rating, overview) VALUES
('Whiplash', 2014, 106, 'Damien Chazelle', 'English', 'United States', 8.5, 'R', 'A young drummer is pushed to extremes by an abusive conservatory instructor.'),
('The Green Mile', 1999, 189, 'Frank Darabont', 'English', 'United States', 8.6, 'R', 'A death row guard encounters a prisoner with a mysterious gift.'),
('City of God', 2002, 130, 'Fernando Meirelles, Kátia Lund', 'Portuguese', 'Brazil', 8.6, 'R', 'Young lives collide with crime and survival in the favelas of Rio de Janeiro.'),
('The Intouchables', 2011, 112, 'Olivier Nakache, Éric Toledano', 'French', 'France', 8.5, 'R', 'A wealthy quadriplegic and his caregiver form an unlikely friendship.'),
('The Raid', 2011, 101, 'Gareth Evans', 'Indonesian', 'Indonesia', 7.6, 'R', 'A police squad fights floor by floor through a criminal stronghold.'),
('Nairobi Half Life', 2012, 96, 'David Tosh Gitonga', 'Swahili, English', 'Kenya', 7.3, 'NR', 'A young actor from rural Kenya is pulled into Nairobi’s criminal underworld while chasing his dream.'),
('The Constant Gardener', 2005, 129, 'Fernando Meirelles', 'English', 'United Kingdom', 7.4, 'R', 'A diplomat investigates the murder of his wife and uncovers pharmaceutical corruption.'),
('Queen of Katwe', 2016, 124, 'Mira Nair', 'English', 'Uganda, United States', 7.4, 'PG', 'A Ugandan girl discovers chess and changes the possibilities of her life.'),
('Tsotsi', 2005, 94, 'Gavin Hood', 'Zulu, Xhosa, Afrikaans, English', 'South Africa', 7.2, 'R', 'A young gang leader is forced to confront himself after kidnapping an infant by accident.'),
('The Boy Who Harnessed the Wind', 2019, 113, 'Chiwetel Ejiofor', 'English, Chichewa', 'United Kingdom, Malawi', 7.6, 'TV-PG', 'A Malawian boy builds a wind turbine to help his village survive famine.'),
('Beasts of No Nation', 2015, 137, 'Cary Joji Fukunaga', 'English, Akan', 'Ghana, United States', 7.7, 'NR', 'A child soldier is drawn into war under the command of a brutal leader.'),
('Crouching Tiger, Hidden Dragon', 2000, 120, 'Ang Lee', 'Mandarin', 'Taiwan, Hong Kong, United States', 7.9, 'PG-13', 'Warriors, lovers, and thieves pursue freedom and honor around a legendary sword.');

INSERT INTO genres (name) VALUES ('Documentary');
INSERT INTO genres (name) VALUES ('Sports');
INSERT INTO genres (name) VALUES ('Political');
INSERT INTO genres (name) VALUES ('African Cinema');

INSERT INTO actors (name, birth_date) VALUES ('Miles Teller', '1987-02-20');
INSERT INTO actors (name, birth_date) VALUES ('J. K. Simmons', '1955-01-09');
INSERT INTO actors (name, birth_date) VALUES ('Michael Clarke Duncan', '1957-12-10');
INSERT INTO actors (name, birth_date) VALUES ('Alexandre Rodrigues', '1983-05-21');
INSERT INTO actors (name, birth_date) VALUES ('Omar Sy', '1978-01-20');
INSERT INTO actors (name, birth_date) VALUES ('Iko Uwais', '1983-02-12');
INSERT INTO actors (name, birth_date) VALUES ('Joseph Wairimu', '1984-01-01');
INSERT INTO actors (name, birth_date) VALUES ('Lupita Nyong''o', '1983-03-01');
INSERT INTO actors (name, birth_date) VALUES ('Ralph Fiennes', '1962-12-22');
INSERT INTO actors (name, birth_date) VALUES ('Madina Nalwanga', '2002-02-02');
INSERT INTO actors (name, birth_date) VALUES ('Presley Chweneyagae', '1984-10-19');
INSERT INTO actors (name, birth_date) VALUES ('Maxwell Simba', '2005-03-21');
INSERT INTO actors (name, birth_date) VALUES ('Idris Elba', '1972-09-06');
INSERT INTO actors (name, birth_date) VALUES ('Chow Yun-fat', '1955-05-18');
INSERT INTO actors (name, birth_date) VALUES ('Michelle Yeoh', '1962-08-06');

CREATE INDEX IF NOT EXISTS idx_movies_title ON movies(title);
CREATE INDEX IF NOT EXISTS idx_movies_release_year ON movies(release_year);
CREATE INDEX IF NOT EXISTS idx_movies_director ON movies(director);
CREATE INDEX IF NOT EXISTS idx_movies_country ON movies(country);
CREATE INDEX IF NOT EXISTS idx_movies_rating ON movies(imdb_rating);
CREATE INDEX IF NOT EXISTS idx_actors_name ON actors(name);
CREATE INDEX IF NOT EXISTS idx_genres_name ON genres(name);
CREATE INDEX IF NOT EXISTS idx_movie_genres_movie ON movie_genres(movie_id);
CREATE INDEX IF NOT EXISTS idx_movie_genres_genre ON movie_genres(genre_id);
CREATE INDEX IF NOT EXISTS idx_movie_actors_movie ON movie_actors(movie_id);
CREATE INDEX IF NOT EXISTS idx_movie_actors_actor ON movie_actors(actor_id);
