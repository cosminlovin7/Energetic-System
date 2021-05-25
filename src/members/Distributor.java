package members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@JsonPropertyOrder({ "id", "budget", "isBankrupt", "contracts" })
@JsonIgnoreProperties({ "contractLength",
                        "initialBudget",
                        "initialInfrastructureCost",
                        "initialProductionCost",
                        "contractsMap",
                        "profit",
                        "infrastructureCost",
                        "productionCost",
                        "totalCosts",
                        "contractValue"
                        })
public final class Distributor implements Members {
    /**
     * Clasa interna de contracte pentru distribuitori
     */
    @JsonPropertyOrder({ "consumerId", "price", "remainedContractMonths" })
     public class Contracts {
        private Integer consumerId;
        private long price;
        private Integer remainedContractMonths;

        public Contracts(final Integer consumerId,
                         final long price,
                         final Integer remainedContractMonths) {
            this.consumerId = consumerId;
            this.price = price;
            this.remainedContractMonths = remainedContractMonths;
        }

        /**
         * @return id-ul consumatorului
         */
        public Integer getConsumerId() {
            return consumerId;
        }

        /**
         * @return pretul platit de consumator
         */
        public long getPrice() {
            return price;
        }

        /**
         * @return durata ramasa pentru contract
         */
        public Integer getRemainedContractMonths() {
            return remainedContractMonths;
        }

        /**
         * Seteaza valoarea contractului
         * @param price
         */
        public void setPrice(final long price) {
            this.price = price;
        }

        /**
         * Seteaza durata contractului
         * @param remainedContractMonths
         */
        public void setRemainedContractMonths(final Integer remainedContractMonths) {
            this.remainedContractMonths = remainedContractMonths;
        }

        /**
         * Suprascrierea clasei toString()
         * @return
         */
        @Override
        public String toString() {
            return "Contracts{"
                    + "consumerId=" + consumerId
                    + ", price=" + price
                    + ", remainedContractMonths=" + remainedContractMonths
                    + '}';
        }
    }

    /**
     * Variabile folosite pentru input
     */
    private final Integer id;
    private Integer contractLength;
    private long initialBudget;
    private long initialInfrastructureCost;
    private long initialProductionCost;

    /**
     * Variabile folosite pentru output
     */
    private long budget;
    private boolean isBankrupt = false;
    private List<Contracts> contracts = new ArrayList<>();

    /**
     * Variabile folosite pentru simulare
     */
    private long profit;
    private long contractValue;
    private long totalCosts;
    private long infrastructureCost;
    private long productionCost;
    private Map<Integer, Contracts> contractsMap = new HashMap<>();

    public Distributor(final Integer id,
                       final Integer contractLength,
                       final long initialBudget,
                       final long initialInfrastructureCost,
                       final long initialProductionCost) {
        this.id = id;
        this.contractLength = contractLength;
        this.initialBudget = initialBudget;
        budget = initialBudget;
        this.initialInfrastructureCost = initialInfrastructureCost;
        infrastructureCost = initialInfrastructureCost;
        this.initialProductionCost = initialProductionCost;
        productionCost = initialProductionCost;
    }

    /**
     * Updateaza infrastructureCost
     * @param infrastructureCost
     */
    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    /**
     * Updateaza productionCost
     * @param productionCost
     */
    public void setProductionCost(final long productionCost) {
        this.productionCost = productionCost;
    }

    /**
     * Updateaza detaliile legate de contract
     * - profitul obtinut per utilizator
     * - costul unui contract
     * @param newProfit
     * @param newContractValue
     */
    public void setContractDetails(final long newProfit,
                                   final long newContractValue) {
        this.profit = newProfit;
        this.contractValue = newContractValue;
    }
    /**
     * @return profitul facut lunar
     */
    public long getProfit() {
        return profit;
    }

    /**
     * Intoarce lista de contracte a distribuitorului
     * @return
     */
    public List<Contracts> getContracts() {
        return contracts;
    }

    /**
     * @return valoarea contractului actual
     */
    public long getContractValue() {
        return contractValue;
    }

    /**
     * @return costurile totale lunare
     */
    public long getTotalCosts() {
        return totalCosts;
    }

    /**
     * @return costul lunar al infrastructurii
     */
    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    /**
     * @return costul lunar pentru producere
     */
    public long getProductionCost() {
        return productionCost;
    }

    /**
     * Primeste toate facturile
     */
    public void increaseBudget(final long taxes) {
        budget += taxes;
    }

    /**
     * Plateste taxele aferente fiecarei luni
     */
    public void payTaxes() {
        totalCosts = infrastructureCost + productionCost * contractsMap.size();
        if (!isBankrupt) {
            if (budget - totalCosts >= 0) {
                budget -= totalCosts;
            } else {
                budget -= totalCosts;
                isBankrupt = true;
            }
        }
    }

    /**
     * Daca distribuitorul este falimentat, atunci sterge
     * lista de clienti si anuleaza datoriile daca au
     * @param consumersList
     */
    public void checkIfBankrupt(final Map<Integer, Consumer> consumersList) {
        if (isBankrupt) {
            for (Map.Entry<Integer, Contracts> contract : contractsMap.entrySet()) {
                Consumer consumer = consumersList.get(contract.getValue().getConsumerId());
                consumer.setDebtsFalse();
            }
            contractsMap.clear();
        }
    }

    /**
     * Seteaza daca e falimentat sau nu
     */
    @Override
    public void setIsBankrupt(final boolean isBankrupt) {
        this.isBankrupt = isBankrupt;
    }

    /**
     * Adauga un nou contract la lista de contracte
     * @param consumerId
     * @param price
     * @param remainedContractMonths
     */
    public void addContract(final Integer consumerId,
                            final long price,
                            final Integer remainedContractMonths) {
        contractsMap.put(consumerId, new Contracts(consumerId, price, remainedContractMonths));
    }

    /**
     * Updateaza un contract deja existent in lista de contracte
     * @param consumerId
     * @param price
     * @param remainedContractMonths
     */
    public void setNewContract(final Integer consumerId,
                               final long price,
                               final Integer remainedContractMonths) {
        contractsMap.replace(consumerId, new Contracts(consumerId, price, remainedContractMonths));
    }
    /**
     * @return isBankrupt?
     */
    @Override
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * @return lista de contracte
     */
    public Map<Integer, Contracts> getContractsMap() {
        return contractsMap;
    }

    /**
     * @return buget
     */
    @Override
    public long getBudget() {
        return budget;
    }

    /**
     * @return id-ul distribuitorului
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return bugetul initial al distribuitorului
     */
    @Override
    public long getInitialBudget() {
        return initialBudget;
    }

    /**
     * @return lungimea contractului
     */
    public Integer getContractLength() {
        return contractLength;
    }

    /**
     * @return costul initial al infrastructurii
     */
    public long getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    /**
     * @return costul initial al producerii
     */
    public long getInitialProductionCost() {
        return initialProductionCost;
    }

    /**
     * Suprascrie metoda de toString()
     */
    @Override
    public String toString() {
        return "Distributor{"
                + "id=" + id
                + ", contractLength=" + contractLength
                + ", initialBudget=" + initialBudget
                + ", initialInfrastructureCost=" + initialInfrastructureCost
                + ", initialProductionCost=" + initialProductionCost
                + ", budget=" + budget
                + ", isBankrupt=" + isBankrupt
                + ", contracts=" + contracts
                + '}';
    }
}
