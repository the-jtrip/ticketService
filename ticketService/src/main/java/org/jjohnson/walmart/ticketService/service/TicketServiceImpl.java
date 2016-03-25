package org.jjohnson.walmart.ticketService.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.jjohnson.walmart.ticketService.config.MemoryDB;
import org.jjohnson.walmart.ticketService.domain.SeatHold;
import org.jjohnson.walmart.ticketService.domain.TicketLevel;

/**
 * Implementation of the ticket service
 * 
 * @author jon
 *
 */
public class TicketServiceImpl implements TicketService {

	static Logger log = Logger.getLogger(TicketServiceImpl.class.getName());

	// the following maps will act as a 'local db' so this webservice can run
	// without having to correspond to an externally configured schema
	private Map<Long, SeatHold> seats = MemoryDB.getSeatsHolds();
	private Map<Long, TicketLevel> ticketLevels = MemoryDB.getTicketLevels();

	/**
	 * Constructor for this ticket service implementation class, populates the
	 * 'memoryDb' with the ticket level values outlined in the assignment
	 */
	public TicketServiceImpl() {
		TicketLevel orchestra = new TicketLevel(Long.valueOf(1), "Orchestra", 100.00, 25, 50);
		TicketLevel main = new TicketLevel(Long.valueOf(2), "Main", 75.00, 20, 100);
		TicketLevel balcony1 = new TicketLevel(Long.valueOf(3), "Balcony 1", 50.00, 15, 100);
		TicketLevel balcony2 = new TicketLevel(Long.valueOf(4), "Balcony 2", 40.00, 15, 100);
		ticketLevels.put(orchestra.getLevelId(), orchestra);
		ticketLevels.put(main.getLevelId(), main);
		ticketLevels.put(balcony1.getLevelId(), balcony1);
		ticketLevels.put(balcony2.getLevelId(), balcony2);
	}

	/**
	 * Getter for the ticket level map
	 * 
	 * @return an arraylist of the values of the ticket level map
	 */
	public List<TicketLevel> getAllTicketLevels() {
		return new ArrayList<TicketLevel>(ticketLevels.values());
	}

