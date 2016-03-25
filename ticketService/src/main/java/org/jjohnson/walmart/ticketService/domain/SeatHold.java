package org.jjohnson.walmart.ticketService.domain;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SeatHold Pojo
 * 
 * @author jon
 *
 */
@XmlRootElement
public class SeatHold {

	static Logger log = Logger.getLogger(SeatHold.class.getName());

	Long seatHoldId;
	String userName;
	Long levelId;
	TicketLevel level;
	String levelName;
	double price;
	int row;
	int seatsDesired;
	int minLevel;
	int maxLevel;
	List<Integer> seats;
	String status = "available";

	/**
	 * default empty constructor
	 */
	public SeatHold() {

	}

	/**
	 * Constructor for the seathold object
	 * 
	 * @param name
	 *            the identifying name of the level
	 * @param id
	 *            the level id
	 * @param price
	 *            the cost for the ticket at this level
	 * @param rows
	 *            the amount of rows for this level
	 * @param seats
	 *            the amount of seats per row for this level
	 */
	public SeatHold(String name, Long id, double price, int row, List<Integer> seats, String userEmail,
			Long seatHoldId) {
		this.levelName = name;
		this.levelId = id;
		this.price = price;
		this.row = row;
		this.seats = seats;
		this.userName = userEmail;
		this.seatHoldId = seatHoldId;
	}

	@XmlElement
	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	@XmlElement
	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@XmlElement
	public int getSeatsDesired() {
		return seatsDesired;
	}

	public void setSeatsDesired(int seatsDesired) {
		this.seatsDesired = seatsDesired;
	}

	@XmlElement
	public String getStatus() {
		return status;
	}

	public synchronized void setStatus(String status) {
		this.status = status;
	}

	@XmlElement
	public Long getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(Long seatHoldId) {
		this.seatHoldId = seatHoldId;
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
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@XmlElement
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement
	public List<Integer> getSeats() {
		return seats;
	}

	public void setSeats(List<Integer> seats) {
		this.seats = seats;
	}
}
