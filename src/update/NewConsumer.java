package update;

public final class NewConsumer implements UpdateMember {
    private Integer id;
    private long initialBudget;
    private long monthlyIncome;

    public NewConsumer(final Integer id,
                       final long initialBudget,
                       final long monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * @return id-ul noului consumator
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return bugetul initial al noului consumator
     */
    public long getInitialBudget() {
        return initialBudget;
    }

    /**
     * @return venitul lunar al noului consumator
     */
    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Suprascrierea metodei toString()
     * @return
     */
    @Override
    public String toString() {
        return "NewConsumer{"
                + "id=" + id
                + ", initialBudget=" + initialBudget
                + ", monthlyIncome=" + monthlyIncome
                + '}';
    }
}
