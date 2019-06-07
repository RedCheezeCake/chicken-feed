package me.donald.chickenfeed.controller;

import lombok.RequiredArgsConstructor;
import me.donald.chickenfeed.model.Numbers;
import me.donald.chickenfeed.service.application.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@PostMapping("/tickets")
	public ResponseEntity createTicket(@RequestBody Numbers reqNumbers) {
		if (!reqNumbers.validateLength())
			return ResponseEntity.badRequest().body("잘못된 길이의 요청입니다.");

		Integer[] numbers = reqNumbers.getNumbers();
		return ResponseEntity.ok(ticketService.createTicket(numbers));
	}
}
