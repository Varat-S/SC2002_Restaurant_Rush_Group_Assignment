package entity;

public class PricingPolicy {
    public int applyDiscount(int cents) {
        return cents;
    }

    public int getSatisfactionRecovery() {
        return 0;
    }

    public boolean isActive() {
        return false;
    }

    public int getRemainingTurns() {
        return 0;
    }

    public void advanceTurn() {
        // Standard pricing has no timed effect.
    }
}
