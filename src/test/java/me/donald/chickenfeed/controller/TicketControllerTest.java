package me.donald.chickenfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.donald.chickenfeed.domain.ticket.Ticket;
import me.donald.chickenfeed.domain.ticket.TicketType;
import me.donald.chickenfeed.domain.ticket.repository.TicketRepository;
import me.donald.chickenfeed.model.Numbers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TicketControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TicketRepository ticketRepository;

	@Test
	public void createTicket_auto() throws Exception {
		// given
		Numbers reqNumbers = new Numbers(new Integer[]{0, 0, 0, 0, 0, 0});

		// when
		mockMvc.perform(post("/tickets")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(reqNumbers)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("numbers").exists());

		// then
		List<Ticket> tickets = ticketRepository.findAll();
		assertThat(tickets.size()).isEqualTo(1);

		Ticket ticket = tickets.get(0);
		assertThat(ticket.getBalls().size()).isEqualTo(6);
		ticket.getBalls().forEach(ball -> assertThat(ball.isManual()).isFalse());
		assertThat(ticket.getRound()).isEqualTo(0);
		assertThat(ticket.getType()).isEqualTo(TicketType.AUTO);
		assertThat(ticket.getIssueTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
	}

	@Test
	public void createTicket_manual() throws Exception {
		// given
		Numbers reqNumbers = new Numbers(new Integer[]{0, 4, 0, 0, 27, 0});

		// when
		mockMvc.perform(post("/tickets")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(reqNumbers)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("numbers").exists());

		// then
		List<Ticket> tickets = ticketRepository.findAll();
		assertThat(tickets.size()).isEqualTo(1);

		Ticket ticket = tickets.get(0);
		assertThat(ticket.getBalls().size()).isEqualTo(6);
		assertThat(ticket.getBalls().get(0).isManual()).isFalse();
		assertThat(ticket.getBalls().get(1).isManual()).isTrue();
		assertThat(ticket.getBalls().get(2).isManual()).isFalse();
		assertThat(ticket.getBalls().get(3).isManual()).isFalse();
		assertThat(ticket.getBalls().get(4).isManual()).isTrue();
		assertThat(ticket.getBalls().get(5).isManual()).isFalse();
		assertThat(ticket.getRound()).isEqualTo(0);
		assertThat(ticket.getType()).isEqualTo(TicketType.MANUAL);
		assertThat(ticket.getIssueTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
	}

	@Test
	public void createTicket_exceptionNumbersSize() throws Exception {
		// given
		Numbers reqNumbers = new Numbers(new Integer[]{0, 0, 0, 0, 0, 0, 0});

		// when
		mockMvc.perform(post("/tickets")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(reqNumbers)))
				.andDo(print())
				.andExpect(status().isBadRequest());

		// then
		List<Ticket> tickets = ticketRepository.findAll();
		assertThat(tickets.size()).isEqualTo(0);
	}

	@Test
	public void createTicket_exceptionCantIssuable() throws Exception {
		// given
		Numbers reqNumbers = new Numbers(new Integer[]{0, 4, 0, 5, 0, 0});

		// when
		mockMvc.perform(post("/tickets")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(reqNumbers)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string("티켓을 발급할 수 없습니다."));

		// then
		List<Ticket> tickets = ticketRepository.findAll();
		assertThat(tickets.size()).isEqualTo(0);

	}
}