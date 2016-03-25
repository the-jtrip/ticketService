package org.jjohnson.walmart.ticketService.config;

import org.jjohnson.walmart.ticketService.controller.TicketController;
import org.jjohnson.walmart.ticketService.service.TicketServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring configuration class
 * 
 * @author jon
 *
 */
@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "org.jjohnson.walmart.ticketService")
public class TicketServiceConfig {

	@Bean
	public TicketController ticketController() {
		return new TicketController();
	}

	@Bean
	public TicketServiceImpl ticketServiceImpl() {
		return new TicketServiceImpl();
	}
}
