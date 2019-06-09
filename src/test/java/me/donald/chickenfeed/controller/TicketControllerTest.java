package me.donald.chickenfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.donald.chickenfeed.domain.ticket.Ticket;
import me.donald.chickenfeed.domain.ticket.TicketType;
import me.donald.chickenfeed.domain.ticket.repository.TicketRepository;
import me.donald.chickenfeed.model.Numbers;
import me.donald.chickenfeed.service.application.TicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

	@Autowired
	TicketService ticketService;

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

	@Test
	public void findLastTickets_noParameter() throws Exception {
		// given
		Numbers ticket1 = ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});
		Numbers ticket2 = ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});
		Numbers ticket3 = ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});

		// when
		mockMvc.perform(get("/tickets"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("lastTicketNo").value(3))
				.andExpect(jsonPath("tickets[0].numbers[0]").value(ticket3.getNumbers()[0]))
				.andExpect(jsonPath("tickets[0].numbers[1]").value(ticket3.getNumbers()[1]))
				.andExpect(jsonPath("tickets[0].numbers[2]").value(ticket3.getNumbers()[2]))
				.andExpect(jsonPath("tickets[0].numbers[3]").value(ticket3.getNumbers()[3]))
				.andExpect(jsonPath("tickets[0].numbers[4]").value(ticket3.getNumbers()[4]))
				.andExpect(jsonPath("tickets[0].numbers[5]").value(ticket3.getNumbers()[5]))
				.andExpect(jsonPath("tickets[0].issueTime").exists())
				.andExpect(jsonPath("tickets[1].numbers[0]").value(ticket2.getNumbers()[0]))
				.andExpect(jsonPath("tickets[1].numbers[1]").value(ticket2.getNumbers()[1]))
				.andExpect(jsonPath("tickets[1].numbers[2]").value(ticket2.getNumbers()[2]))
				.andExpect(jsonPath("tickets[1].numbers[3]").value(ticket2.getNumbers()[3]))
				.andExpect(jsonPath("tickets[1].numbers[4]").value(ticket2.getNumbers()[4]))
				.andExpect(jsonPath("tickets[1].numbers[5]").value(ticket2.getNumbers()[5]))
				.andExpect(jsonPath("tickets[1].issueTime").exists())
				.andExpect(jsonPath("tickets[2].numbers[0]").value(ticket1.getNumbers()[0]))
				.andExpect(jsonPath("tickets[2].numbers[1]").value(ticket1.getNumbers()[1]))
				.andExpect(jsonPath("tickets[2].numbers[2]").value(ticket1.getNumbers()[2]))
				.andExpect(jsonPath("tickets[2].numbers[3]").value(ticket1.getNumbers()[3]))
				.andExpect(jsonPath("tickets[2].numbers[4]").value(ticket1.getNumbers()[4]))
				.andExpect(jsonPath("tickets[2].numbers[5]").value(ticket1.getNumbers()[5]))
				.andExpect(jsonPath("tickets[2].issueTime").exists());

		// then
	}

	@Test
	public void findLastTickets_parameter() throws Exception {
		// given
		ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});
		Numbers ticket2 = ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});
		Numbers ticket3 = ticketService.createTicket(new Integer[]{0, 0, 0, 0, 0, 0});

		// when
		mockMvc.perform(get("/tickets")
				.param("lastTicketNo", "1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("lastTicketNo").value(3))
				.andExpect(jsonPath("tickets[0].numbers[0]").value(ticket3.getNumbers()[0]))
				.andExpect(jsonPath("tickets[0].numbers[1]").value(ticket3.getNumbers()[1]))
				.andExpect(jsonPath("tickets[0].numbers[2]").value(ticket3.getNumbers()[2]))
				.andExpect(jsonPath("tickets[0].numbers[3]").value(ticket3.getNumbers()[3]))
				.andExpect(jsonPath("tickets[0].numbers[4]").value(ticket3.getNumbers()[4]))
				.andExpect(jsonPath("tickets[0].numbers[5]").value(ticket3.getNumbers()[5]))
				.andExpect(jsonPath("tickets[0].issueTime").exists())
				.andExpect(jsonPath("tickets[1].numbers[0]").value(ticket2.getNumbers()[0]))
				.andExpect(jsonPath("tickets[1].numbers[1]").value(ticket2.getNumbers()[1]))
				.andExpect(jsonPath("tickets[1].numbers[2]").value(ticket2.getNumbers()[2]))
				.andExpect(jsonPath("tickets[1].numbers[3]").value(ticket2.getNumbers()[3]))
				.andExpect(jsonPath("tickets[1].numbers[4]").value(ticket2.getNumbers()[4]))
				.andExpect(jsonPath("tickets[1].numbers[5]").value(ticket2.getNumbers()[5]))
				.andExpect(jsonPath("tickets[1].issueTime").exists());

		// then
	}
}