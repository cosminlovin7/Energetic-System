package simulate;

import members.Consumer;
import members.Distributor;
import update.CostChange;
import update.NewConsumer;
import update.Update;

import java.util.List;
import java.util.Map;


/**
 * Clasa ce simuleaza actiunile din fiecare luna
 * Implementeaza un singleton
 */
public final class Simulate {
    private static final Simulate INSTANCE = new Simulate();
    private Simulate() { }
    public static Simulate getInstance() {
        return INSTANCE;
    }

    /**
     * Intoarce distribuitorul cu cel mai bun cotract
     * posibil la momentul actual
     * @param distributorsList
     * @return
     */
    Distributor getBestContract(final Map<Integer, Distributor> distributorsList) {
        Distributor bestDistributor = null;
        for (Map.Entry<Integer, Distributor> distributorIterator : distributorsList.entrySet()) {
            Distributor distributor = distributorIterator.getValue();
            if (!distributor.getIsBankrupt()) {
                bestDistributor = distributor;
                break;
            }
        }
        for (Map.Entry<Integer, Distributor> distributorIterator : distributorsList.entrySet()) {
            Distributor distributor = distributorIterator.getValue();
            if (!distributor.getIsBankrupt()) {
                if (bestDistributor.getContractValue() > distributor.getContractValue()) {
                    bestDistributor = distributor;
                }
            }
        }

        return bestDistributor;
    }

    /**
     * Realizeaza prima etapa de simulare, cea initiala
     * @param consumersList
     * @param distributorsList
     */
    public void initiate(final Map<Integer, Consumer> consumersList,
                         final Map<Integer, Distributor> distributorsList) {
        Action action = Action.getInstance();

        action.updateDistributorPrices(distributorsList);
        Distributor bestDistributor = getBestContract(distributorsList);

        if (bestDistributor == null) {
            return;
        }

        action.updateSalaries(consumersList);
        action.setContracts(bestDistributor, consumersList, distributorsList);
        action.consumerPayTaxes(consumersList, distributorsList);
        action.distributorPayTaxes(consumersList, distributorsList);
        action.cleanConsumerList(consumersList, distributorsList);
    }

    /**
     * Metoda simuleaza o luna din jocul nostru
     * @param consumersList
     * @param distributorsList
     * @param update
     */
    public void simulateOneMonth(final Map<Integer, Consumer> consumersList,
                                 final Map<Integer, Distributor> distributorsList,
                                 final Update update) {
        List<NewConsumer> newConsumers = update.getNewConsumers();
        List<CostChange> costChanges = update.getCostChanges();
        Action action = Action.getInstance();

        action.addNewConsumers(consumersList, newConsumers);
        action.updateDistributorCosts(distributorsList, costChanges);

        action.updateDistributorPrices(distributorsList);
        Distributor bestDistributor = getBestContract(distributorsList);

        if (bestDistributor == null) {
            return;
        }

        action.updateSalaries(consumersList);
        action.setContracts(bestDistributor, consumersList, distributorsList);
        action.consumerPayTaxes(consumersList, distributorsList);
        action.distributorPayTaxes(consumersList, distributorsList);
        action.cleanConsumerList(consumersList, distributorsList);

    }

    /**
     * Metoda simuleaza intregul joc
     * @param numberOfTurns
     * @param consumersList
     * @param distributorsList
     * @param monthlyUpdateList
     */
    public void game(final Integer numberOfTurns,
                     final Map<Integer, Consumer> consumersList,
                     final Map<Integer, Distributor> distributorsList,
                     final List<Update> monthlyUpdateList) {
        initiate(consumersList, distributorsList);

        for (int i = 0; i < numberOfTurns; i++) {
            Update update = monthlyUpdateList.get(i);
            simulateOneMonth(consumersList, distributorsList, update);
        }
    }
}
