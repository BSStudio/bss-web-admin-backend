-- create category table with name and description
CREATE TABLE category
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(64)  NOT NULL UNIQUE CHECK ( TRIM(name) != '' ),
    description VARCHAR(256) NOT NULL CHECK ( TRIM(description) != '' )
);

-- create video category table
-- a video only can have one of the same category
CREATE TABLE video_category
(
    category_id UUID REFERENCES category (id),
    video_id    UUID REFERENCES video (id),
    PRIMARY KEY (category_id, video_id)
);

INSERT INTO category (name, description)
VALUES ('Archív', '2000 előtti videók.'),
       ('BME', 'Hivatalosabb, komolyabb az egyetemhez és a VIK-hez köthető videók.'),
       ('BSS', 'A stúdiónk közéletéhez tartozó videók.'),
       ('Egyetemi élet', 'Egyetemista feelingről szóló videó, inkább egyetemközi, nem VIK-es.'),
       ('Feeling', 'Egy feelinget, életérzést ad át a videó.'),
       ('Főzőshow', 'Videó, amiben főzünk.'),
       ('Gólya', 'Ökörsütés, G7, GTB, Gólyabál stb...'),
       ('Kisokos', 'BSS Kisokos epizódjai'),
       ('Kolis közélet', 'SCH, TTNY hírek, aktualitások, köröket bemutató videók.'),
       ('Könnyű', 'Buli, party, könnyűzene.'),
       ('Közélet', 'Nem egyetemi, hanem egyéb, közéleti témát feldolgozó videó.'),
       ('Kultúr', 'Kiállítás, színház, komolyzene, jazz, blues, festészet stb.'),
       ('Lányok', 'A videón a lányok jelenléte jelentős :)'),
       ('Qpa', 'Qpa-videók'),
       ('Rücsök', 'Igénytelenség, szemét, undorító dolgok vannak a videón.'),
       ('SCH-történelem', 'A Schönherz történelméhez, mérföldköveihez tartozik a videó.'),
       ('Simonyi', 'A Simonyi közéletéhez tartozó, nem feltétlenül szakmai videó.'),
       ('Simonyi Konferencia', 'A konferenciához bármilyen szinten köthető a videó.'),
       ('Sport', 'DSK rendezvényei, egyéb sportesemény.'),
       ('Szakma', 'Informatikus és villamosmérnök szakmához köthető a videó.'),
       ('Sztárinterjú', 'Hosszabb lélegzetvételű interjú valami hírességgel.'),
       ('Tanár', 'Tanárokat hivatás közben, vagy éppen funky helyzetekben mutatja be a videó.'),
       ('TTNY', 'A TTNY kolihoz, szakmához, közélethez köthető a videó.'),
       ('Vélemény', 'Amikor az utca vagy koli emberét kérdezzük.'),
       ('What the fun', 'Értelmetlen, funky, faszság, WTF');
