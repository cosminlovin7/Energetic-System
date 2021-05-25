package simulate;

import members.Consumer;
import members.Distributor;
import update.CostChange;
import update.NewConsumer;
import utils.Constants;

import java.util.List;
import java.util.Map;

public final class Action {
    private static final Action INSTANCE = new Action();
    private Action() { }
    public static Action getInstance() {
        return INSTANCE;
    }

    /**
     * Prin intermediul acestei metode, toti consumatorii vor
     * primi salariul corespunzator luni curente
     * @param consumersList
     */
    public void updateSalaries(final Map<Integer, Consumer> consumersList) {
        for (Map.Entry<Integer, Consumer> iterator : consumersList.entrySet()) {
            Consumer consumer = iterator.getValue();
            if (!consumer.getIsBankrupt()) {
                consumer.updateBudget();
            }
        }
    }

    /**
     * Prin intermediul aceste metode, se introduc in joc noii
     * consumatori care apar lunar
     * @param consumersList
     * @param newConsumersList
     */
    public void addNewConsumers(final Map<Integer, Consumer> consumersList,
                                final List<NewConsumer> newConsumersList) {
        for (NewConsumer newConsumer : newConsumersList) {
            if (newConsumer.getId() >= 0) {
                consumersList.put(newConsumer.getId(),
                        new Consumer(newConsumer.getId(),
                                newConsumer.getInitialBudget(),
                                newConsumer.getMonthlyIncome()));
            }
        }
    }

    /**
     * Prin intermediul acestei metode se updateaza valorile contractelor
     * oferite de distribuitori in functie de cati clienti au
     * @param distributorList
     */
    public void updateDistributorPrices(final Map<Integer, Distributor> distributorList) {
        for (Map.Entry<Integer, Distributor> iterator : distributorList.entrySet()) {
            Distributor distributor = iterator.getValue();
            long profit = Math.round(Math.floor(Constants.PROFIT_MULTIPLIER
                    * distributor.getProductionCost()));
            long contractValue;
            if (distributor.getContractsMap().size() == 0) {
                contractValue = distributor.getProductionCost()
                        + distributor.getInfrastructureCost()
                        + profit;
            } else {
                contractValue = Math.round(Math.floor(distributor.getInfrastructureCost()
                        / distributor.getContractsMap().size()
                        + distributor.getProductionCost() + profit));
            }
            distributor.setContractDetails(profit, contractValue);
        }
    }

    /**
     * Prin intermediul acestei metode se updateaza cost-urile
     * fiecarui distribuitor lunar
     * @param distributorsList
     * @param costChanges
     */
    public void updateDistributorCosts(final Map<Integer, Distributor> distributorsList,
                                       final List<CostChange> costChanges) {
        for (CostChange costChange : costChanges) {
            if (costChange.getId() >= 0) {
                Distributor distributor = distributorsList.get(costChange.getId());
                distributor.setInfrastructureCost(costChange.getInfrastructureCost());
                distributor.setProductionCost(costChange.getProductionCost());
            }
        }
    }

