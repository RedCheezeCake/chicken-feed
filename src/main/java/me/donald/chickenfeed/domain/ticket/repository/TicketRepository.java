package me.donald.chickenfeed.domain.ticket.repository;

import me.donald.chickenfeed.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
