package org.jjohnson.walmart.ticketService.domain;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Pojo for ticket level
 * 
 * @author jon
 *
 */
@XmlRootElement
public class TicketLevel {

	Long levelId;
	String levelName;
	double price;
	int rows;
	int seats;
	Map<Integer, HashMap<Integer, String>> tickets = new HashMap<>();

	@XmlElement
	public Map<Integer, HashMap<Integer, String>> getTickets() {
		return tickets;
	}

	public void setTickets(Map<Integer, HashMap<Integer, String>> tickets) {
		this.tickets = tickets;
	}

	@XmlElement
	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	@XmlElement
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@XmlElement
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@XmlElement
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@XmlElement
	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public TicketLevel() {

	}

	/**
	 * The constructor for the ticket level
	 * 
	 * @param levelId
	 *            the id of the ticket level
	 * @param levelName
	 *            the name of the ticket level
	 * @param price
	 *            the price per ticket
	 * @param rows
	 *            the amount of rows in the given level
	 * @param seats
	 *            the amount of seats per row
	 */
	public TicketLevel(Long levelId, String levelName, double price, int rows, int seats) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.price = price;
		this.rows = rows;
		this.seats = seats;
		HashMap<Integer, String> rowSeats = new HashMap<>();
		for (int seat = 0; seat < seats; seat++) {
			rowSeats.put(seat, "available");
		}
		for (int row = 0; row < rows; row++) {
			this.tickets.put(row, (HashMap<Integer, String>) rowSeats.clone());
		}
	}

}
