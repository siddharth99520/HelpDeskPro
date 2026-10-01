package com.helpdeskpro;

import com.helpdeskpro.domain.*;
import com.helpdeskpro.exception.AssetAlreadyAssignedException;
import com.helpdeskpro.exception.InvalidTransitionException;
import com.helpdeskpro.service.AssetService;
import com.helpdeskpro.service.TicketService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        TicketService ticketService = new TicketService();
        AssetService assetService = new AssetService();

        User employee = new User(UUID.randomUUID().toString(), "Alice Employee", Role.EMPLOYEE);
        User engineer = new User(UUID.randomUUID().toString(), "Bob Engineer", Role.ENGINEER);
        Category category = new Category(UUID.randomUUID().toString(), "Hardware");
        Asset laptop = new Asset(UUID.randomUUID().toString(), "MacBook Pro");

        assetService.addAsset(laptop);

        System.out.println("Scenario 1: Full happy-path ticket lifecycle");
        Ticket ticket = ticketService.createTicket("Laptop broken", "Screen is flickering", employee, category);
        System.out.println("Ticket created: " + ticket.getStatus());
        
        ticketService.assignTicket(ticket.getId(), engineer);
        System.out.println("Ticket assigned: " + ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        System.out.println("Ticket in progress: " + ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.RESOLVED);
        System.out.println("Ticket resolved: " + ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.CLOSED);
        System.out.println("Ticket closed: " + ticket.getStatus());

        System.out.println("\nScenario 2: Attempted invalid ticket transition");
        try {
            ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
            System.out.println("SUCCESS - this shouldn't happen!");
        } catch (InvalidTransitionException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\nScenario 3: Reopen a ticket");
        Ticket ticket2 = ticketService.createTicket("Need new mouse", "Mouse is broken", employee, category);
        ticketService.assignTicket(ticket2.getId(), engineer);
        ticketService.updateStatus(ticket2, TicketStatus.IN_PROGRESS);
        ticketService.updateStatus(ticket2, TicketStatus.RESOLVED);
        System.out.println("Ticket resolved: " + ticket2.getStatus());
        ticketService.updateStatus(ticket2, TicketStatus.IN_PROGRESS);
        System.out.println("Ticket reopened: " + ticket2.getStatus());
        
        System.out.println("\nScenario 4: Asset Assignment");
        assetService.assign(laptop.getId(), employee);
        System.out.println("Asset assigned to: " + laptop.getAssignedTo().name() + ", Status: " + laptop.getStatus());
        
        System.out.println("\nScenario 5: Attempted double asset assignment");
        try {
            assetService.assign(laptop.getId(), engineer);
            System.out.println("SUCCESS - this shouldn't happen!");
        } catch (AssetAlreadyAssignedException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\nScenario 6: Return asset");
        assetService.returnAsset(laptop.getId());
        System.out.println("Asset returned, Status: " + laptop.getStatus());
    }
}
