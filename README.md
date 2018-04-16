# CS-3200-Project

Group Members:
  Haig Bernard, 
  Hugh Ferguson, 
  Jordan Massa, 
  Macie Rosenthal, 
  Brian Shea.
  
  
Build Instructions Video:


Demonstration Video:


Presentation Video:
https://youtu.be/oDvbsuVyUdk



ddl.sql and dml.sql should be run through SQLite to create/populate test database.

java directory should be imported to Eclipse, and path to test database should be entered under Run Configurations > Arguments.

Task queries here:

/* Register a new user */
INSERT INTO USER(Email,Pword,Fname,Lname,Photo,Phone,Street,City,Postal,Country)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

/* Record that a user loves a movie */
INSERT INTO LOVE(UID, MID)
VALUES (?, ?)

/* Order a ticket from a local theatre */
INSERT INTO ORDERS(UID, MID, VID, TIMESTAMP, COST, QUANTITY, SHOWING, EXPIRATION)
VALUES (?, ?, ?, ?, ?, ?, ?, ?)

/* Credit an existing actress for a movie */
INSERT INTO CREDIT(MID, PID, ROLE, PHOTO, CHARACTER)
VALUES (?, ?, ?, ?, ?)

/* Provide a ranked list of revenue generated from the top-10 studios */
SELECT STUDIO.Name AS Studio, PRINTF('$%.2f', SUM(Orders.Cost)) AS Revenue
FROM (ORDERS INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID)
INNER JOIN STUDIO ON MOVIE.Studio=STUDIO.SID
GROUP BY MOVIE.Studio
ORDER BY TOTAL(ORDERS.Cost) DESC
LIMIT 10

/* Find all movies directed by a person (supplied via last name) */
SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Director, MOVIE.Name AS Movie
FROM ((MOVIE INNER JOIN CREDIT ON MOVIE.MID=CREDIT.MID)
INNER JOIN PERSON ON CREDIT.PID=PERSON.PID)
INNER JOIN ROLE ON CREDIT.Role=ROLE.RID
WHERE PERSON.Lname LIKE ? AND ROLE.Name == 'Director'
ORDER BY MOVIE.Name

/* Load the cover images and names of movies ordered by a particular user */
SELECT USER.UID AS User, MOVIE.Name AS Movie, IMAGE.Image AS Cover_image
FROM ((USER INNER JOIN ORDERS ON USER.UID=ORDERS.UID)
INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID)
INNER JOIN IMAGE ON MOVIE.MID=IMAGE.MID
WHERE USER.UID == ?
ORDER BY MOVIE.Name

/* Find all movies released this year that a user loves but has not ordered */
SELECT USER.UID AS User, MOVIE.Name AS Movie, strftime('%m-%d-%Y', datetime(Rdate, 'unixepoch')) AS Release_date
FROM (USER INNER JOIN LOVE ON USER.UID=LOVE.UID)
INNER JOIN MOVIE ON LOVE.MID=MOVIE.MID
WHERE strftime('%Y', datetime(Rdate, 'unixepoch')) == strftime('%Y', 'now') AND USER.UID == ?
EXCEPT
SELECT USER.UID AS User, MOVIE.Name AS Movie, strftime('%m-%d-%Y', datetime(Rdate, 'unixepoch')) AS Release_date
FROM (USER INNER JOIN ORDERS ON USER.UID=ORDERS.UID)
INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID
WHERE strftime('%Y', datetime(Rdate, 'unixepoch')) == strftime('%Y', 'now') AND USER.UID == ?
ORDER BY MOVIE.Name

/* Find all people (name, picture, and role) credited for a particular movie (supplied by name) */
SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Name, PERSON.Photo AS Picture, ROLE.Name AS Role, MOVIE.Name AS Movie
FROM ((PERSON INNER JOIN CREDIT ON PERSON.PID=CREDIT.PID)
INNER JOIN MOVIE ON CREDIT.MID=MOVIE.MID)
INNER JOIN ROLE ON CREDIT.ROLE=ROLE.RID
WHERE MOVIE.Name LIKE ?
ORDER BY Name

/* Provide a ranked list of revenue generated from the top-3 movie genres */
SELECT Genre.Name AS Genre, PRINTF('$%.2f', SUM(Orders.Cost)) AS Revenue
FROM (ORDERS INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID)
INNER JOIN GENRE ON MOVIE.Genre=GENRE.GID
GROUP BY MOVIE.Genre
ORDER BY TOTAL(ORDERS.Cost) DESC
LIMIT 3

Complex queries and their justifications here:

Query 11:

/* Find all the names of people who ordered a particular movie (supplied by name) within 24 hours of the movie’s release date in chronological order (earliest orders appear first). Strong motivation/justification for the query in the domain because the site could offer discounts/deals for most avid fans */

/* Query joins 4 tables, uses strftime function, has two ordering fields, two WHERE clauses in case orders are made at the same time, has multiple non-aggregate functions in both the SELECT and WHERE clauses, and the query is a valuable one for vendor/order statistics. */

SELECT USER.UID AS User, ORDERS.Confirmation AS OrderNum, MOVIE.Name as Movie, VENDOR.Name AS Vendor,
strftime('%m-%d-%Y',MOVIE.Rdate, 'unixepoch') AS ReleaseDate, 
(ORDERS.Timestamp - MOVIE.Rdate)/3600 AS hoursAfterRelease,
(ORDERS.Timestamp - MOVIE.Rdate)%3600/60 AS minsAfterRelease
FROM ((MOVIE INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID) 
INNER JOIN USER ON USER.UID=ORDERS.UID)
INNER JOIN VENDOR ON ORDERS.VID=VENDOR.VID
WHERE MOVIE.Name LIKE ?
AND (ORDERS.Timestamp - MOVIE.Rdate) <= 86400
ORDER BY ORDERS.Timestamp, User

