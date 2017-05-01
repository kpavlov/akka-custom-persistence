package example.api;

public class WorkerUpdate {
    
    private final int healthDelta;
    private final int manaDelta;

    public WorkerUpdate(int healthDelta, int manaDelta) {
        this.healthDelta = healthDelta;
        this.manaDelta = manaDelta;
    }

    public int getHealthDelta() {
        return healthDelta;
    }

    public int getManaDelta() {
        return manaDelta;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkerUpdate{");
        sb.append("healthDelta=").append(healthDelta);
        sb.append(", manaDelta=").append(manaDelta);
        sb.append('}');
        return sb.toString();
    }
}
