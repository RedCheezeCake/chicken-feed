package me.donald.chickenfeed.service;

import lombok.extern.slf4j.Slf4j;
import me.donald.chickenfeed.domain.ticket.Ticket;
import me.donald.chickenfeed.model.Numbers;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketService {

	/**
	 * 주어진 번호 배열을 가지고 티켓 생성
	 *
	 * @param numbers 요청 번호 배열
	 * @return 생성된 번호 배열
	 */
	public Numbers createTicket(Integer[] numbers) {
		Ticket ticket = new Ticket(0, numbers);// TODO 라운드 최신화

		boolean issuable = ticket.checkIssuable();
		if (!issuable)
			throw new IllegalArgumentException("티켓을 발급할 수 없습니다.");

		ticket.issueTicket();

		return new Numbers(ticket.getTicketNumbers());
	}
}
