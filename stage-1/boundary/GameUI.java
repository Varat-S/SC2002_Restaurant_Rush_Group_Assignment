package boundary;

import entity.Customer;
import entity.GameResult;
import entity.Order;
import entity.Restaurant;
import entity.Staff;
import entity.Table;

import java.util.Locale;
import java.util.Scanner;

public class GameUI {
    private final Scanner input;

    public GameUI() {
        input = new Scanner(System.in);
    }

    public void showTurnStart(int turn, Restaurant restaurant) {
        System.out.println();
        System.out.println("=== Turn " + turn + " ===");
        System.out.println("Revenue: " + formatCents(restaurant.getRevenueCents()));
        System.out.println("Average paid satisfaction: " + averageSatisfaction(restaurant));
        System.out.println("Waiting queue: " + waitingQueueText(restaurant));

        System.out.println("Tables:");
        for (Table t : restaurant.getTables()) {
            Customer c = t.getSeatedCustomer();
            if (c == null) {
                System.out.println("  Table " + t.getTableId() + ": free");
            } else {
                System.out.println("  Table " + t.getTableId() + ": customer "
                        + c.getCustomerId() + " (" + c.getState() + ")");
            }
        }

        System.out.println("Customers:");
        if (restaurant.getCustomers().length == 0) {
            System.out.println("  (none)");
        } else {
            for (Customer c : restaurant.getCustomers()) {
                System.out.println("  " + describeCustomer(c));
            }
        }

        System.out.println("Staff: waiter " + availability(restaurant.getWaiter())
                + ", chef " + availability(restaurant.getChef())
                + ", cashier " + availability(restaurant.getCashier()));
    }

    public void showWaiterOptions(String[] options) {
        System.out.println();
        System.out.println("Waiter - choose one task:");
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + i + ") " + options[i]);
        }
    }

    public void showChefOptions(String[] options) {
        System.out.println();
        System.out.println("Chef - choose one task:");
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + i + ") " + options[i]);
        }
    }

    public int readChoice() {
        System.out.print("Choice: ");
        if (!input.hasNextLine()) {
            return 0;
        }
        String line = input.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showEndOfTurn(int turn, Restaurant restaurant) {
        System.out.println();
        System.out.println("--- End of turn " + turn + " ---");
        System.out.println("Served so far: " + restaurant.getServedCount());
        System.out.println("Unhappy departures: " + restaurant.getAbandonedCount());
        System.out.println("Revenue: " + formatCents(restaurant.getRevenueCents()));
        System.out.println("Average paid satisfaction: " + averageSatisfaction(restaurant));
        System.out.println("Turns used: " + turn);
    }

    public void showResult(GameResult result, Restaurant restaurant, int targetPaidCustomers,
                           int targetRevenueCents, int targetAverageSatisfaction) {
        System.out.println();
        System.out.println("=== " + result + " ===");
        System.out.println("Paid customers: " + restaurant.getPaidCustomerCount()
                + " (target " + targetPaidCustomers + ")");
        System.out.println("Revenue: " + formatCents(restaurant.getRevenueCents())
                + " (target " + formatCents(targetRevenueCents) + ")");
        System.out.println("Average paid satisfaction: " + averageSatisfaction(restaurant)
                + " (target " + String.format(Locale.US, "%.1f",
                (double) targetAverageSatisfaction) + ")");
    }

    private String describeCustomer(Customer c) {
        String text = "customer " + c.getCustomerId()
                + ": satisfaction " + c.getSatisfaction()
                + ", " + c.getState();
        Order o = c.getOrder();
        if (o == null) {
            return text + ", no order";
        }
        return text + ", order " + o.getItem().getName() + " " + o.getStatus()
                + " (" + o.getRemainingPrepUnits() + " unit(s) left)";
    }

    private String waitingQueueText(Restaurant restaurant) {
        Customer[] waiting = restaurant.getWaitingCustomers();
        if (waiting.length == 0) {
            return "(empty)";
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < waiting.length; i++) {
            if (i > 0) {
                text.append(", ");
            }
            text.append("customer ").append(waiting[i].getCustomerId());
        }
        return text.toString();
    }

    private String availability(Staff staff) {
        if (staff.hasActed()) {
            return "used";
        }
        return "available";
    }

    private String averageSatisfaction(Restaurant restaurant) {
        if (restaurant.getPaidCustomerCount() == 0) {
            return "N/A";
        }
        return String.format(Locale.US, "%.1f", restaurant.getAveragePaidSatisfaction());
    }

    private String formatCents(int cents) {
        return String.format(Locale.US, "$%.2f", cents / 100.0);
    }
}
