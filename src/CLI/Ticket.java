package CLI;

public class Ticket {
    private final String id;
    private final String eventName;
    private final double price;
    private static int counter = 0;

    public Ticket( String eventName, double price) {
        this.eventName = eventName;
        this.price = price;
        counter++;
        this.id = "Ticket: " + counter;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", eventName=" + eventName + ", price=" + price + "]";
    }
}
