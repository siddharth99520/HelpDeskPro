package com.helpdeskpro.dashboard;

import com.helpdeskpro.asset.AssetRepository;
import com.helpdeskpro.asset.AssetStatus;
import com.helpdeskpro.ticket.TicketRepository;
import com.helpdeskpro.ticket.TicketStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TicketRepository ticketRepository;
    private final AssetRepository assetRepository;

    public DashboardController(TicketRepository ticketRepository, AssetRepository assetRepository) {
        this.ticketRepository = ticketRepository;
        this.assetRepository = assetRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        long activeTickets = ticketRepository.countByStatusNot(TicketStatus.CLOSED);
        long immediateAttentionTickets = ticketRepository.countByPriorityInAndStatusNot(
                Arrays.asList("CRITICAL", "HIGH"), TicketStatus.CLOSED);
        
        long assignedAssets = assetRepository.countByStatus(AssetStatus.ASSIGNED);

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeTickets", activeTickets);
        stats.put("immediateAttentionTickets", immediateAttentionTickets);
        stats.put("assignedAssets", assignedAssets);
        stats.put("slaCompliance", 98.2); // Placeholder until SLA tracking is fully implemented

        return stats;
    }
}
