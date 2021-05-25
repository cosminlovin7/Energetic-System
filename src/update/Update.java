package update;

import java.util.ArrayList;
import java.util.List;

public final class Update {
    private List<CostChange> costChanges;
    private List<NewConsumer> newConsumers;

    public Update() {
        this.costChanges = new ArrayList<>();
        this.newConsumers = new ArrayList<>();
    }

    /**
     * @return schimbul costurilor noi
     */
    public List<CostChange> getCostChanges() {
        return costChanges;
    }

    /**
     * @return noul consumator
     */
    public List<NewConsumer> getNewConsumers() {
        return newConsumers;
    }

    /**
     * Suprascrierea metodei toString()
     * @return
     */
    @Override
    public String toString() {
        return "Update{"
                + "costChange=" + costChanges
                + ", newConsumer=" + newConsumers
                + '}';
    }
}
