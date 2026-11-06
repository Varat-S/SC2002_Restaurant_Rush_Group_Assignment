package control;

import boundary.GameUI;
import entity.Customer;
import entity.CriticCustomer;
import entity.RegularCustomer;
import entity.VIPCustomer;
import entity.CustomerState;
import entity.GameResult;
import entity.MenuItem;
import entity.Order;
import entity.OrderStatus;
import entity.Restaurant;
import entity.Table;

public class GameController {
    private static final int MAX_TURNS = 18;
    private static final int[] ARRIVAL_TURNS = {1, 4, 7, 10, 13, 16};
    private static final int TARGET_PAID_CUSTOMERS = 4;
    private static final int TARGET_REVENUE_CENTS = 4500;
    private static final int TARGET_AVERAGE_SATISFACTION = 60;

    private final Restaurant restaurant;
    private final GameUI ui;
    private String lastMessage;

    public GameController(Restaurant restaurant, GameUI ui) {
        this.restaurant = restaurant;
        this.ui = ui;
        this.lastMessage = "";
    }

    public void playGame() {
        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            restaurant.getWaiter().resetForTurn();
            restaurant.getChef().resetForTurn();
            restaurant.getCashier().resetForTurn();
            ui.showTurnStart(turn, restaurant);
            playTurn(turn);
            ui.showEndOfTurn(turn, restaurant);
        }
        ui.showResult(checkResult(), restaurant, TARGET_PAID_CUSTOMERS,
                TARGET_REVENUE_CENTS, TARGET_AVERAGE_SATISFACTION);
    }

    public String[] getWaiterOptions() {
        Customer[] seatable = getSeatableCustomers();
        Customer[] orderable = getOrderableCustomers();
        Customer[] servable = getServableOrders();
        Table freeTable = restaurant.getFreeTable();

        String[] options = new String[1 + seatable.length + orderable.length + servable.length];
        options[0] = "Wait";
        int i = 1;
        for (Customer c : seatable) {
            options[i] = "Seat customer " + c.getCustomerId()
                    + " at table " + freeTable.getTableId();
            i++;
        }
        for (Customer c : orderable) {
            options[i] = "Take order from customer " + c.getCustomerId()
                    + " at table " + c.getTable().getTableId();
            i++;
        }
        for (Customer c : servable) {
            options[i] = "Serve order for customer " + c.getCustomerId()
                    + " (" + c.getOrder().getItem().getName() + ")";
            i++;
        }
        return options;
    }

    public String[] getChefOptions() {
        Customer[] preparable = getPreparableOrders();
        String[] options = new String[1 + preparable.length];
        options[0] = "Wait";
        for (int i = 0; i < preparable.length; i++) {
            Order o = preparable[i].getOrder();
            options[i + 1] = "Advance order for customer " + preparable[i].getCustomerId()
                    + " (" + o.getItem().getName() + ", "
                    + o.getRemainingPrepUnits() + " unit(s) left)";
        }
        return options;
    }

    public boolean handleWaiterChoice(int option) {
        if (restaurant.getWaiter().hasActed()) {
            lastMessage = "The waiter already completed a task this turn.";
            return false;
        }
        if (option == 0) {
            lastMessage = "The waiter waits.";
            return true;
        }

        Customer[] seatable = getSeatableCustomers();
        Customer[] orderable = getOrderableCustomers();
        Customer[] servable = getServableOrders();

        int index = option - 1;
        if (index < 0) {
            lastMessage = "Please enter a number from the list.";
            return false;
        }

        if (index < seatable.length) {
            Customer c = seatable[index];
            Table t = restaurant.getFreeTable();
            if (restaurant.getWaiter().seatCustomer(c, t)) {
                restaurant.getWaitingQueue().remove(c);
                lastMessage = "Seated customer " + c.getCustomerId()
                        + " at table " + t.getTableId() + ".";
                return true;
            }
            lastMessage = "That customer cannot be seated.";
            return false;
        }
        index -= seatable.length;

        if (index < orderable.length) {
            Customer c = orderable[index];
            MenuItem item = c.chooseMenuItem(restaurant.getMenu());
            if (restaurant.getWaiter().takeOrder(c, item) != null) {
                lastMessage = "Customer " + c.getCustomerId() + " orders " + item.getName()
                        + " (" + item.getPrepUnits() + " preparation unit(s)).";
                return true;
            }
            lastMessage = "That order cannot be taken.";
            return false;
        }
        index -= orderable.length;

        if (index < servable.length) {
            Customer c = servable[index];
            if (restaurant.getWaiter().serveOrder(c.getOrder())) {
                lastMessage = "Served the order for customer " + c.getCustomerId() + ".";
                return true;
            }
            lastMessage = "That order cannot be served.";
            return false;
        }

        lastMessage = "Invalid waiter choice. Enter a number from 0 to "
                + (getWaiterOptions().length - 1) + ".";
        return false;
    }

    public boolean handleChefChoice(int option) {
        if (restaurant.getChef().hasActed()) {
            lastMessage = "The chef already completed a task this turn.";
            return false;
        }
        if (option == 0) {
            lastMessage = "The chef waits.";
            return true;
        }

        Customer[] preparable = getPreparableOrders();
        int index = option - 1;
        if (index >= 0 && index < preparable.length) {
            Customer c = preparable[index];
            Order o = c.getOrder();
            restaurant.getChef().advanceOrder(o);
            if (o.getStatus() == OrderStatus.READY) {
                lastMessage = "The order for customer " + c.getCustomerId() + " is READY.";
            } else {
                lastMessage = "Advanced the order for customer " + c.getCustomerId()
                        + " (" + o.getRemainingPrepUnits() + " unit(s) left).";
            }
            return true;
        }

        lastMessage = "Invalid chef choice. Enter a number from 0 to "
                + (getChefOptions().length - 1) + ".";
        return false;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    private void playTurn(int turn) {
        handleArrivals(turn);

        lastMessage = "";

        boolean done = false;
        while (!done) {
            ui.showWaiterOptions(getWaiterOptions());
            int choice = ui.readChoice();
            done = handleWaiterChoice(choice);
            if (!done) {
                ui.showMessage(getLastMessage());
            }
        }
        ui.showMessage(getLastMessage());

        done = false;
        while (!done) {
            ui.showChefOptions(getChefOptions());
            int choice = ui.readChoice();
            done = handleChefChoice(choice);
            if (!done) {
                ui.showMessage(getLastMessage());
            }
        }
        ui.showMessage(getLastMessage());

        settleEatingAndPayment();
        applyWaitingPenalty();
        restaurant.nextTurn();
    }

    private void handleArrivals(int turn) {
        for (int arrivalTurn : ARRIVAL_TURNS) {
            if (arrivalTurn == turn) {
                int customerId = restaurant.getCustomers().length + 1;
                Customer c;
                if (turn == 4) {
                    c = new VIPCustomer(customerId);
                } else if (turn == 10) {
                    c = new CriticCustomer(customerId);
                } else {
                    c = new RegularCustomer(customerId);
                }
                restaurant.addCustomer(c);
                ui.showMessage("Customer " + customerId + " arrived.");
                return;
            }
        }
    }

    private void settleEatingAndPayment() {
        Customer payer = restaurant.takeOldestReadyToPay();
        if (payer != null) {
            int amount = restaurant.getCashier().collectPayment(payer);
            if (amount >= 0) {
                restaurant.recordPayment(payer, amount);
                ui.showMessage("Cashier collected the bill from customer "
                        + payer.getCustomerId() + ".");
            }
        }

        for (Customer c : restaurant.getCustomers()) {
            if (c.finishEating()) {
                restaurant.addReadyToPay(c);
            }
        }
        for (Customer c : restaurant.getCustomers()) {
            if (c.getState() == CustomerState.SEATED) {
                c.startEating();
            }
        }
    }

    private void applyWaitingPenalty() {
        for (Customer c : restaurant.getCustomers()) {
            if (!c.isAwaitingService()) {
                continue;
            }
            c.loseSatisfaction();
            if (c.getSatisfaction() > 0) {
                continue;
            }

            if (c.getState() == CustomerState.WAITING) {
                restaurant.getWaitingQueue().remove(c);
            }
            if (c.getOrder() != null) {
                c.getOrder().cancel();
            }
            Table t = c.getTable();
            if (t != null) {
                t.free();
            }
            c.leave();
            restaurant.recordAbandonment();
            ui.showMessage("Customer " + c.getCustomerId()
                    + " has left unhappy without paying.");
        }
    }

    private GameResult checkResult() {
        boolean enoughPaid = restaurant.getPaidCustomerCount() >= TARGET_PAID_CUSTOMERS;
        boolean enoughRevenue = restaurant.getRevenueCents() >= TARGET_REVENUE_CENTS;
        boolean enoughSatisfaction =
                restaurant.getAveragePaidSatisfaction() >= TARGET_AVERAGE_SATISFACTION;
        if (enoughPaid && enoughRevenue && enoughSatisfaction) {
            return GameResult.VICTORY;
        }
        return GameResult.DEFEAT;
    }

    private Customer[] getSeatableCustomers() {
        if (restaurant.getFreeTable() == null) {
            Customer[] empty = {};
            return empty;
        }
        Customer[] waiting = restaurant.getWaitingCustomers();
        int count = 0;
        for (Customer c : waiting) {
            if (c.getState() == CustomerState.WAITING) {
                count++;
            }
        }
        Customer[] result = new Customer[count];
        int index = 0;
        for (Customer c : waiting) {
            if (c.getState() == CustomerState.WAITING) {
                result[index] = c;
                index++;
            }
        }
        return result;
    }

    private Customer[] getOrderableCustomers() {
        Table[] tables = restaurant.getTables();
        int count = 0;
        for (Table t : tables) {
            Customer c = t.getSeatedCustomer();
            if (c != null && c.getState() == CustomerState.SEATED && c.getOrder() == null) {
                count++;
            }
        }
        Customer[] result = new Customer[count];
        int index = 0;
        for (Table t : tables) {
            Customer c = t.getSeatedCustomer();
            if (c != null && c.getState() == CustomerState.SEATED && c.getOrder() == null) {
                result[index] = c;
                index++;
            }
        }
        return result;
    }

    private Customer[] getServableOrders() {
        Customer[] customers = restaurant.getCustomers();
        int count = 0;
        for (Customer c : customers) {
            if (c.getOrder() != null && c.getOrder().getStatus() == OrderStatus.READY) {
                count++;
            }
        }
        Customer[] result = new Customer[count];
        int index = 0;
        for (Customer c : customers) {
            if (c.getOrder() != null && c.getOrder().getStatus() == OrderStatus.READY) {
                result[index] = c;
                index++;
            }
        }
        return result;
    }

    private Customer[] getPreparableOrders() {
        Customer[] customers = restaurant.getCustomers();
        int count = 0;
        for (Customer c : customers) {
            if (c.getOrder() != null && c.getOrder().getStatus() == OrderStatus.PLACED) {
                count++;
            }
        }
        Customer[] result = new Customer[count];
        int index = 0;
        for (Customer c : customers) {
            if (c.getOrder() != null && c.getOrder().getStatus() == OrderStatus.PLACED) {
                result[index] = c;
                index++;
            }
        }
        return result;
    }
}
