package com.everis.ocrworker.repository;

import com.everis.ocrworker.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
