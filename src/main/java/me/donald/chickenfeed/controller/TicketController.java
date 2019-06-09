package me.donald.chickenfeed.controller;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donald.chickenfeed.domain.ticket.QTicket;
import me.donald.chickenfeed.domain.ticket.Ticket;
import me.donald.chickenfeed.model.Numbers;
import me.donald.chickenfeed.model.response.TicketStream;
import me.donald.chickenfeed.model.response.TicketStreamElement;
import me.donald.chickenfeed.service.application.TicketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TicketController {

	@Value("${ticket.stream.size.init}")
	private int TICKET_STREAM_INIT_SIZE;

	@Value("${ticket.stream.size.maximum}")
	private int TICKET_STREAM_MAX_SIZE;

	private final TicketService ticketService;
	private final EntityManager entityManager;

	@PostMapping("/tickets")
	public ResponseEntity createTicket(@RequestBody Numbers reqNumbers) {
		if (!reqNumbers.validateLength())
			return ResponseEntity.badRequest().body("잘못된 길이의 요청입니다.");

		Integer[] numbers = reqNumbers.getNumbers();
		return ResponseEntity.ok(ticketService.createTicket(numbers));
	}

	@GetMapping("/tickets")
	public ResponseEntity findLastTickets(@RequestParam(name = "lastTicketNo", required = false) Long previousLastTicketNo) {
		log.debug("Previous Last Ticket No : '{}'", previousLastTicketNo);

		QTicket qTicket = QTicket.ticket;
		List<Ticket> ticketList;
		if (previousLastTicketNo == null) {
			ticketList = new JPAQuery<>(entityManager)
					.select(qTicket)
					.from(qTicket)
					.orderBy(qTicket.ticketNo.desc())
					.limit(TICKET_STREAM_INIT_SIZE)
					.fetch();
		} else {
			ticketList = new JPAQuery<>(entityManager)
					.select(qTicket)
					.from(qTicket)
					.orderBy(qTicket.ticketNo.desc())
					.where(qTicket.ticketNo.gt(previousLastTicketNo))
					.limit(TICKET_STREAM_MAX_SIZE)
					.fetch();
		}

		if (ticketList.isEmpty())
			return ResponseEntity.ok(new TicketStream(previousLastTicketNo, Collections.emptyList()));

		List<TicketStreamElement> elements = ticketList.stream()
				.map(ticket -> new TicketStreamElement(ticket.getTicketNumbers(), ticket.getIssueTime()))
				.collect(Collectors.toList());

		long currentLastTicketNo = ticketList.get(0).getTicketNo();

		return ResponseEntity.ok(new TicketStream(currentLastTicketNo, elements));
	}
}
