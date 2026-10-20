package entity;

public class Restaurant {
    public static final int MAX_CUSTOMERS = 6;

    private int turnNumber;
    private int revenueCents;
    private int abandonedCount;
    private final Customer[] customers;
    private int customerCount;
    private final Customer[] readyToPay;
    private int readyToPayCount;
    private final Table[] tables = {new Table(1), new Table(2)};
    private final WaitingQueue waitingQueue;
    private final Menu menu;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;

    public Restaurant() {
        turnNumber = 0;
        revenueCents = 0;
        abandonedCount = 0;
        customers = new Customer[MAX_CUSTOMERS];
        readyToPay = new Customer[MAX_CUSTOMERS];
        waitingQueue = new WaitingQueue();
        menu = new Menu();
        waiter = new Waiter();
        chef = new Chef();
        cashier = new Cashier();
    }

    public void addCustomer(Customer c) {
        customers[customerCount] = c;
        customerCount++;
        waitingQueue.enqueue(c);
    }

    public Customer[] getWaitingCustomers() {
        return waitingQueue.getCustomers();
    }

    public Customer takeOldestReadyToPay() {
        if (readyToPayCount == 0) {
            return null;
        }
        Customer c = readyToPay[0];
        for (int i = 1; i < readyToPayCount; i++) {
            readyToPay[i - 1] = readyToPay[i];
        }
        readyToPayCount--;
        readyToPay[readyToPayCount] = null;
        return c;
    }

    public void addReadyToPay(Customer c) {
        readyToPay[readyToPayCount] = c;
        readyToPayCount++;
    }

    public Table getFreeTable() {
        for (Table t : tables) {
            if (!t.isOccupied()) {
                return t;
            }
        }
        return null;
    }

    public void recordPayment(Customer c, int amountCents) {
        revenueCents += amountCents;
        Table t = c.getTable();
        if (t != null) {
            t.free();
        }
    }

    public void recordAbandonment() {
        abandonedCount++;
    }

    public int getPaidCustomerCount() {
        int count = 0;
        for (int i = 0; i < customerCount; i++) {
            Customer c = customers[i];
            if (c.getState() == CustomerState.PAID) {
                count++;
            }
        }
        return count;
    }

    public int getServedCount() {
        int count = 0;
        for (int i = 0; i < customerCount; i++) {
            Customer c = customers[i];
            Order o = c.getOrder();
            if (o != null
                    && (o.getStatus() == OrderStatus.SERVED || o.getStatus() == OrderStatus.PAID)) {
                count++;
            }
        }
        return count;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public double getAveragePaidSatisfaction() {
        int count = 0;
        int sum = 0;
        for (int i = 0; i < customerCount; i++) {
            Customer c = customers[i];
            if (c.getState() == CustomerState.PAID) {
                count++;
                sum += c.getSatisfactionWhenPaid();
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return (double) sum / count;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void nextTurn() {
        turnNumber++;
    }

    public int getAbandonedCount() {
        return abandonedCount;
    }

    public Customer[] getCustomers() {
        Customer[] result = new Customer[customerCount];
        for (int i = 0; i < customerCount; i++) {
            result[i] = customers[i];
        }
        return result;
    }

    public Table[] getTables() {
        Table[] result = new Table[tables.length];
        for (int i = 0; i < tables.length; i++) {
            result[i] = tables[i];
        }
        return result;
    }

    public Menu getMenu() {
        return menu;
    }

    public WaitingQueue getWaitingQueue() {
        return waitingQueue;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }
}
