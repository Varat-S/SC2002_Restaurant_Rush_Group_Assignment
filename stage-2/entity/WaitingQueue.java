package entity;

public class WaitingQueue {
    private final Customer[] waiting;
    private int count;

    public WaitingQueue() {
        waiting = new Customer[Restaurant.MAX_CUSTOMERS];
    }

    public void enqueue(Customer c) {
        waiting[count] = c;
        count++;
    }

    public Customer dequeue() {
        if (count == 0) {
            return null;
        }
        Customer c = waiting[0];
        remove(c);
        return c;
    }

    public boolean remove(Customer c) {
        for (int i = 0; i < count; i++) {
            if (waiting[i] == c) {
                for (int j = i + 1; j < count; j++) {
                    waiting[j - 1] = waiting[j];
                }
                count--;
                waiting[count] = null;
                return true;
            }
        }
        return false;
    }

    public Customer peekOldest() {
        if (count == 0) {
            return null;
        }
        return waiting[0];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public Customer[] getCustomers() {
        Customer[] result = new Customer[count];
        for (int i = 0; i < count; i++) {
            result[i] = waiting[i];
        }
        return result;
    }
}
