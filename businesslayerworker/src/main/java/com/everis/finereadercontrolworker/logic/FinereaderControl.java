package com.everis.finereadercontrolworker.logic;

import com.everis.finereadercontrolworker.interfaces.IFinereaderControl;
import com.everis.ocrworker.FinereaderImpl;
import com.everis.ocrworker.model.Ticket;
import com.everis.ocrworker.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;


@Component
@EntityScan({"com.everis.*"})
public class FinereaderControl implements IFinereaderControl {

    private FinereaderImpl finereaderImplementation = new FinereaderImpl();

    @Autowired
    private TicketRepository ticketRepository;

    public String Run(String imagePath, String exportPath, Integer maxVolume, Integer actualVolume, Integer precision) {
        try {
            return finereaderImplementation.Run(imagePath, exportPath, maxVolume, actualVolume, precision);
        } catch (Exception e) {
            return "Error " + e.toString();
        }
    }

    public void inProcessTicket(String id) {
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setState("Ticket in process");
        ticketRepository.save(ticket);
    }

    public void ticketFinished(String id) {
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setState("Process finished");
        ticketRepository.save(ticket);
    }

    public void ticketWithError(String id, String error) {
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setState("Process finished");
        ticket.setError(error);
        ticketRepository.save(ticket);
    }
}
