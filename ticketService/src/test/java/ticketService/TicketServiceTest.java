package ticketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.jjohnson.walmart.ticketService.config.TicketServiceConfig;
import org.jjohnson.walmart.ticketService.domain.SeatHold;
import org.jjohnson.walmart.ticketService.service.TicketServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TicketServiceConfig.class })
public class TicketServiceTest {

	static Logger log = Logger.getLogger(TicketServiceTest.class.getName());

	@Autowired
	TicketServiceImpl tickeService;

	@Test
	public void findSeats() {
		Integer test = null;
		// int num = tickeService.numSeatsAvailable(Optional.of(3));
		int num2 = tickeService.numSeatsAvailable(Optional.ofNullable(test));
		// log.info("yo " + num);
		log.info("mama" + num2);
	}

	@Test
	public void testHoldTickets() {
		log.info("num of seats available at start " + tickeService.numSeatsAvailable(Optional.ofNullable(null)));

		SeatHold holdSeats = tickeService.findAndHoldSeats(9, Optional.of(3), Optional.of(4), "jonjohnsonj@gmail.com");
		int num2 = tickeService.numSeatsAvailable(Optional.ofNullable(null));
		log.info("level name: " + holdSeats.getLevelName());
		log.info("user name: " + holdSeats.getUserName());
		log.info("level id: " + holdSeats.getLevelId().toString());
		log.info("seat price: " + holdSeats.getPrice());
		log.info("seat row name: " + holdSeats.getRow());
		String seatNums = "";
		for (Integer seatNum : holdSeats.getSeats()) {
			seatNums += " " + seatNum;
		}
		log.info("seats in row: " + seatNums);
		log.info("remaining seat available: " + num2);
	}

	@Test
	public void testChangeStatus() {
		log.info("------------------num of seats available at start "
				+ tickeService.numSeatsAvailable(Optional.ofNullable(null)));
		List<Integer> seats = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		SeatHold seatHold = new SeatHold("Balcony 1", Long.valueOf(3), 100.00, 7, seats, "jonjohnsonj@gmail.com",
				Long.valueOf(1));
		tickeService.changeSeatStatus(seatHold, "booked");
		log.info("------------------num of seats available at end "
				+ tickeService.numSeatsAvailable(Optional.ofNullable(null)));

	}

	@Test
	public void testTiming() {

		new java.util.Timer().schedule(new java.util.TimerTask() {
			int i = 0;
			boolean check = false;

			@Override
			public void run() {
				System.out.println("Some sort of something bro");
			}
		}, 5000);
	}
}
