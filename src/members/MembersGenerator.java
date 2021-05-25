package members;

/**
 * Factory pentru consumatori si distribuitori
 * Clasa implementeaza un singleton si intoarce
 * un consumator sau un distribuitor
 */
public final class MembersGenerator {
    private static final MembersGenerator INSTANCE = new MembersGenerator();
    private MembersGenerator() { }
    public static MembersGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * @param id
     * @param initialBudget
     * @param monthlyIncome
     * @return un nou consumator(input)
     */
    public Members getMember(final int id,
                             final int initialBudget,
                             final int monthlyIncome) {
        return new Consumer(id, initialBudget, monthlyIncome);
    }

    /**
     * @param id
     * @param contractLength
     * @param initialBudget
     * @param initialInfrastructureCost
     * @param initialProductionCost
     * @return un nou distribuitor
     */
    public Members getMember(final int id,
                             final int contractLength,
                             final int initialBudget,
                             final int initialInfrastructureCost,
                             final int initialProductionCost) {
        return new Distributor(id,
                contractLength,
                initialBudget,
                initialInfrastructureCost,
                initialProductionCost);
    }
}
