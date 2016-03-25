package org.jjohnson.walmart.ticketService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.jjohnson.walmart.ticketService.domain.SeatHold;
import org.jjohnson.walmart.ticketService.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The rest controller for the ticket services
 * 
 * @author jon
 *
 */
@RestController
@SessionAttributes({ "heldSeats" })
@RequestMapping(value = "/tickets")
public class TicketController {

	static Logger log = Logger.getLogger(TicketController.class.getName());
	static List<SeatHold> heldSeats = new ArrayList<>();

	@Autowired
	TicketServiceImpl tickeService;

	/**
	 * Every 15 seconds the local 'heldseats' list is checked. If it's empty,
	 * the method populates it with all the seatHold objects that currently have
	 * a status of 'hold'. If it's not empty, the list is iterated through, and
	 * all items that still have a 'status' of 'hold' are set to 'available'.
	 * The list is then emptied.
	 */
	@Scheduled(fixedRate = 15000)
	public void releaseTicketHolds() {
		List<SeatHold> seatsList = tickeService.getAllSeatHold();
		if (heldSeats.isEmpty()) {
			for (SeatHold seat : seatsList) {
				if (seat.getStatus().equalsIgnoreCase("hold")) {
					heldSeats.add(seat);
				}
			}
		} else {
			for (SeatHold seat : heldSeats) {
				if (seatsList.get(seat.getSeatHoldId().intValue()).getStatus().equalsIgnoreCase("hold")) {
					tickeService.changeSeatStatus(seat, "available");
				}
			}
			heldSeats.clear();
		}

	}

	/**
	 * This method consumes a minimal seatHold xml, that only needs an email and
	 * a quantity of desired seats specified. Alternately it can populate a max
	 * and min level
	 * 
	 * @param seatHoldPosted
	 *            The minimal seatHold xml
	 * @return The fully populated seatHold xml based on the input
	 */
	@RequestMapping(value = "/holdTickets", method = RequestMethod.POST, consumes = "application/xml")
	public SeatHold holdTickets(@RequestBody SeatHold seatHoldPosted) {
		return tickeService.findAndHoldSeats(seatHoldPosted.getSeatsDesired(),
				Optional.ofNullable(seatHoldPosted.getMinLevel()), Optional.ofNullable(seatHoldPosted.getMaxLevel()),
				seatHoldPosted.getUserName());
	}

	/**
	 * This returns the seatHold object that has the seatHold Id that matches
	 * the given parameter
	 * 
	 * @param seatsHoldId
	 *            the user entered value for the seatHoldId
	 * @return the matching seatHold object
	 */
	@RequestMapping("/myTicket/{seatsHoldId}")
	public SeatHold findHoldSeats(@PathVariable Long seatsHoldId) {
		return tickeService.findSeatHold(seatsHoldId);
	}

	/**
	 * Finds all available seats in the building
	 * 
	 * @return a count of all the available seats in the building
	 */
	@RequestMapping("/seatsAvailable")
	public int seatsAvailable() {
		return tickeService.numSeatsAvailable(Optional.ofNullable(null));
	}

	/**
	 * Find counts of all available seats within a given level
	 * 
	 * @param levelId
	 *            the id of the level to search in
	 * @return the count of available seats in the level specified
	 */
	@RequestMapping("/seatsAvailable/{levelId}")
	public int seatsAvailableByLevel(@PathVariable int levelId) {
		return tickeService.numSeatsAvailable(Optional.of(levelId));
	}

	/**
	 * This method attempts to reserve seats based on the given seatHold object.
	 * 
	 * @param seatHoldPosted
	 *            the seatHold xml submitted
	 * @return A string indicating whether or not the seat reservation was
	 *         successful
	 */
	@RequestMapping(value = "/reserveSeats", method = RequestMethod.POST, consumes = "application/xml")
	public String reserveSeats(@RequestBody SeatHold seatHoldPosted) {
		return tickeService.reserveSeats(seatHoldPosted);
	}
}
