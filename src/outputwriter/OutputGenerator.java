package outputwriter;

import members.Consumer;
import members.Distributor;
import org.json.simple.JSONArray;

import java.util.Map;

public final class OutputGenerator {
    private static final OutputGenerator INSTANCE = new OutputGenerator();
    private OutputGenerator() { }
    public static OutputGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Intoarce un jsonArray ce contine toti consumatorii din joc
     * @param consumersList
     * @return
     */
    public JSONArray getJSONConsumers(final Map<Integer, Consumer> consumersList) {
        JSONArray jsonConsumers = new JSONArray();

        for (Map.Entry<Integer, Consumer> consumerIterator : consumersList.entrySet()) {
            Consumer consumer = consumerIterator.getValue();
            jsonConsumers.add(consumer);
        }

        return jsonConsumers;
    }

    /**
     * Intoarce un jsonArray ce contine toti distribuitorii din joc
     * @param distributorsList
     * @return
     */
    public JSONArray getJSONDistributors(final Map<Integer, Distributor> distributorsList) {
        JSONArray jsonDistributors = new JSONArray();

        for (Map.Entry<Integer, Distributor> distributorIterator : distributorsList.entrySet()) {
            Distributor distributor = distributorIterator.getValue();
            Map<Integer, Distributor.Contracts> contractsMap = distributor.getContractsMap();
            for (Map.Entry<Integer, Distributor.Contracts> contractsIterator
                    : contractsMap.entrySet()) {
                distributor.getContracts().add(contractsIterator.getValue());
            }
            distributor.getContracts().sort(new SortByContractAndId());
            jsonDistributors.add(distributor);
        }

        return jsonDistributors;
    }
}