    /**
     * Prin intermediul acestei metode, fiecarui consumator ii este atribuit
     * un distribuitor de energie
     * @param bestDistributor
     * @param consumersList
     * @param distributorsList
     */
    public void setContracts(final Distributor bestDistributor,
                             final Map<Integer, Consumer> consumersList,
                             final Map<Integer, Distributor> distributorsList) {
        for (Map.Entry<Integer, Consumer> iterator : consumersList.entrySet()) {
            Consumer consumer = iterator.getValue();

            if (!consumer.hasDistributor() && !consumer.getIsBankrupt()) {
                bestDistributor.addContract(consumer.getId(),
                        bestDistributor.getContractValue(),
                        bestDistributor.getContractLength());
                consumer.setDistributor(true, bestDistributor.getId());
                consumer.setContract(bestDistributor.getContractValue(),
                        bestDistributor.getContractLength(),
                        bestDistributor.getId());

            } else if (consumer.getRemainedContractMonths() == 0 && !consumer.getIsBankrupt()) {
                if (bestDistributor.getId() == consumer.getDistributorId()) {
                    bestDistributor.setNewContract(consumer.getId(),
                            bestDistributor.getContractValue(),
                            bestDistributor.getContractLength());
                    consumer.setDistributor(true, bestDistributor.getId());
                    consumer.setContract(bestDistributor.getContractValue(),
                            bestDistributor.getContractLength(),
                            bestDistributor.getId());
                } else {
                    for (Map.Entry<Integer, Distributor> distributorIterator
                            : distributorsList.entrySet()) {
                        Distributor distributor = distributorIterator.getValue();
                        if (distributor.getContractsMap().containsKey(consumer.getId())) {
                            distributor.getContractsMap().remove(consumer.getId());
                            break;
                        }
                    }
                    bestDistributor.addContract(consumer.getId(),
                            bestDistributor.getContractValue(),
                            bestDistributor.getContractLength());
                    consumer.setDistributor(true, bestDistributor.getId());
                    consumer.setContract(bestDistributor.getContractValue(),
                            bestDistributor.getContractLength(),
                            bestDistributor.getId());
                }
            } else {
                if (consumer.getDistributorId() >= 0) {
                    Distributor currentDistributor
                            = distributorsList.get(consumer.getDistributorId());
                    if (currentDistributor.getIsBankrupt()) {
                        bestDistributor.setNewContract(consumer.getId(),
                                bestDistributor.getContractValue(),
                                bestDistributor.getContractLength());
                        consumer.setDistributor(true, bestDistributor.getId());
                        consumer.setContract(bestDistributor.getContractValue(),
                                bestDistributor.getContractLength(),
                                bestDistributor.getId());
                    }
                }
            }
        }
    }

    /**
     * Prin intermediul acestei metode, consumatorii isi vor plati
     * taxele lunare catre distribuitorii cu care au contract
     * @param consumersList
     * @param distributorsList
     */
    public void consumerPayTaxes(final Map<Integer, Consumer> consumersList,
                                 final Map<Integer, Distributor> distributorsList) {
        for (Map.Entry<Integer, Distributor> iterator : distributorsList.entrySet()) {
            Distributor distributor = iterator.getValue();
            for (Map.Entry<Integer, Distributor.Contracts> contractIterator
                    : distributor.getContractsMap().entrySet()) {
                Distributor.Contracts contract = contractIterator.getValue();
                Consumer currentConsumer = consumersList.get(contract.getConsumerId());
                if (contract.getRemainedContractMonths() > 0 && !currentConsumer.hasDebts()) {
                    currentConsumer.payTaxes(distributor);
                    contract.setRemainedContractMonths(contract.getRemainedContractMonths() - 1);
                } else if (currentConsumer.hasDebts()) {
                    Distributor oldDistributor
                            = distributorsList.get(currentConsumer.getIdToPayDebt());
                    currentConsumer.payDebts(oldDistributor, distributor);
                    contract.setRemainedContractMonths(contract.getRemainedContractMonths() - 1);
                }
            }
        }
    }

    /**
     * Prin intermediul acestei metode, distribuitorii isi vor plati taxele
     * lunare in functie de cati clienti au
     * @param consumersList
     * @param distributorsList
     */
    public void distributorPayTaxes(final Map<Integer, Consumer> consumersList,
                                    final Map<Integer, Distributor> distributorsList) {
        for (Map.Entry<Integer, Distributor> iterator : distributorsList.entrySet()) {
            Distributor distributor = iterator.getValue();
            distributor.payTaxes();
            distributor.checkIfBankrupt(consumersList);
        }
    }

    /**
     * In cazul in care un client da faliment acesta trebuie sters din lista
     * de clienti a distribuitorului cu care a incheiat contractul
     * @param consumersList
     * @param distributorsList
     */
    public void cleanConsumerList(final Map<Integer, Consumer> consumersList,
                                  final Map<Integer, Distributor> distributorsList) {
        for (Map.Entry<Integer, Consumer> consumerIterator : consumersList.entrySet()) {
            Consumer consumer = consumerIterator.getValue();
            if (consumer.hasDistributor()) {
                Distributor cDistributor = distributorsList.get(consumer.getDistributorId());
                if (consumer.getIsBankrupt()) {
                    cDistributor.getContractsMap().remove(consumer.getId());
                    consumer.setDistributor(false, -1);
                }
            }
        }
    }
}
