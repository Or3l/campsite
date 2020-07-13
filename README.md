## Local Deploy Instructions
### Maven Build
mvn clean package spring-boot:repackage
### Docker Build
docker build . -t campsite -f Dockerfile

docker run -p 8080:8080 campsite

## Swagger
http://localhost:8080/swagger-ui.html#

## Design explanation
The database contains 2 tables, one for the bookings (booking_id, arrival, departure, email, etc) and another table that contains a 
preloaded list of calendar days (day, is_booked, booking_id). 

I ended up using this design to handle the concurrency aspect of the booking. Having the table
date_availability allowed me to use optimistic locking and ensure that the campsite can only be booked one at a time.

In a real word scenario, I would probably go for a event source system.

## Notes
1. The app uses h2 as database. The scripts can be found in /resources
2. All dates are in UTC and the server uses UTC for date comparisons
3. "The campsite can be reserved minimum one day ahead" -> If now is 2020-07-13 00:0:01, a user won't be able to book 
for 2020-07-14 