Query 12:

/* Provide a ranked list of the top 3 directors in a supplied genre based on the number of orders that their movies in that genre receive. Justification for the query in the domain because the site can see which directors are most popular and get more of their movies to offer to the customers */

/*Query joins 6 tables, has multiple WHERE clauses which includes user input, there is a grouping for directors, has two ORDER BY fields to get the max count or go alphabetically in the case of a tie, an aggregate function to count the number of orders, and has a strong justification. */

SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Director, COUNT(ORDERS.Confirmation) AS OrderCount
FROM ((((CREDIT INNER JOIN MOVIE ON CREDIT.MID=MOVIE.MID)
INNER JOIN PERSON ON CREDIT.PID=PERSON.PID)
INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID)
INNER JOIN ROLE ON CREDIT.Role=ROLE.RID)
INNER JOIN GENRE ON MOVIE.Genre = GENRE.GID
WHERE ROLE.Name == 'Director' AND GENRE.Name LIKE ?
GROUP BY Director
ORDER BY OrderCount DESC, Director
LIMIT 3

Query 13:

/* Provide the most popular actors in a given country (supplied by name). Could advertise new movies with that actor more frequently to users in a specific country. */

/*Query joins 6 tables, one is a left join, the COUNT aggregate function is used, a grouping for the person ID of credit, two ORDER BY fields, a WHERE to match the country with user input and find where the person’s role is an actor, and a non-aggregate function is used to concatenate the person’s first and last name as an actor, and has a strong justification. */

SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Actor, COUNT(*) AS Appearances
FROM ((((MOVIE INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID)
INNER JOIN USER ON ORDERS.UID=USER.UID)
LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID)
INNER JOIN PERSON ON CREDIT.PID=PERSON.PID)
INNER JOIN ROLE ON CREDIT.Role=ROLE.RID
WHERE USER.Country LIKE ?
AND ROLE.Name == 'Actor' 
GROUP BY CREDIT.PID
ORDER BY Appearances DESC, Actor
LIMIT 3

Query 14:

/* Provide a list of suggested movies for a user based on their most recently ordered and loved movie. The suggestion list is built upon movies in the same genre with the same director. This can be an effective marketing tool for the site as it suggests movies it thinks the user will like and buy */

/* This query is complex because it uses the MAX aggregate function, has two subqueries, uses left joins to get all credits from the same movie, joins more than three tables, and has a strong justification. */

SELECT DISTINCT MOVIE.Name, GENRE.Name, (PERSON.Fname || ' ' || PERSON.Lname) AS Director
FROM ((MOVIE INNER JOIN GENRE ON MOVIE.Genre=GENRE.GID)
INNER JOIN CREDIT ON CREDIT.MID=MOVIE.MID)
INNER JOIN PERSON ON CREDIT.PID=PERSON.PID
WHERE GENRE.GID IN (
SELECT q1.GID
FROM
(
SELECT LOVE.MID, MAX(ORDERS.Timestamp), GENRE.GID
FROM ((((LOVE INNER JOIN USER ON LOVE.UID = USER.UID)
INNER JOIN ORDERS ON USER.UID=ORDERS.UID)
INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID)
INNER JOIN GENRE ON GENRE.GID=MOVIE.Genre)
LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID
WHERE USER.UID = ?
) q1
)
AND CREDIT.PID IN (
SELECT q2.PID
FROM
(
SELECT LOVE.MID, MAX(ORDERS.Timestamp), CREDIT.PID
FROM (((((LOVE INNER JOIN USER ON LOVE.UID = USER.UID)
INNER JOIN ORDERS ON USER.UID=ORDERS.UID)
INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID)
INNER JOIN GENRE ON GENRE.GID=MOVIE.Genre)
LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID)
INNER JOIN ROLE ON ROLE.RID=CREDIT.Role
WHERE USER.UID = ? AND ROLE.Name == 'Director'
) q2
)

Query 15:

/* A ranked list of directors whose movies are still being ordered 10 years after their release date. Provide a ranked (desc) list of movies and directors who have orders after the 10-year mark. Justification for the site is that these directors provide the best long run revenue opportunity because their movies are popular for many years */

/*This query is complex because it has two ORDER BY fields, joins five tables, uses the aggregate COUNT function, uses non-aggregate functions to concatenate a person's name in the director field, the WHERE is not used for joins, and has a strong justification. */

SELECT MOVIE.Name AS Movie, (PERSON.Fname || ' ' || PERSON.Lname) AS Director,  COUNT(ORDERS.Confirmation) AS Orders_After_10_Years
FROM
((ORDERS INNER JOIN MOVIE ON ORDERS.MID = MOVIE.MID)
INNER JOIN CREDIT ON MOVIE.MID = CREDIT.MID)
INNER JOIN PERSON ON CREDIT.PID = PERSON.PID
INNER JOIN ROLE ON CREDIT.Role = ROLE.RID
WHERE
ORDERS.Timestamp >= MOVIE.Rdate + 315400000
AND ROLE.Name == 'Director'
GROUP BY Director
ORDER BY Orders_After_10_Years DESC, Director