	/**
	 * Getter for the seat hold map
	 * 
	 * @return an arraylist of the values of the seat hold map
	 */
	public List<SeatHold> getAllSeatHold() {
		return new ArrayList<SeatHold>(seats.values());
	}

	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		int availableSeats = 0;
		if (venueLevel.isPresent()) {
			availableSeats = findSeatsCountByStatus(ticketLevels.get(venueLevel.get().longValue()), "available");
		} else {
			for (TicketLevel ticket : ticketLevels.values()) {
				availableSeats += findSeatsCountByStatus(ticket, "available");
			}
		}
		return availableSeats;
	}

	/**
	 * Counts the number of seats in a ticket level that match the given status
	 * 
	 * @param ticketLevel
	 *            the ticket level object to search against
	 * @param status
	 *            the status to check for
	 * @return an int count of all the matches
	 */
	public int findSeatsCountByStatus(TicketLevel ticketLevel, String status) {
		int seats = 0;
		Map<Integer, HashMap<Integer, String>> a = ticketLevel.getTickets();
		for (Map.Entry<Integer, HashMap<Integer, String>> set : a.entrySet()) {
			for (Map.Entry<Integer, String> subSet : set.getValue().entrySet()) {
				if (subSet.getValue().equalsIgnoreCase(status)) {
					seats++;
				}
			}
		}
		return seats;
	}

	/**
	 * This method looks through each row of a ticket level until it finds a
	 * consecutive number of seats equal to the number specified
	 * 
	 * @param ticketLevel
	 *            the ticket level to search for seats in
	 * @param numSeats
	 *            the amount of consecutive seats to search for
	 * @return a map that returns the row and the seat numbers in that row
	 */
	public Map<String, Object> findAvailableSeatAndRowNums(TicketLevel ticketLevel, int numSeats) {
		Map<String, Object> rowSeatMap = null;
		Map<Integer, HashMap<Integer, String>> a = ticketLevel.getTickets();
		outerloop: for (Map.Entry<Integer, HashMap<Integer, String>> set : a.entrySet()) {
			int seats = 1;
			for (Map.Entry<Integer, String> subSet : set.getValue().entrySet()) {
				if (subSet.getValue().equalsIgnoreCase("available")) {
					if (seats == numSeats) {
						List<Integer> seatList = new ArrayList<>();
						for (int startSeat = subSet.getKey() - numSeats; startSeat < subSet.getKey(); startSeat++) {
							seatList.add(startSeat);
						}
						rowSeatMap = new HashMap<>();
						rowSeatMap.put("seats", seatList);
						rowSeatMap.put("row", set.getKey());
						break outerloop;
					}
					seats++;
				} else {
					seats = 0;
				}
			}
		}
		return rowSeatMap;
	}

	/**
	 * This method takes updates the availability of seats within a seathold
	 * object to the status specified. The static seats map and ticketLevels map
	 * are also updated with the new availability
	 * 
	 * @param seatHold
	 *            the seathold object containing the list of seats within a row
	 *            within a ticket level
	 * @param status
	 *            the availability status to update the given seats to.
	 */
	public void changeSeatStatus(SeatHold seatHold, String status) {
		TicketLevel updateLevel = ticketLevels.get(seatHold.getLevelId());
		HashMap<Integer, String> seatsInRow = updateLevel.getTickets().get(seatHold.getRow());
		List<Integer> seatsList = seatHold.getSeats();
		for (Integer seat : seatsList) {
			seatsInRow.put(seat, status);
		}
		updateLevel.getTickets().put(seatHold.getRow(), seatsInRow);
		ticketLevels.put(seatHold.getLevelId(), updateLevel);
		seatHold.setStatus(status);
		seats.put(seatHold.getSeatHoldId(), seatHold);
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		SeatHold seatHold = new SeatHold();
		int min = minLevel.isPresent() ? minLevel.get() : 1;
		int max = maxLevel.isPresent() ? maxLevel.get() : 4;
		for (int i = min; i < max; i++) {
			Map<String, Object> levelSeats = findAvailableSeatAndRowNums(ticketLevels.get(Long.valueOf(i)), numSeats);
			if (!levelSeats.isEmpty()) {
				Long seatHoldId = (long) (seats.size() + 1);
				seatHold = new SeatHold(ticketLevels.get(Long.valueOf(i)).getLevelName(),
						ticketLevels.get(Long.valueOf(i)).getLevelId(), ticketLevels.get(Long.valueOf(i)).getPrice(),
						(int) levelSeats.get("row"), (List<Integer>) levelSeats.get("seats"), customerEmail,
						seatHoldId);
				seats.put(seatHold.getSeatHoldId(), seatHold);
				changeSeatStatus(seatHold, "hold");
				break;
			}
		}

		return seatHold;
	}

	@Override
	public String reserveSeats(SeatHold seatHold) {
		boolean matches = true;
		String response = "Unfortunately the seats are not available at this time.";
		TicketLevel updateLevel = ticketLevels.get(seatHold.getLevelId());
		HashMap<Integer, String> seatsInRow = updateLevel.getTickets().get(seatHold.getRow());
		List<Integer> seatsList = seatHold.getSeats();
		for (Integer seat : seatsList) {
			if (!seatsInRow.get(seat).equals(seatHold.getStatus())) {
				matches = false;
				break;
			}
		}

		if (seatHold.getStatus().equalsIgnoreCase("hold") && matches) {
			changeSeatStatus(seatHold, "reserved");
			response = "Congratulations, your seats have been reserved!";
		}
		return response;
	}

	/**
	 * This method finds a seatHold object based on the given seatHold object's
	 * id
	 * 
	 * @param seatHoldId
	 *            The seatHold object ID to look for
	 * @return the seatHoldObject
	 */
	public SeatHold findSeatHold(Long seatHoldId) {
		SeatHold fetchSeatHold = seats.get(seatHoldId);
		return fetchSeatHold;
	}

}
