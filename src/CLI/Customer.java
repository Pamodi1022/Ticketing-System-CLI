package CLI;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerId;
    private final int retrievalRate;
    private boolean running = true;

    public Customer(TicketPool ticketPool, int customerId, int retrievalRate) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        while (running) {
            if (ticketPool.isSimulationActive()){
                break;
            }
            ticketPool.removeTicket(customerId);
            try{
                Thread.sleep(retrievalRate * 1000L);
            }catch(InterruptedException e){
                System.out.println("Customer: " + customerId + " is interrupted");
            }
        }
    }

    public void stop() {
        running = false;
    }
}
