package members;

public interface Members {
    /**
     * Intoarce id-ul unui membru
     * @return
     */
    Integer getId();

    /**
     * Intoarce bugetul initial
     * @return
     */
    long getInitialBudget();

    /**
     * Intoarce true/false daca e falimentat
     * @return
     */
    boolean getIsBankrupt();

    /**
     * Intoarce bugetul curent
     * @return
     */
    long getBudget();

    /**
     * Updateaza valoarea campului isBankrupt
     * @param isBankrupt
     */
    void setIsBankrupt(boolean isBankrupt);
}
