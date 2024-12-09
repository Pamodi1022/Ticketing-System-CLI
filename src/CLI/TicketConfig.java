package CLI;

public class TicketConfig {
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final int ticketRetrievalRate;
    private final int maxCapacity;

    public TicketConfig(int totalTickets, int ticketReleaseRate, int ticketRetrievalRate, int maxCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.maxCapacity = maxCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getTicketRetrievalRate() {
        return ticketRetrievalRate;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
}
