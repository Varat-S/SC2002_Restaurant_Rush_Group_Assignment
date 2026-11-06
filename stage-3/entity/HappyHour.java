package entity;

public class HappyHour extends PricingPolicy {
    private static final double DISCOUNT_RATE = 0.8;
    private static final int RECOVERY_POINTS = 15;
    private static final int DURATION_TURNS = 2;
    private int remainingTurns;

    public HappyHour() {
        remainingTurns = DURATION_TURNS;
    }

    public int applyDiscount(int cents) {
        if (!isActive()) {
            return cents;
        }
        return (int) Math.round(cents * DISCOUNT_RATE);
    }

    public int getSatisfactionRecovery() {
        if (!isActive()) {
            return 0;
        }
        return RECOVERY_POINTS;
    }

    public boolean isActive() {
        return remainingTurns > 0;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    public void advanceTurn() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }
}
