package org.jjohnson.walmart.ticketService.config;

import java.util.HashMap;
import java.util.Map;

import org.jjohnson.walmart.ticketService.domain.SeatHold;
import org.jjohnson.walmart.ticketService.domain.TicketLevel;

/**
 * Stub for in-memory farce of a database
 * 
 * @author jon
 *
 */
public class MemoryDB {

	private static Map<Long, SeatHold> seatHolds = new HashMap<>();
	private static Map<Long, TicketLevel> ticketLevels = new HashMap<>();

	public static Map<Long, SeatHold> getSeatsHolds() {
		return seatHolds;
	}

	public static Map<Long, TicketLevel> getTicketLevels() {
		return ticketLevels;
	}

}
