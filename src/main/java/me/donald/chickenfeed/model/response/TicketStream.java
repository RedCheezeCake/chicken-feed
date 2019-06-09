package me.donald.chickenfeed.model.response;


import lombok.Getter;

import java.util.List;

@Getter
public class TicketStream {

	private Long lastTicketNo;

	private List<TicketStreamElement> tickets;

	public TicketStream(Long lastTicketNo, List<TicketStreamElement> tickets) {
		this.lastTicketNo = lastTicketNo;
		this.tickets = tickets;
	}
}
