CREATE TYPE weapon_type AS ENUM ('огнестрельное оружие', 'лук и стрелы', 'метательное оружие', 'ручное оружие',
    'кинжалы и ножи', 'дробящее оружие', 'копья', 'топоры и тесаки', 'другое');

CREATE TYPE feeling AS ENUM ('голод', 'страх');
CREATE TYPE animal_species AS ENUM ('бородавочник', 'газель', 'антилопа', 'зебра');

CREATE TABLE hunter (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    experience INTEGER DEFAULT 0
);

CREATE TABLE location (
    id SERIAL PRIMARY KEY,
    name TEXT,
    coordinates POINT NOT NULL DEFAULT '(0, 0)'
);

CREATE TABLE animal (
    id SERIAL PRIMARY KEY,
    species ANIMAL_SPECIES NOT NULL,
    location INTEGER REFERENCES location ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE weapon (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    weapon_type WEAPON_TYPE NOT NULL,
    protection INTEGER NOT NULL CONSTRAINT check_weapon_protection CHECK (protection >= 0 AND protection <= 10),
    damage INTEGER NOT NULL CONSTRAINT check_weapon_damage CHECK (damage >= 0 AND damage <= 10)
);

CREATE TABLE skill (
    hunter_id INTEGER REFERENCES hunter ON DELETE CASCADE ON UPDATE CASCADE,
    weapon_id INTEGER REFERENCES weapon ON DELETE CASCADE ON UPDATE CASCADE,
    level INTEGER NOT NULL CONSTRAINT check_skill_level CHECK (level >= 0 AND level <= 10),
    PRIMARY KEY (hunter_id, weapon_id)
);

CREATE TABLE prey (
    hunter_id INTEGER REFERENCES hunter ON DELETE CASCADE ON UPDATE CASCADE,
    animal_id INTEGER REFERENCES animal ON DELETE CASCADE ON UPDATE CASCADE,
    date DATE NOT NULL,
    weight NUMERIC NOT NULL CONSTRAINT check_positive_weight CHECK (weight >= 0),
    used_weapon INTEGER REFERENCES weapon ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE hunter_feeling (
    id SERIAL PRIMARY KEY,
    hunter_id INTEGER REFERENCES hunter ON DELETE CASCADE ON UPDATE CASCADE,
    feeling FEELING NOT NULL,
    level INTEGER NOT NULL CONSTRAINT check_hunter_feeling_level CHECK (level >= 0 AND level <= 10),
    occurrence_datetime TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE animal_feeling (
    id SERIAL PRIMARY KEY,
    animal_id INTEGER REFERENCES animal ON DELETE CASCADE ON UPDATE CASCADE,
    feeling FEELING NOT NULL,
    level INTEGER NOT NULL CONSTRAINT check_animal_feeling_level CHECK (level >= 0 AND level <= 10),
    occurrence_datetime TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO hunter (name, surname)
VALUES
    ('Петр', 'Петрович'),
    ('Иван', 'Иванов'),
    ('Прокофий', 'Прокофьевич');

INSERT INTO weapon (name, weapon_type, protection, damage)
VALUES
    ('Пистолет', 'огнестрельное оружие', 3, 8),
    ('Длинный лук', 'лук и стрелы', 2, 7),
    ('Топор', 'топоры и тесаки', 1, 5),
    ('Метательный топор', 'метательное оружие', 5, 9),
    ('Кинжал', 'кинжалы и ножи', 3, 7);

INSERT INTO skill (hunter_id, weapon_id, level)
VALUES
    (1, 1, 8),
    (1, 2, 9),
    (1, 3, 10),
    (1, 4, 9),
    (1, 5, 7),
    (2, 1, 8),
    (2, 2, 10),
    (2, 3, 8),
    (2, 4, 9),
    (2, 5, 7),
    (3, 1, 8),
    (3, 2, 10),
    (3, 3, 8),
    (3, 4, 9),
    (3, 5, 7);

INSERT INTO hunter_feeling (hunter_id, feeling, level)
VALUES
    (1, 'голод', 0),
    (2, 'голод', 0),
    (3, 'голод', 0);

INSERT INTO location (name)
VALUES
    ('равнина');

INSERT INTO animal (species, location)
VALUES
    ('бородавочник', 1),
    ('бородавочник', 1),
    ('газель', 1),
    ('газель', 1),
    ('антилопа', 1),
    ('антилопа', 1),
    ('антилопа', 1),
    ('зебра', 1),
    ('зебра', 1);

INSERT INTO animal_feeling (animal_id, feeling, level)
VALUES
    (1, 'страх', 6),
    (2, 'страх', 7);


INSERT INTO prey (hunter_id, animal_id, date, weight, used_weapon)
VALUES
    (1, 3, '2024-03-01', 15, 1),
    (1, 4, '2024-03-02', 30, 2),
    (1, 5, '2024-03-03', 200, 3),
    (2, 6, '2024-03-01', 140, 4),
    (2, 7, '2024-03-05', 160, 4),
    (3, 8, '2024-03-03', 300, 5),
    (3, 9, '2024-03-01', 320, 5);



SELECT Н_ОЦЕНКИ.КОД, Н_ВЕДОМОСТИ.ДАТА FROM Н_ОЦЕНКИ
INNER JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ОЦЕНКА = Н_ОЦЕНКИ.КОД
WHERE Н_ОЦЕНКИ.КОД = '5' AND Н_ВЕДОМОСТИ.ДАТА = '2010-06-18 00:00:00';


SELECT Н_ЛЮДИ.ФАМИЛИЯ, Н_ВЕДОМОСТИ.ИД, Н_СЕССИЯ.ЧЛВК_ИД FROM Н_ЛЮДИ
RIGHT JOIN Н_ВЕДОМОСТИ ON Н_ЛЮДИ.ИД = Н_ВЕДОМОСТИ.ЧЛВК_ИД
RIGHT JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД = Н_СЕССИЯ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ОТЧЕСТВО = 'Александрович' and Н_ВЕДОМОСТИ.ИД = 1426978 and
Н_СЕССИЯ.ДАТА < '2002-01-04 00:00:00';


SELECT Н_ЛЮДИ.ОТЧЕСТВО, COUNT(Н_ЛЮДИ.ИД) AS КОЛИЧЕСТВО FROM Н_ЛЮДИ
JOIN Н_УЧЕНИКИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ПЛАНЫ ON  Н_УЧЕНИКИ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ ON Н_ФОРМЫ_ОБУЧЕНИЯ.ИД = Н_ПЛАНЫ.ФО_ИД
GROUP BY Н_ЛЮДИ.ОТЧЕСТВО
HAVING SUM(CASE WHEN Н_ФОРМЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ = 'заочная' THEN 1 ELSE 0 END) < 50
ORDER BY Н_ЛЮДИ.ОТЧЕСТВО;

SELECT
CASE
    WHEN Н_ЛЮДИ.ОТЧЕСТВО = '.' THEN '-'
    ELSE Н_ЛЮДИ.ОТЧЕСТВО
END,
    COUNT(Н_ЛЮДИ.ИД) AS КОЛИЧЕСТВО FROM Н_ЛЮДИ
JOIN Н_УЧЕНИКИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ПЛАНЫ ON  Н_УЧЕНИКИ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ ON Н_ФОРМЫ_ОБУЧЕНИЯ.ИД = Н_ПЛАНЫ.ФО_ИД
where Н_ФОРМЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ = 'заочная'
GROUP BY Н_ЛЮДИ.ОТЧЕСТВО
HAVING SUM(CASE WHEN  THEN 1 ELSE 0 END) < 50
ORDER BY Н_ЛЮДИ.ОТЧЕСТВО;

SELECT Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО,
       AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) AS "СРЕДНЯЯ ОЦЕНКА"
FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND (Н_ОЦЕНКИ.СОРТ < 5 or Н_ОЦЕНКИ.СОРТ isnull )
WHERE Н_УЧЕНИКИ.ГРУППА = '4100'
GROUP BY Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО

HAVING  AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) >= (
        SELECT MAX(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) FROM Н_ВЕДОМОСТИ
        JOIN Н_ЛЮДИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
        JOIN Н_УЧЕНИКИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
        JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
        WHERE Н_УЧЕНИКИ.ГРУППА = '3100');


SELECT Н_УЧЕНИКИ.ИД, avg(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) FROM Н_ВЕДОМОСТИ
        JOIN Н_ЛЮДИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
        JOIN Н_УЧЕНИКИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
        JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
        WHERE Н_УЧЕНИКИ.ГРУППА = '4100'
group by Н_УЧЕНИКИ.ИД;

SELECT Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО,
       AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) AS "СРЕДНЯЯ ОЦЕНКА"
FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_УЧЕНИКИ.ИД
JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
WHERE Н_УЧЕНИКИ.ГРУППА = '4100'
GROUP BY Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО

SELECT Н_УЧЕНИКИ.ГРУППА, Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО, Н_УЧЕНИКИ.П_ПРКОК_ИД FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE Н_УЧЕНИКИ.ИД IN
(SELECT Н_УЧЕНИКИ.ИД FROM Н_УЧЕНИКИ
JOIN Н_ПЛАНЫ ON  Н_УЧЕНИКИ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
JOIN Н_ФОРМЫ_ОБУЧЕНИЯ ON Н_ФОРМЫ_ОБУЧЕНИЯ.ИД = Н_ПЛАНЫ.ФО_ИД
WHERE Н_УЧЕНИКИ.ПРИЗНАК = 'отчисл' AND Н_УЧЕНИКИ.СОСТОЯНИЕ = 'утвержден'AND Н_ФОРМЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ IN ('Заочная', 'Очная')
AND Н_УЧЕНИКИ.КОНЕЦ < '2012-09-01 00:00:00');

SELECT Н_ЛЮДИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО
FROM Н_ЛЮДИ
LEFT JOIN Н_УЧЕНИКИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE Н_УЧЕНИКИ.ЧЛВК_ИД IS NULL ;

SELECT COUNT(*)
FROM (
    SELECT COUNT(Н_ЛЮДИ.ФАМИЛИЯ) FROM Н_ЛЮДИ
    GROUP BY Н_ЛЮДИ.ФАМИЛИЯ
) AS unique_surnames;

SELECT COUNT(*)
FROM (
    SELECT COUNT(Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ) FROM Н_ЛЮДИ
    GROUP BY Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
) AS unique_birthdays;

CREATE VIEW MAX_GRADE_3100 AS
SELECT Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ОТЧЕСТВО, MAX(Н_ВЕДОМОСТИ.ОЦЕНКА) FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_УЧЕНИКИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
GROUP BY Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ОТЧЕСТВО;



CREATE VIEW max_fear AS
SELECT hunter.id, hunter.name, hunter.surname, MAX(prey.weight) from hunter
JOIN prey ON hunter.id = prey.hunter_id
JOIN hunter_feeling ON hunter.id = hunter_feeling.hunter_id
WHERE hunter_feeling.feeling = 'страх'
GROUP BY (hunter.id, hunter.name, hunter.surname);

CREATE MATERIALIZED VIEW avg_hungry AS
SELECT hunter.id, hunter.name, hunter.surname, AVG(prey.weight) from hunter
JOIN prey ON hunter.id = prey.hunter_id
JOIN hunter_feeling ON hunter.id = hunter_feeling.hunter_id
WHERE hunter_feeling.feeling = 'голод'
GROUP BY (hunter.id, hunter.name, hunter.surname);

SELECT avg_hungry.id, avg_hungry.name, avg_hungry.surname, avg_hungry.avg
FROM avg_hungry
JOIN max_fear ON avg_hungry.id = max_fear.id
WHERE avg_hungry.avg > max_fear.max;


SELECT hunter.id, hunter.name, hunter.surname, AVG(prey.weight) from hunter
JOIN prey ON hunter.id = prey.hunter_id
JOIN hunter_feeling ON hunter.id = hunter_feeling.hunter_id
WHERE hunter_feeling.feeling = 'голод'
GROUP BY (hunter.id, hunter.name, hunter.surname)

HAVING AVG(prey.weight) >= (
    SELECT MAX(prey.weight) from prey
    JOIN hunter ON hunter.id = prey.hunter_id
    JOIN hunter_feeling ON hunter.id = hunter_feeling.hunter_id
    WHERE hunter_feeling.feeling = 'голод'
    );


CREATE VIEW max_weight_prey_hungry AS
SELECT hunter.id, hunter.name, hunter.surname, AVG(prey.weight) from hunter
JOIN prey ON hunter.id = prey.hunter_id
JOIN hunter_feeling ON hunter.id = hunter_feeling.hunter_id AND
WHERE hunter_feeling.feeling = ‘страх’
GROUP BY (hunter.id, hunter.name, hunter.surname);

ALTER TABLE hunter ALTER COLUMN name SET CHECK (name NOT NULL);

SELECT avg_hungry.id, avg_hungry.name, avg_hungry.surname, avg_hungry.avg
FROM avg_hungry
JOIN max_fear ON avg_hungry.id = max_fear.id
WHERE avg_hungry.avg > max_fear.id;



SELECT Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО,
AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) AS "СРЕДНЯЯ ОЦЕНКА"
FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
WHERE Н_УЧЕНИКИ.ГРУППА = '4100' AND
      AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) >= (
        SELECT MAX(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) FROM Н_ВЕДОМОСТИ
        WHERE Н_УЧЕНИКИ.ГРУППА = '3100')
GROUP BY Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО

SELECT Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО,
       AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) AS "СРЕДНЯЯ ОЦЕНКА"
FROM Н_УЧЕНИКИ
JOIN Н_ЛЮДИ ON Н_ЛЮДИ.ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
JOIN Н_ВЕДОМОСТИ ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
JOIN Н_ОЦЕНКИ ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА AND Н_ОЦЕНКИ.СОРТ < 5
WHERE Н_УЧЕНИКИ.ГРУППА = '4100'
  AND AVG(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) >= (
        SELECT MAX(Н_ВЕДОМОСТИ.ОЦЕНКА::integer) FROM Н_ВЕДОМОСТИ
        WHERE Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_УЧЕНИКИ.ЧЛВК_ИД AND Н_УЧЕНИКИ.ГРУППА = '3100')
GROUP BY Н_УЧЕНИКИ.ИД, Н_ЛЮДИ.ИМЯ, Н_ЛЮДИ.ФАМИЛИЯ, Н_ЛЮДИ.ОТЧЕСТВО;
