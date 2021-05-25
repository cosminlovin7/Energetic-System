package members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import utils.Constants;

/**
 * Clasa de consumatori.
 * Fiecare consumator are:
 * - un id
 * - un buget initial
 * - un venit lunar
 */
@JsonPropertyOrder({ "id", "isBankrupt", "budget" })
@JsonIgnoreProperties({ "initialBudget",
                        "monthlyIncome",
                        "contractValue",
                        "hasDistributor",
                        "idToPayDebt",
                        "remainedContractMonths",
                        "distributorId"})
public final class Consumer implements Members {
    private final Integer id;
    private long initialBudget;
    private long monthlyIncome;

    /**
     * Variabile folosite pentru output
     */
    private boolean isBankrupt = false;
    private long budget;

    /**
     * Variabile folosite pentru datorii
     */
    private boolean hasDebts = false;
    private long debtsToPay;
    private Integer idToPayDebt = -1;

    /**
     * Variabile folosite pentru simulare
     */
    private Integer remainedContractMonths;
    private long contractValue;
    private boolean hasDistributor = false;
    private Integer distributorId = -1;

    public Consumer(final Integer id,
                    final long initialBudget,
                    final long monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * Seteaza toate datoriile ca fiind false
     */
    public void setDebtsFalse() {
        debtsToPay = 0;
        hasDebts = false;
        idToPayDebt = -1;
    }

    /**
     * Updateaza durata contractului
     * @param value
     */
    public void setRemainedContractMonths(final Integer value) {
        this.remainedContractMonths += value;
    }
    /**
     * Seteaza contractulActual si durata sa
     */
    public void setContract(final long newContractValue,
                            final Integer newContractLength,
                            final Integer newDistributorId) {
        this.contractValue = newContractValue;
        this.remainedContractMonths = newContractLength;
        this.distributorId = newDistributorId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    /**
     * @return lunile ramase din contract
     */
    public Integer getRemainedContractMonths() {
        return remainedContractMonths;
    }

    /**
     * Updateaza buget dupa salariu
     */
    public void updateBudget() {
        budget += monthlyIncome;
    }

    /**
     * Plateste taxele lunare
     */
    public void payTaxes(final Distributor distributor) {
        if (budget - contractValue >= 0) {
            budget -= contractValue;
            distributor.increaseBudget(contractValue);
        } else {
            hasDebts = true;
            debtsToPay = Math.round(Math.floor(Constants.DEBTS_MULTIPLIER * contractValue));
            idToPayDebt = distributor.getId();
        }
        setRemainedContractMonths(-1);
    }

    /**
     * Plateste datoriile
     */
    public void payDebts(final Distributor oldDistributor,
                         final Distributor currentDistributor) {
        if (budget - debtsToPay - contractValue >= 0) {
            budget -= debtsToPay;
            budget -= contractValue;
            oldDistributor.increaseBudget(debtsToPay);
            currentDistributor.increaseBudget(contractValue);
            setDebtsFalse();
        } else {
            isBankrupt = true;
            return;
        }
        setRemainedContractMonths(-1);

    }
    /**
     * @return true/false daca are datorii
     */
    public boolean hasDebts() {
        return hasDebts;
    }

    /**
     * @return distributor id to pay debts
     */
    public Integer getIdToPayDebt() {
        return idToPayDebt;
    }

    /**
     * @return contract de platit
     */
    public long getContractValue() {
        return contractValue;
    }

    /**
     * @return true/false daca are distribuitor
     */
    public boolean hasDistributor() {
        return hasDistributor;
    }

    /**
     *
     * @param value
     */
    public void setDistributor(final boolean value,
                               final Integer newDistributorId) {
        hasDistributor = value;
        this.distributorId = newDistributorId;
    }

    /**
     * @return id-ul consumatorului
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return bugetul initial al consumatorului
     */
    @Override
    public long getInitialBudget() {
        return initialBudget;
    }

    /**
     * @return venitul lunar al consumatorului
     */
    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * @return isBankrupt?
     */
    @Override
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * @return bugetul consumatorului
     */
    @Override
    public long getBudget() {
        return budget;
    }

    /**
     * Seteaza valoarea lui isBankrupt
     * @param isBankrupt
     */
    @Override
    public void setIsBankrupt(final boolean isBankrupt) {
        this.isBankrupt = isBankrupt;
    }

    /**
     * Seteaza bugetul consumatorului
     * @param budget
     */
    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    /**
     * Suprascrierea metodei toString()
     */
    @Override
    public String toString() {
        return "Consumer{"
                + "id=" + id
                + ", initialBudget=" + initialBudget
                + ", monthlyIncome=" + monthlyIncome
                + ", isBankrupt=" + isBankrupt
                + ", budget=" + budget
                + ", contractValue=" + contractValue
                + ", hasDistributor=" + hasDistributor
                + '}';
    }
}
