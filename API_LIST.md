# API List - bookmyevent

This document lists HTTP APIs grouped by service (User Service, Booking Service, Event Service, Seatlock Service).

## User Service

- GET /api/users/{id} — UserController.getUser — fetch user by id
- POST /api/users — UserController.createUser — create a new user
- PUT /api/users/{id} — UserController.updateUser — update user by id

- POST /api/organizers — OrganizerController.createOrganizer — create organizer
- GET /api/organizers/{id} — OrganizerController.getOrganizer — fetch organizer by id
- PUT /api/organizers/{id} — OrganizerController.updateOrganizer — partial update (orgName, contactEmail)

## Booking Service

- POST /api/bookings — BookingController.createBooking — create a booking (returns booking ref)
- POST /api/bookings/confirm — BookingController.confirm — confirm booking with bookingRef and holdToken

## Event Service

- GET /api/events/{id} — EventController.getEvent — fetch event details by id
- POST /api/events — EventController.createEvent — create a new event
- PUT /api/events/{id} — EventController.updateEvent — update event by id
- GET /api/events/search?city=&categoryId=&dateFrom=&dateTo= — EventController.searchEvents — search/filter events
  - Query params:
    - city (string) — venue city
    - categoryId (number) — event category id
    - dateFrom (YYYY-MM-DD) — start date (inclusive)
    - dateTo (YYYY-MM-DD) — end date (inclusive)
  - Response: List<EventDto> (id, title, categoryId, venueCity, startTime, endTime)

- POST /api/events — EventController.createEvent — create a new event
  - Body: CreateEventRequest (organizerId, venueId, title, categoryId, startTime, endTime)
  - Response: EventDto
- PUT /api/events/{id} — EventController.updateEvent — update event by id
  - Body: EventDto
  - Response: EventDto

- GET /api/events/{id}/seats — SeatMapController.getSeatMap — get seat map (sections + seats)
- GET /api/events/{id}/seat-layout — SeatMapController.getSeatLayout — alias to seat map
- GET /api/events/{id}/seats/booked — SeatMapController.getBookedSeats — list booked seat codes

- POST /api/venues — VenueController.createVenue — create a venue
- GET /api/venues/{id} — VenueController.getVenue — fetch venue by id
- PUT /api/venues/{id} — VenueController.updateVenue — update venue
- GET /api/venues — VenueController.listVenues — list all venues

## Seatlock Service

- POST /api/seatlock/tryLock — SeatLockController.tryLock — attempt to acquire locks for seats (returns true/false)
- POST /api/seatlock/release — SeatLockController.release — release held seat locks

---

If you want this exported as CSV, JSON, or added to a specific path/name, tell me and I'll update it.

