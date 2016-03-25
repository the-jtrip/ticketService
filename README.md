# ticketService
Repo for walmart ticket service assignment

*There are no dao's, daoImpl's, or spring data configs. To use this application I've made a "MemoryDB" class that has a static map of TicketLevel objects (each with its own map of rows and seats), as well as a static map of SeatHold object. This allows the application to behave similarly to database interactions within a given session, without having to actually connect to a database.

*All urls that are capable of consuming xml data are using the content type application/xml

Base path:
http://localhost:8080/TicketService/tickets

/holdTickets
You can post a minimal seatHold xml object to this url, 

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<seatHold>
    <seatsDesired>7</seatsDesired>
    <minLevel>3</minLevel>
    <maxLevel>4</maxLevel>
    <userName>jonjohnsonj@gmail.com</userName>
</seatHold>

and it will return a fully populated seatHoldObject for you to use for future webservice interactions

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<seatHold>
    <levelId>3</levelId>
    <levelName>Balcony 1</levelName>
    <maxLevel>0</maxLevel>
    <minLevel>0</minLevel>
    <price>50.0</price>
    <row>0</row>
    <seatHoldId>1</seatHoldId>
    <seats>1</seats>
    <seats>2</seats>
    <seats>3</seats>
    <seats>4</seats>
    <seats>5</seats>
    <seatsDesired>0</seatsDesired>
    <status>hold</status>
    <userName>jonjohnsonj@gmail.com</userName>
</seatHold>



/myTicket/{seatsHoldId}

if you know the id of your seatHold object, entering it here will return the full seatHold object



/seatsAvailable

this returns the count of all available seats



/seatsAvailable/{levelId}

this returns the count of all the seats available within a given level, based on the level id



/reserveSeats

this will attempt to reserve seats based on the seatHold xml object you post to this url. It will return a string
that indicates whether or not the attempt was successful

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<seatHold>
    <levelId>3</levelId>
    <levelName>Balcony 1</levelName>
    <maxLevel>0</maxLevel>
    <minLevel>0</minLevel>
    <price>50.0</price>
    <row>0</row>
    <seatHoldId>1</seatHoldId>
    <seats>1</seats>
    <seats>2</seats>
    <seats>3</seats>
    <seats>4</seats>
    <seats>5</seats>
    <seatsDesired>0</seatsDesired>
    <status>hold</status>
    <userName>jonjohnsonj@gmail.com</userName>
</seatHold>
