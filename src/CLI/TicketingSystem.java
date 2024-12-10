package CLI;

import java.io.*;
import java.util.Scanner;

public class TicketingSystem {
    public static void main(String[] args) {
        final String RED = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m"; // Reset color

        Scanner scanner = new Scanner(System.in);
        TicketConfig config = loadOrInitializeConfig(scanner);

        TicketPool ticketPool = new TicketPool(config.getMaxCapacity());
        ticketPool.setTotalTickets(config.getTotalTickets());

        Vendor[] vendors = new Vendor[5];  // 5 vendors
        Customer[] customers = new Customer[10];  // 10 customers

        // Create vendors and customers, but don't start threads yet
        for (int i = 0; i < 5; i++) {
            vendors[i] = new Vendor(ticketPool, config.getTicketReleaseRate(), i + 1);
        }

        for (int i = 0; i < 10; i++) {
            customers[i] = new Customer(ticketPool, config.getTicketRetrievalRate(), i + 1);
        }

        System.out.println(YELLOW+"Ticket System Configuration Loaded."+RESET);


        boolean simulationStarted = false;
        while (true) {
            System.out.println("Enter 'start' to begin the simulation, or 'stop' to terminate the system.");
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            if ("start".equals(command) && !simulationStarted) {
                // Start vendor and customer threads
                for (Vendor vendor : vendors) {
                    new Thread(vendor).start();
                }

                for (Customer customer : customers) {
                    new Thread(customer).start();
                }

                simulationStarted = true;
                System.out.println(RED+"Ticket System Simulation Started.");



            } else if ("stop".equals(command)) {
                // Stop the vendor threads
                for (Vendor vendor : vendors) {
                    vendor.stop();
                }
                System.out.println("Warning:Vendor thread was interrupted");

                // Stop the customer threads
                for (Customer customer : customers) {
                    customer.stop();
                }
                System.out.println("Warning:Customer thread was interrupted");

                System.out.println("Simulation terminated. Thank you for using the Real-Time Ticket Management System!");
                break;  // Exit the loop and end the program
            } else {
                System.out.println("Invalid command. Please enter 'start' or 'stop'.");
            }
        }
        scanner.close();
    }

    private static TicketConfig loadOrInitializeConfig(Scanner scanner) {
        String response;

        // Loop until the user enters "yes" or "no"
        while (true) {
            System.out.print("Would you like to load the existing configuration from file? (yes/no): ");
            response = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(response) || "no".equals(response)) {
                break;  // Exit the loop if valid input is entered
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        if ("yes".equals(response)) {
            try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
                // Read the configuration line by line and parse it
                int totalTickets = 0, ticketReleaseRate = 0, ticketRetrievalRate = 0, maxCapacity = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        switch (parts[0]) {
                            case "TotalTickets":
                                totalTickets = Integer.parseInt(parts[1]);
                                break;
                            case "TicketReleaseRate":
                                ticketReleaseRate = Integer.parseInt(parts[1]);
                                break;
                            case "TicketRetrievalRate":
                                ticketRetrievalRate = Integer.parseInt(parts[1]);
                                break;
                            case "MaxCapacity":
                                maxCapacity = Integer.parseInt(parts[1]);
                                break;
                        }
                    }
                }

                System.out.println("Configuration loaded successfully:");
                System.out.println("Total Tickets: " + totalTickets);
                System.out.println("Ticket Release Rate: " + ticketReleaseRate);
                System.out.println("Ticket Retrieval Rate: " + ticketRetrievalRate);
                System.out.println("Max Capacity: " + maxCapacity);

                return new TicketConfig(totalTickets, ticketReleaseRate, ticketRetrievalRate, maxCapacity);

            } catch (IOException | NumberFormatException e) {
                System.out.println("Error loading configuration. Initializing a new configuration.");
            }
        }

        return initializeConfig(scanner);
    }

    private static TicketConfig initializeConfig(Scanner scanner) {
        int totalTickets = getValidatedInput(scanner, "Enter total tickets (must be greater than 0): ");
        int ticketReleaseRate = getValidatedInput(scanner, "Enter ticket release rate (must be greater than 0): ");
        int ticketRetrievalRate = getValidatedInput(scanner, "Enter customer retrieval rate (must be greater than 0): ");
        int maxCapacity = getValidatedInput(scanner, "Enter max ticket capacity (must be greater than 0): ");

        TicketConfig config = new TicketConfig(totalTickets, ticketReleaseRate, ticketRetrievalRate, maxCapacity);

        // Save the configuration as plain text
        try (PrintWriter writer = new PrintWriter(new FileWriter("config.txt"))) {
            writer.println("TotalTickets=" + config.getTotalTickets());
            writer.println("TicketReleaseRate=" + config.getTicketReleaseRate());
            writer.println("TicketRetrievalRate=" + config.getTicketRetrievalRate());
            writer.println("MaxCapacity=" + config.getMaxCapacity());
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }

        return config;
    }

    private static int getValidatedInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0) break;
                System.out.println("Error: Value must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a valid integer.");
            }
        }
        return value;
    }
}
