package CLI;

import CLIII.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private int totalTickets;
    private final int maxCapacity;
    private final Queue<Ticket> tickets = new LinkedList<>();
    private boolean simulationActive = true;
    private boolean isWaitingPrinted = false;
    private boolean isCustomerWaitingPrinted = false;
    private long lastRetrievalTime = System.currentTimeMillis();

    private synchronized void checkForTermination() {
        if (totalTickets == 0 && tickets.isEmpty()) {
            System.out.println("Tickets over...");
            System.out.println("Thank you for using the Real-Time Ticket Management System!");
            simulationActive = false;
            notifyAll();
            System.exit(0);
        } else if (totalTickets == 0) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastRetrievalTime;

            // If no ticket has been retrieved for a certain amount of time (indicating slow ticket retrieval)
            if (timeDifference > 1000 && !isWaitingPrinted) {
                System.out.println("Warning: Tickets are available.");
                System.out.println("Customers are not retrieving them quickly due to a high retrieval rate.");
                isWaitingPrinted = true; // Print the message only once
            }
        }
    }

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = maxCapacity;
    }

    public synchronized void addTicket(String eventName, double price, int vendorId) {
        //if (!simulationActive) {
        //   return;
        //}

        if (totalTickets <= 0) {
            checkForTermination();
            return;
        }

        while (tickets.size() >= maxCapacity) {
            try {
                if (!isWaitingPrinted) {
                    System.out.println("Warning: Maximum ticket capacity reached.");
                    System.out.println("Vendor is waiting for space...");
                    isWaitingPrinted = true;
                }
                wait();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor-" + vendorId + " interrupted while waiting.");
            }
        }

        Ticket ticket = new Ticket(eventName, price);
        tickets.offer(ticket);
        totalTickets--;
        System.out.println(getCurrentTimestamp() + " Ticket added by vendor-" + vendorId +
                ", Total Tickets: " + totalTickets + ", Available Tickets: " + tickets.size());

        isWaitingPrinted = false;
        notifyAll();
        checkForTermination();
    }

    public synchronized void removeTicket(int customerId) {
        //if (!simulationActive) {
          //  return;
        //}

        while (tickets.isEmpty() && totalTickets > 0) {
            try {
                if (!isCustomerWaitingPrinted) {
                    System.out.println("Customer is waiting for Tickets...");
                    isCustomerWaitingPrinted = true; // Print the message only once
                }
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer-" + customerId + " interrupted while waiting.");
            }
        }

        if (!tickets.isEmpty()) {
            Ticket ticket = tickets.poll();
            System.out.println(getCurrentTimestamp() + " Ticket retrieved by customer-" + customerId + ": " +
                    ticket + ", Total Tickets: " + totalTickets + ", Available Tickets: " + tickets.size());

            lastRetrievalTime = System.currentTimeMillis(); // Track when the last retrieval happened
            isCustomerWaitingPrinted = false; // Reset the flag for future use
            notifyAll();
            checkForTermination();
        }
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public boolean isSimulationActive() {
        return !simulationActive;
    }
}
