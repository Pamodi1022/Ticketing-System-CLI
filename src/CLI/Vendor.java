package CLI;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseRate;
    private boolean running = true;

    public Vendor(TicketPool ticketPool, int vendorId, int releaseRate) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        while (running) {
            if (ticketPool.isSimulationActive()){
                break;
            }
            ticketPool.addTicket("Concert",50.0,vendorId);

            try{
                Thread.sleep(releaseRate*1000L);
            }catch(InterruptedException e){
                System.out.println("Vendor: " + vendorId + "thread interrupted");
                break;
            }
        }
    }
    public void stop() {
        running = false;
    }
}
