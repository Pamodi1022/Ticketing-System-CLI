import java.math.BigDecimal;

public class Ticket {
    private int ticketId;
    private String eventName;
    private BigDecimal ticketPrice; //use big decimal when dealing with prices

    //constructor for Ticket class
    public Ticket(int ticketId, String eventName, BigDecimal ticketPrice) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    //getter and setter for getTicketId
    public int getTicketId() {
        return ticketId;
    }
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    //getter and setter for getEventName
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    //getter and setter for getTicketPrice
    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }
    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    //toString Method
    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", eventName=" + eventName + ", ticketPrice="
                + ticketPrice + "]";

    }

}