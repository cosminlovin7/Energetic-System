package inputreader;

import members.Consumer;
import members.Distributor;
import update.Update;
import java.util.List;
import java.util.Map;

public final class Input {
    /**
     * Numarul de schimbari
     */
    private Integer numberOfTurns;
    /**
     * Lista de consumatori
     */
    private Map<Integer, Consumer> consumers;
    /**
     * Lista de distribuitori
     */
    private Map<Integer, Distributor> distributors;
    /**
     * Lista de update-uri lunare
     */
    private List<Update> monthlyUpdates;

    public Input(final Integer numberOfTurns,
                 final Map<Integer, Consumer> consumers,
                 final Map<Integer, Distributor> distributors,
                 final List<Update> montlyUpdates) {
        this.numberOfTurns = numberOfTurns;
        this.consumers = consumers;
        this.distributors = distributors;
        this.monthlyUpdates = montlyUpdates;
    }

    /**
     * @return numberOfTurns
     */
    public Integer getNumberOfTurns() {
        return numberOfTurns;
    }

    /**
     * @return lista de consumatori
     */
    public Map<Integer, Consumer> getConsumers() {
        return consumers;
    }

    /**
     * @return lista de distribuitori
     */
    public Map<Integer, Distributor> getDistributors() {
        return distributors;
    }

    /**
     * @return lista de update-uri lunare
     */
    public List<Update> getMonthlyUpdates() {
        return monthlyUpdates;
    }
}
