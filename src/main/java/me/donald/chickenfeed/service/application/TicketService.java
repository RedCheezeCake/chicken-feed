package me.donald.chickenfeed.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donald.chickenfeed.domain.ticket.Ticket;
import me.donald.chickenfeed.domain.ticket.repository.TicketRepository;
import me.donald.chickenfeed.model.Numbers;
import me.donald.chickenfeed.service.domain.RoundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

	private final RoundService roundService;
	private final TicketRepository ticketRepository;

	/**
	 * 주어진 번호 배열을 가지고 티켓 생성
	 *
	 * @param numbers 요청 번호 배열
	 * @return 생성된 번호 배열
	 */
	public Numbers createTicket(Integer[] numbers) {
		int lastRound = roundService.getLastRound();
		Ticket ticket = new Ticket(lastRound, numbers);

		ticket.issueTicket();
		ticketRepository.save(ticket);

		return new Numbers(ticket.getTicketNumbers());
	}
}
