package update;

public final class CostChange implements UpdateMember {
    private Integer id;
    private long infrastructureCost;
    private long productionCost;

    public CostChange(final Integer id,
                      final long infrastructureCost,
                      final long productionCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
    }

    /**
     * @return id-ul distribuitorului ce trebuie updatat
     */
    @Override
    public Integer getId() {
        return id;
    }
    /**
     * @return noul cost de infrastructura
     */
    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    /**
     * @return noul cost al productiei
     */
    public long getProductionCost() {
        return productionCost;
    }

    /**
     * Suprascrierea metodei toString()
     * @return
     */
    @Override
    public String toString() {
        return "CostChange{"
                + "id=" + id
                + ", infrastructureCost=" + infrastructureCost
                + ", productionCost=" + productionCost
                + '}';
    }
}
